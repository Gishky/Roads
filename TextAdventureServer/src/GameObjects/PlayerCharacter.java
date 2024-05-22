package GameObjects;

import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;

import AdminConsole.AdminConsole;
import GameObjects.Blocks.Block;
import GameObjects.Blocks.BlockAir;
import HelperObjects.CraftingHandler;
import HelperObjects.Hitbox;
import HelperObjects.JSONObject;
import HelperObjects.Position;
import HelperObjects.VirtualKeyboard;
import Server.GameMaster;
import UDPServer.UDPClientConnection;
import UDPServer.UDPClientObject;
import UDPServer.UDPServer;

public class PlayerCharacter extends Entity implements UDPClientObject {

	private VirtualKeyboard keyboard;

	private UDPClientConnection connection;

	private boolean placeFlag = false;
	private double craftingRange = 4;

	private int heldBlock = 0;
	private Block[] inventory;
	private int breakCount = 0;
	private int maxHP = 0;
	private int HP;

	public PlayerCharacter() {
		super(new Position(World.getWorld().length / 2 + 0.5,
				World.getHeight((int) (World.getWorld().length / 2 + 0.5)) - 0.5));
		inventory = new Block[5];
		hitBox = new Hitbox(false, 0.25);
		keyboard = new VirtualKeyboard();
		maxHP = 100;
		HP = maxHP;
		this.accelleration = 0.075;
		mouse = new Position();
	}

	public void receivedMessage(String message) {
		String[] contents = message.split(";");
		if (contents[0].equals("key")) {
			keyboard.inputReceived(message);
		} else if (contents[0].equals("block")) {
			connection.sendMessage("{action:setBlock,block:"
					+ World.getBlock(Integer.parseInt(contents[1]), Integer.parseInt(contents[2])).toJSON() + "}",
					true);
		} else if (contents[0].equals("reboot")) {
			GameMaster.restartServer();
		} else if (contents[0].equals("mouse")) {
			mouse.set(Double.parseDouble(contents[1]), Double.parseDouble(contents[2]));
		} else if (contents[0].equals("scroll")) {
			scrollInventory(contents[1]);
		}

	}

	private int fireCooldown = 0;
	private boolean placing = false;

	@Override
	public boolean action() {
		int oldX = (int) pos.getX();
		int oldY = (int) pos.getY();

		if (keyboard == null)
			return false;

		fireCooldown--;
		boolean update = false;

		if (keyboard.getKey("" + KeyEvent.VK_A)) {
			velocity[0] -= accelleration * (velocity[0] > 0 ? 2 : 1) * (isGrounded ? 2 : 1);
		}
		if (keyboard.getKey("" + KeyEvent.VK_D)) {
			velocity[0] += accelleration * (velocity[0] < 0 ? 2 : 1) * (isGrounded ? 2 : 1);
		}
		if (keyboard.getKey("" + KeyEvent.VK_W)) {
			if (isGrounded)
				velocity[1] = -jumpforce;
		}
		if (keyboard.getKey("" + KeyEvent.VK_X)) {
			if (getHeldBlock().getId() != -1) {
				setHeldBlock(null);
				update = true;
			}
		}
		if (keyboard.getKey("" + KeyEvent.VK_1)) {
			heldBlock = 0;
			scrollInventory("");
		}
		if (keyboard.getKey("" + KeyEvent.VK_2)) {
			heldBlock = 1;
			scrollInventory("");
		}
		if (keyboard.getKey("" + KeyEvent.VK_3)) {
			heldBlock = 2;
			scrollInventory("");
		}
		if (keyboard.getKey("" + KeyEvent.VK_4)) {
			heldBlock = 3;
			scrollInventory("");
		}
		if (keyboard.getKey("" + KeyEvent.VK_5)) {
			heldBlock = 4;
			scrollInventory("");
		}
		if (keyboard.getKey("" + MouseEvent.BUTTON1)) {
			if (fireCooldown <= 0 && getHeldBlock().getId() != -1) {
				fireCooldown = getHeldBlock().getAbilityCooldown();
				getHeldBlock().activateAbility(this);
			} else if (getHeldBlock().getId() == -1) {
				if (Math.sqrt(Math.pow(mouse.getX(), 2) + Math.pow(mouse.getY(), 2)) <= craftingRange) {
					CraftingHandler.tryCrafting((int) (mouse.getX() + pos.getX()), (int) (mouse.getY() + pos.getY()));
				}
			}
		}
		if (keyboard.getKey("" + KeyEvent.VK_S)) {
			if (isGrounded) {
				if (getHeldBlock().getId() == -1
						&& World.getBlock((int) pos.getX(), (int) (pos.getY() + 1)).getBreakThreshhold() <= breakCount
						&& World.getBlock((int) pos.getX(), (int) (pos.getY() + 1)).isBlocksMovement()) {
					setHeldBlock(World.getBlock((int) pos.getX(), (int) (pos.getY()) + 1));
					World.setBlock((int) pos.getX(), (int) (pos.getY()) + 1, new BlockAir());
					breakCount = 0;
				} else if (getHeldBlock().getId() != -1 && placeFlag
						&& !World.getBlock((int) pos.getX(), (int) (pos.getY()) - 1).isBlocksMovement()) {
					placing = true;
					velocity[1] = -jumpforce;
				}
				if (getHeldBlock().getId() == -1) {
					update = true;
					breakCount++;
				}
			} else {
				if (getHeldBlock().getId() != -1 && placeFlag
						&& !World.getBlock((int) pos.getX(), (int) (pos.getY()) + 1).isBlocksMovement()) {
					World.setBlock((int) pos.getX(), (int) (pos.getY()) + 1, getHeldBlock());
					setHeldBlock(null);
				}
			}
			placeFlag = false;
		} else {
			if (breakCount != 0) {
				update = true;
			}
			placeFlag = true;
			breakCount = 0;
		}

		if (placing && !World.getBlock((int) pos.getX(), (int) pos.getY() + 1).isBlocksMovement()) {
			World.setBlock((int) pos.getX(), (int) (pos.getY()) + 1, getHeldBlock());
			setHeldBlock(null);
			placing = false;
		}

		update = super.action() || update;

		if (oldX != (int) pos.getX() || oldY != (int) pos.getY()) {
			breakCount = 0;
		}

		return update;
	}

	public void receiveDamage(int damage) {
		HP -= damage;
		if (HP <= 0) {
			GameMaster.removeEntity(this);
			return;
		}
		actionUpdateOverride = true;
	}

	public UDPClientConnection getConnection() {
		return connection;
	}

	public Block getHeldBlock() {
		if (inventory == null || inventory[heldBlock] == null)
			return new Block();
		return inventory[heldBlock];
	}

	public void setHeldBlock(Block b) {
		inventory[heldBlock] = b;
		connection.sendMessage("{action:inventoryUpdate,inventory:" + inventoryJSON() + "}", placeFlag);
	}

	public Block getInventory(int slot) {
		if (inventory == null || inventory[slot] == null)
			return new Block();
		return inventory[slot];
	}

	private int getHPPercentile() {
		if (maxHP == 0)
			return 100;
		return HP * 100 / maxHP;
	}

	public int getHP() {
		return HP;
	}

	@Override
	public void remove() {
		GameMaster.removeEntity(this);
	}

	@Override
	public void setClientConnection(UDPClientConnection con) {
		AdminConsole.log("New Player connected from: " + con.getAddress() + ":" + con.getPort() + ". Players online: "
				+ UDPServer.getInstances().get(0).getClientCount());
		connection = con;
		sendInitialData();
	}

	private void sendInitialData() {
		connection.sendMessage("{action:playerID,playerID:" + id + "}", true);
		for (Entity e : GameMaster.getEntities()) {
			if (!e.equals(this))
				connection.sendMessage("{action:createEntity,entity:" + e.toJSON() + "}", true);

		}
		connection.sendMessage(
				"{action:createWorld,length:" + World.getWorld().length + ",height:" + World.getWorld()[0].length + "}",
				true);
	}

	private void scrollInventory(String string) {
		if (string.equals("up")) {
			heldBlock--;
			if (heldBlock <= 0)
				heldBlock = 0;
		} else if (string.equals("down")) {
			heldBlock++;
			if (heldBlock >= inventory.length)
				heldBlock = inventory.length - 1;
		}

		breakCount = 0;
		updateInventory();
		this.actionUpdateOverride = true;
	}

	@Override
	public void disconnected() {
		AdminConsole.log("Player at " + connection.getAddress() + ":" + connection.getPort()
				+ " disconnected. Players online: " + UDPServer.getInstances().get(0).getClientCount());
		GameMaster.removeEntity(this);
	}

	public void updateInventory() {
		connection.sendMessage("{action:inventoryUpdate,inventory:" + inventoryJSON() + "}", placeFlag);
	}

	private String inventoryJSON() {
		JSONObject json = new JSONObject();
		json.put("heldid", "" + heldBlock);
		json.put("block0", getInventory(0).toJSON());
		json.put("block1", getInventory(1).toJSON());
		json.put("block2", getInventory(2).toJSON());
		json.put("block3", getInventory(3).toJSON());
		json.put("block4", getInventory(4).toJSON());
		return json.getJSON();
	}

	public String toJSON() {
		JSONObject json = new JSONObject();
		json.put("type", "player");
		json.put("id", "" + id);
		json.put("x", String.format("%.4f", pos.getX()));
		json.put("y", String.format("%.4f", pos.getY()));
		json.put("heldBlock", getHeldBlock().toJSON());
		json.put("breakCount", "" + breakCount);
		json.put("hp%", "" + getHPPercentile());
		return json.getJSON();
	}

}

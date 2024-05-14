package GameObjects;

import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;

import GameObjects.Blocks.Block;
import GameObjects.Blocks.BlockAir;
import HelperObjects.CraftingHandler;
import HelperObjects.Hitbox;
import HelperObjects.Position;
import HelperObjects.VirtualKeyboard;
import Server.GameMaster;
import UDPServer.UDPClientConnection;
import UDPServer.UDPClientObject;

public class PlayerCharacter extends Entity implements UDPClientObject {

	private VirtualKeyboard keyboard;

	private UDPClientConnection connection;

	private boolean placeFlag = false;

	public PlayerCharacter() {
		super("player");
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
			connection.sendMessage(
					World.getBlock(Integer.parseInt(contents[1]), Integer.parseInt(contents[2])).blockString, true);
		} else if (contents[0].equals("reboot")) {
			GameMaster.restartServer();
		} else if (contents[0].equals("mouse")) {
			mouse.set(Integer.parseInt(contents[1]), Integer.parseInt(contents[2]));
		}

	}

	private int fireCooldown = 0;
	private boolean placing = false;

	@Override
	public boolean action() {
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
			if (heldBlock != null) {
				heldBlock = null;
				update = true;
			}
		}
		if (keyboard.getKey("" + MouseEvent.BUTTON1)) {
			if (fireCooldown <= 0 && heldBlock != null) {
				fireCooldown = heldBlock.getAbilityCooldown();
				heldBlock.activateAbility(this);
			} else if (heldBlock == null) {
				CraftingHandler.tryCrafting((int) (mouse.getX() + pos.getX()), (int) (mouse.getY() + pos.getY()));
			}
		}
		if (keyboard.getKey("" + KeyEvent.VK_S)) {
			if (isGrounded) {
				if (heldBlock == null && World.getBlock((int) pos.getX(), (int) (pos.getY() + 1))
						.getBreakThreshhold() <= breakCount) {
					heldBlock = World.getBlock((int) pos.getX(), (int) (pos.getY()) + 1);
					heldBlock.setPosition(-id, 0);
					World.setBlock((int) pos.getX(), (int) (pos.getY()) + 1, new BlockAir());
					breakCount = 0;
				} else if (heldBlock != null && placeFlag
						&& !World.getBlock((int) pos.getX(), (int) (pos.getY()) - 1).isBlocksMovement()) {
					placing = true;
					velocity[1] = -jumpforce;
				}
				if (heldBlock == null) {
					update = true;
					breakCount++;
				}
			} else {
				if (heldBlock != null && placeFlag
						&& !World.getBlock((int) pos.getX(), (int) (pos.getY()) + 1).isBlocksMovement()) {
					World.setBlock((int) pos.getX(), (int) (pos.getY()) + 1, heldBlock);
					heldBlock = null;
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
			World.setBlock((int) pos.getX(), (int) (pos.getY()) + 1, heldBlock);
			heldBlock = null;
			placing = false;
		}

		if (super.action() || update) {
			return true;
		}
		return false;
	}

	public UDPClientConnection getConnection() {
		return connection;
	}

	@Override
	public void remove() {
		GameMaster.removeEntity(this);
	}

	@Override
	public void setClientConnection(UDPClientConnection con) {
		connection = con;
		sendInitialData();
	}

	private void sendInitialData() {
		connection.sendMessage("id;" + id, true);
		for (Entity e : GameMaster.getEntities()) {
			if (!e.equals(this))
				connection.sendMessage("createEntity;" + e.getEntityIdentifier() + ";" + e.getId() + ";"
						+ GameMaster.decimalFormat.format(e.getPos().getX()) + ";"
						+ GameMaster.decimalFormat.format(e.getPos().getY()) + ";" + e.getHPPercentile() + ";"
						+ e.getHeldBlockId(), true);

		}
		connection.sendMessage("createWorld;" + World.getWorld().length + ";" + World.getWorld()[0].length, true);
	}

	@Override
	public void disconnected() {
		if (heldBlock != null)
			World.setBlock((int) pos.getX(), (int) pos.getY(), heldBlock);
		GameMaster.removeEntity(this);
	}

}

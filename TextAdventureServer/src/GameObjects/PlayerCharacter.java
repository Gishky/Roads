package GameObjects;

import java.awt.event.KeyEvent;

import HelperObjects.VirtualKeyboard;
import Server.GameMaster;
import UDPServer.UDPClientConnection;
import UDPServer.UDPClientObject;

public class PlayerCharacter extends Entity implements UDPClientObject {

	private VirtualKeyboard keyboard;

	private UDPClientConnection connection;

	private Block heldBlock = null;
	private boolean placeFlag = false;

	public PlayerCharacter() {
		entityIdentifier = "player";
		keyboard = new VirtualKeyboard();
		GameMaster.addEntity(this);
	}

	public void receivedMessage(String message) {
		String[] contents = message.split(";");
		if (contents[0].equals("key")) {
			keyboard.inputReceived(message);
		} else if (contents[0].equals("block")) {
			connection.sendMessage(
					World.getBlock(Integer.parseInt(contents[1]), Integer.parseInt(contents[2])).blockString, true);
		}

	}

	@Override
	public boolean action() {

		boolean update = false;

		if (keyboard.getKey("" + KeyEvent.VK_A)) {
			velocity[0] -= accelleration * (velocity[0] > 0 ? 2 : 1);
		}
		if (keyboard.getKey("" + KeyEvent.VK_D)) {
			velocity[0] += accelleration * (velocity[0] < 0 ? 2 : 1);
		}
		if (keyboard.getKey("" + KeyEvent.VK_SPACE) || keyboard.getKey("" + KeyEvent.VK_W)) {
			if (isGrounded)
				velocity[1] = -jumpforce;
		}
		if (keyboard.getKey("" + KeyEvent.VK_S)) {
			if (isGrounded) {
				if (heldBlock == null
						&& World.getBlock((int) pos.getX() / Block.size, (int) (pos.getY() + 1) / Block.size)
								.getBreakThreshhold() <= breakCount) {
					heldBlock = World.getBlock((int) pos.getX() / Block.size, (int) (pos.getY()) / Block.size + 1);
					World.setBlock((int) pos.getX() / Block.size, (int) (pos.getY()) / Block.size + 1, new BlockAir());
					breakCount = 0;
				} else if (heldBlock != null && placeFlag && !World.getBlock((int) pos.getX() / Block.size,
						(int) (pos.getY()) / Block.size - 1).blocksMovement) {
					World.setBlock((int) pos.getX() / Block.size, (int) (pos.getY()) / Block.size, heldBlock);
					heldBlock = null;
					velocity[1] = -jumpforce;
				}
				if (heldBlock == null) {
					update = true;
					breakCount++;
				}
			} else {
				if (heldBlock != null && placeFlag && !World.getBlock((int) pos.getX() / Block.size,
						(int) (pos.getY()) / Block.size + 1).blocksMovement) {
					World.setBlock((int) pos.getX() / Block.size, (int) (pos.getY()) / Block.size + 1, heldBlock);
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
						+ (int) e.getPos().getX() + ";" + (int) e.getPos().getY(), true);
		}
		connection.getServer().sendToAll("createEntity;" + getEntityIdentifier() + ";" + getId() + ";"
				+ (int) getPos().getX() + ";" + (int) getPos().getY(), true);
		connection.sendMessage("createWorld;" + World.getWorld().length + ";" + World.getWorld()[0].length, true);
	}

	@Override
	public void disconnected() {
		if (heldBlock != null)
			World.setBlock((int) pos.getX() / Block.size, (int) pos.getY() / Block.size, heldBlock);
		GameMaster.removeEntity(this);
	}

}

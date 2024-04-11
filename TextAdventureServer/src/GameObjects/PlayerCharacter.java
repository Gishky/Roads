package GameObjects;

import java.awt.event.KeyEvent;

import HelperObjects.VirtualKeyboard;
import Networking.Connection;
import Server.GameMaster;

public class PlayerCharacter extends Entity {

	private VirtualKeyboard keyboard;
	private Connection con;

	private int breakCount = 0;

	private Block heldBlock = null;

	public PlayerCharacter(Connection con) {
		this.con = con;
		entityIdentifier = "player";
		GameMaster.sendToAll("createEntity;player;" + id + ";" + (int) pos.getX() + ";" + (int) pos.getY());
		keyboard = new VirtualKeyboard();
	}

	private int aliveCounter = 1000;

	public void messageReceived(String message) {
		aliveCounter = 50;
		String[] contents = message.split(";");
		if (contents[0].equals("key")) {
			keyboard.inputReceived(message);
		} else if (contents[0].equals("ping")) {
			con.sendMessage("ping;" + contents[1]);
		} else if (contents[0].equals("block")) {
			con.sendMessage(World.getBlock(Integer.parseInt(contents[1]), Integer.parseInt(contents[2])).blockString);
		}
	}

	@Override
	public boolean action() {
		aliveCounter--;
		if (aliveCounter <= 0) {
			if (heldBlock != null)
				World.setBlock((int) pos.getX() / Block.size, (int) (pos.getY()) / Block.size, heldBlock);
			GameMaster.removeListener(con);
			GameMaster.removeEntity(this);
		}

		for (String key : keyboard.getKeys()) {
			con.sendMessage("key;" + (keyboard.getKey(key) ? "down" : "up") + ";" + key);
		}

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
				} else if (heldBlock != null && breakCount == 0 && !World.getBlock((int) pos.getX() / Block.size,
						(int) (pos.getY()) / Block.size - 1).blocksMovement) {
					World.setBlock((int) pos.getX() / Block.size, (int) (pos.getY()) / Block.size, heldBlock);
					heldBlock = null;
					velocity[1] = -jumpforce;
					update = true;
				}
				breakCount++;
			} else {
				if (heldBlock != null && breakCount == 0 && !World.getBlock((int) pos.getX() / Block.size,
						(int) (pos.getY()) / Block.size + 1).blocksMovement) {
					World.setBlock((int) pos.getX() / Block.size, (int) (pos.getY()) / Block.size + 1, heldBlock);
					heldBlock = null;
				}
			}
		} else {
			breakCount = 0;
		}
		if (update || super.action()) {
			return true;
		}
		return false;
	}

	public Connection getConnection() {
		return con;
	}

}

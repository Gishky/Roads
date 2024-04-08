package GameObjects;

import java.awt.event.KeyEvent;

import HelperObjects.VirtualKeyboard;
import Networking.ClientConnection;
import Server.GameMaster;

public class PlayerCharacter extends Entity {

	private VirtualKeyboard keyboard;
	private ClientConnection con;

	private int breakCount = 0;

	private Block heldBlock = null;

	public PlayerCharacter(ClientConnection con) {
		this.con = con;
		entityIdentifier = "player";
		GameMaster.sendToAll("createEntity;player;" + id + ";" + (int) pos.getX() + ";" + (int) pos.getY());
		keyboard = new VirtualKeyboard();
	}

	public void messageReceived(String message) {
		String[] contents = message.split(";");
		if (contents[0].equals("key")) {
			keyboard.inputReceived(message);
		} else if (contents[0].equals("ping")) {
			con.sendMessage("ping;" + contents[1]);
		}
	}

	@Override
	public String action(boolean override) {

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
					override = true;
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
		return super.action(override);
	}

}

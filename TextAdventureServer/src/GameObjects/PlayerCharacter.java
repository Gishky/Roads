package GameObjects;

import java.awt.event.KeyEvent;

import HelperObjects.VirtualKeyboard;
import Server.GameMaster;

public class PlayerCharacter extends Entity {

	private VirtualKeyboard keyboard;

	public PlayerCharacter() {
		entityIdentifier = "player";
		GameMaster.sendToAll("createEntity;player;" + id + ";" + (int)pos.getX() + ";" + (int)pos.getY());
		keyboard = new VirtualKeyboard();
		
	}

	public void messageReceived(String message) {
		String[] contents = message.split(";");
		if (contents[0].equals("key")) {
			keyboard.inputReceived(message);
		}
	}

	@Override
	public String action() {

		if (keyboard.getKey("" + KeyEvent.VK_A)) {
			velocity[0] -= accelleration;
		} 
		if (keyboard.getKey("" + KeyEvent.VK_D)) {
			velocity[0] += accelleration;
		} 
		if (keyboard.getKey("" + KeyEvent.VK_SPACE)) {
			if (isGrounded)
				velocity[1] = -jumpforce;
		}
		
		return super.action();
	}

}

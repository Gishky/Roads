package HelperObjects;

import GameObjects.PlayerCharacter;
import UDPServer.UDPClientObject;
import UDPServer.UDPClientObjectCreator;

public class PlayerCharacterCreator implements UDPClientObjectCreator{

	@Override
	public UDPClientObject newClientObject() {
		return new PlayerCharacter();
	}

}

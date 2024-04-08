package Networking;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;

import GameObjects.Block;
import GameObjects.Entity;
import GameObjects.PlayerCharacter;
import GameObjects.World;
import Server.GameMaster;

public class ClientConnection extends Thread {

	private PrintWriter out;
	private BufferedReader in;

	private PlayerCharacter p;

	public ClientConnection(PrintWriter out, BufferedReader in) {
		GameMaster.addListeners(this);
		this.out = out;
		this.in = in;
		sendMessage("" + Entity.getNextID());
		for (Entity e : GameMaster.getEntities()) {
			sendMessage("createEntity;" + e.getEntityIdentifier() + ";" + e.getId() + ";" + (int) e.getPos().getX()
					+ ";" + (int) e.getPos().getY());
		}
		p = new PlayerCharacter(this);
		GameMaster.addEntity(p);

		sendMessage("createWorld;" + World.getWorld().length + ";" + World.getWorld()[0].length);

		Block[][] world = World.getWorld();
		for (int x = 0; x < world.length; x++) {
			String worldString = "";
			for (int y = 0; y < world[0].length; y++) {
				worldString += world[x][y].blockString + "\n";
			}
			sendMessage(worldString);
		}
	}

	public void run() {
		String inputLine;
		try {
			while ((inputLine = in.readLine()) != null) {
				// System.out.println(inputLine);
				p.messageReceived(inputLine);
			}
		} catch (IOException e) {
			try {
				in.close();
			} catch (IOException e1) {
			}
			out.close();
			GameMaster.removeEntity(p);
			GameMaster.removeListener(this);
		}
	}

	public void sendMessage(String message) {
		out.println(message);
	}

	public void setWriter(PrintWriter out) {
		this.out = out;
	}

	public void setReader(BufferedReader in) {
		this.in = in;
	}
}

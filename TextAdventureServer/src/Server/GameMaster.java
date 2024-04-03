package Server;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.Timer;

import GameObjects.Entity;
import GameObjects.World;
import Networking.ClientConnection;

public class GameMaster implements ActionListener{
	
	private static GameMaster master;
	
	private ArrayList<ClientConnection> clients;
	
	private ArrayList<Entity> entities;
	
	public GameMaster() {
		new Timer(50,this).start();
		entities = new ArrayList<Entity>();
		clients = new ArrayList<ClientConnection>();
		World.generateWorld();
		
		if(master == null) {
			master = this;
		}
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		for(Entity entity : entities) {
			entity.action();
		}
	}

	public static void addEntity(Entity e) {
		master.entities.add(e);
	}

	public static void removeEntity(Entity e) {
		sendToAll("removeEntity;"+e.getId());
		master.entities.remove(e);
	}

	public static void addListeners(ClientConnection clientConnection) {
		master.clients.add(clientConnection);
	}

	public static void removeListener(ClientConnection clientConnection) {
		master.clients.remove(clientConnection);
	}

	public static void entityPositionUpdate(Entity e) {
		//System.out.println("updateing");
		sendToAll("entity;"+e.getId()+";"+(int)e.getPos().getX()+";"+(int)e.getPos().getY());
	}
	
	public static void sendToAll(String message) {
		for(ClientConnection client : master.clients){
			client.sendMessage(message);
		}
	}
	
}

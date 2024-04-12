package Window;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;

import javax.swing.JPanel;
import javax.swing.Timer;

import GameObjects.Entity;
import GameObjects.World;
import HelperObjects.MessageInterpreter;
import Networking.UDPServerConnection;

public class Panel extends JPanel implements ActionListener, KeyListener {

	private Timer t = new Timer(50, this);
	private static UDPServerConnection connection;

	public static int windowWidth, windowHeight;

	private static ArrayList<Entity> entities;

	public Panel() {
		this.addKeyListener(this);
		this.setFocusable(true);

		entities = new ArrayList<Entity>();

		t.start();
		connection = new UDPServerConnection("localhost", 61852, new MessageInterpreter());
	}

	@Override
	public void actionPerformed(ActionEvent arg0) {
		repaint();
	}

	@Override
	public void paintComponent(Graphics gr) {
		windowWidth = getWidth();
		windowHeight = getHeight();
		Graphics2D g = (Graphics2D) gr;
		g.clearRect(0, 0, getWidth(), getHeight());

		int cameraX = World.cameraX;
		int cameraY = World.cameraY;

		World.draw(g, cameraX, cameraY);

		for (Entity e : entities) {
			e.draw(g, cameraX, cameraY);
		}

		g.setFont(new Font("Arial", Font.PLAIN, 15));
		g.setColor(Color.green);
		g.drawString("" + connection.getPing(), 2, 15);
		g.setColor(Color.orange);
		g.drawString(connection.getPackagePercentile() + "%", 2, 30);
	}

	@Override
	public void keyTyped(KeyEvent e) {
		// TODO Auto-generated method stub

	}

	private ArrayList<Integer> pressedKeys = new ArrayList<Integer>();

	@Override
	public void keyPressed(KeyEvent e) {
		int key = e.getKeyCode();
		if (!pressedKeys.contains(key)) {
			connection.sendMessage("key;down;" + key, true);
			pressedKeys.add(key);
		}
	}

	@Override
	public void keyReleased(KeyEvent e) {
		int key = e.getKeyCode();
		connection.sendMessage("key;up;" + key, true);
		pressedKeys.remove(pressedKeys.indexOf(key));
	}

	public static ArrayList<Entity> getEntities() {
		return entities;
	}

	public static UDPServerConnection getServerConnection() {
		return connection;
	}
}

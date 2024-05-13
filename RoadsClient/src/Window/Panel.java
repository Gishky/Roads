package Window;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.ArrayList;
import java.util.LinkedList;

import javax.swing.JPanel;
import javax.swing.Timer;

import GameObjects.Entity;
import GameObjects.World;
import HelperObjects.MessageInterpreter;
import HelperObjects.Particle;
import UDPClient.UDPServerConnection;

public class Panel extends JPanel implements ActionListener, KeyListener, MouseMotionListener, MouseListener {

	private Timer t = new Timer(50, this);
	private static UDPServerConnection connection;

	public static int windowWidth, windowHeight;

	private static LinkedList<Entity> entities;
	private static LinkedList<Integer> removedEntityIDs = new LinkedList<Integer>();
	private static LinkedList<Particle> particles = new LinkedList<Particle>();

	public Panel() {
		this.addKeyListener(this);
		this.setFocusable(true);
		addMouseListener(this);
		addMouseMotionListener(this);

		entities = new LinkedList<Entity>();

		t.start();

		connection = new UDPServerConnection("localhost", 61852, new MessageInterpreter());
	}

	@Override
	public void actionPerformed(ActionEvent arg0) {
		if (!mousePositionUpdate.equals("")) {
			connection.sendMessage(mousePositionUpdate, false);
		}
		repaint();
	}

	long timestamp = 0;
	@Override
	public void paintComponent(Graphics gr) {
		windowWidth = getWidth();
		windowHeight = getHeight();
		Graphics2D g = (Graphics2D) gr;
		g.clearRect(0, 0, getWidth(), getHeight());

		int cameraX = World.cameraX;
		int cameraY = World.cameraY;

		World.draw(g, cameraX, cameraY);

		for (int i = 0; i < entities.size(); i++) {
			Entity e = entities.get(i);
			e.draw(g, cameraX, cameraY);
		}

		ArrayList<Particle> toRemove = new ArrayList<Particle>();
		for (int i = 0; i < particles.size(); i++) {
			Particle p = particles.get(i);
			if (p.draw(g, cameraX, cameraY)) {
				toRemove.add(p);
			}
		}
		for (Particle p : toRemove) {
			particles.remove(p);
		}

		g.setFont(new Font("Arial", Font.PLAIN, 15));
		g.setColor(Color.green);
		g.drawString("" + connection.getPing(), 2, 15);
		g.setColor(Color.orange);
		g.drawString(connection.getPackagePercentile() + "%", 2, 30);
	}

	private String typedString = "";

	@Override
	public void keyTyped(KeyEvent e) {
		typedString += e.getKeyChar();
		if (typedString.endsWith("sudo reboot\n")) {
			connection.sendMessage("reboot", true);
		}
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

	public static LinkedList<Entity> getEntities() {
		return entities;
	}

	public static void addParticle(Particle p) {
		particles.add(p);
	}

	public static UDPServerConnection getServerConnection() {
		return connection;
	}

	public static void removeEntity(Entity e) {
		if (entities.contains(e)) {
			entities.remove(e);
		} else {
			removedEntityIDs.add(e.getId());
		}
	}

	public static LinkedList<Integer> getRemovedEntityIDs() {
		return removedEntityIDs;
	}

	public static boolean existsEntityID(String id) {
		for (int i = 0; i < entities.size(); i++) {
			if ((entities.get(i).getId() + "").equals(id)) {
				return true;
			}
		}
		return false;
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mousePressed(MouseEvent e) {
		int key = e.getButton();
		if (!pressedKeys.contains(key)) {
			connection.sendMessage("key;down;" + key, true);
			pressedKeys.add(key);
		}
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		int key = e.getButton();
		connection.sendMessage("key;up;" + key, true);
		pressedKeys.remove(pressedKeys.indexOf(key));
	}

	@Override
	public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	private String mousePositionUpdate = "";

	@Override
	public void mouseDragged(MouseEvent e) {
		mousePositionUpdate = "mouse;" + (e.getX() - windowWidth / 2) + ";" + (e.getY() - windowHeight / 2);
	}

	@Override
	public void mouseMoved(MouseEvent e) {
		mousePositionUpdate = "mouse;" + (e.getX() - windowWidth / 2) + ";" + (e.getY() - windowHeight / 2);
	}
}

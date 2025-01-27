package Window;

import java.awt.BasicStroke;
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
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.util.ArrayList;

import javax.swing.JPanel;
import javax.swing.Timer;

import GameObjects.Entity;
import GameObjects.World;
import GameObjects.Blocks.Block;
import HelperObjects.MessageInterpreter;
import HelperObjects.Particle;
import UDPClient.UDPServerConnection;

@SuppressWarnings("serial")
public class Panel extends JPanel
		implements ActionListener, KeyListener, MouseMotionListener, MouseListener, MouseWheelListener {

	private Timer t = new Timer(50, this);
	private static UDPServerConnection connection;
	private String version = "v1.8";

	public static int windowWidth, windowHeight;

	private static ArrayList<Entity> entities;
	private static ArrayList<Integer> removedEntityIDs = new ArrayList<Integer>();
	private static ArrayList<Particle> particles = new ArrayList<Particle>();

	public Panel() {
		this.addKeyListener(this);
		this.setFocusable(true);
		addMouseListener(this);
		addMouseMotionListener(this);
		addMouseWheelListener(this);

		entities = new ArrayList<Entity>();

		connection = new UDPServerConnection("gishky.duckdns.org", 61852, new MessageInterpreter());
		if (connection.startConnection(version)) {
			String username = System.getProperty("user.name");
			connection.sendMessage("username;" + username, true);
			t.start();
		}
	}

	@Override
	public void actionPerformed(ActionEvent arg0) {
		if (!mousePositionUpdate.equals("")) {
			connection.sendMessage(mousePositionUpdate, false);
		}
		repaint();
	}

	private long timestamp = 0;
	private boolean statistics = false;

	@Override
	public void paintComponent(Graphics gr) {
		windowWidth = getWidth();
		windowHeight = getHeight();
		Graphics2D g = (Graphics2D) gr;
		g.clearRect(0, 0, getWidth(), getHeight());

		int cameraX = (int) (World.cameraX * Block.size);
		int cameraY = (int) (World.cameraY * Block.size);

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

		drawInventory(g);

		if (!statistics)
			return;
		g.setFont(new Font("Arial", Font.PLAIN, 15));
		g.setColor(Color.green);
		g.drawString("" + connection.getPing(), 2, 15);
		g.setColor(Color.orange);
		g.drawString(connection.getPackagePercentile() + "%", 2, 30);
		g.setColor(Color.orange);
		if ((System.currentTimeMillis() - timestamp) > 0)
			g.drawString((1000 / (System.currentTimeMillis() - timestamp)) + " FPS", 2, getHeight() - 7);
		g.drawString(serverTickRate + "ms", 2, getHeight() - 22);
		g.setColor(Color.black);
		g.drawString(cameraX / Block.size + "/" + cameraY / Block.size,
				windowWidth - 2 - (g.getFontMetrics().stringWidth(cameraX / Block.size + "/" + cameraY / Block.size)),
				15);
		timestamp = System.currentTimeMillis();
	}

	private void drawInventory(Graphics2D g) {
		g.setColor(Color.gray.brighter());
		g.fillRect((int) (windowWidth - Block.size * 2.4), (int) (windowHeight / 2 - Block.size * 5.6),
				(int) (Block.size * 2.4), (int) (Block.size * 11.2));

		g.setColor(Color.gray.brighter().brighter());
		g.drawRect((int) (windowWidth - Block.size * 2.4), (int) (windowHeight / 2 - Block.size * 5.6),
				(int) (Block.size * 2.4), (int) (Block.size * 11.2));

		for (int i = 0; i < World.playerInventory.length; i++) {
			g.setColor(Color.gray);
			g.fillRect((int) (windowWidth - Block.size * 2.2),
					(int) (windowHeight / 2 - Block.size * 5.4 + Block.size * 2.2 * i), Block.size * 2, Block.size * 2);
			g.setColor(Color.gray.brighter().brighter());
			g.drawRect((int) (windowWidth - Block.size * 2.2),
					(int) (windowHeight / 2 - Block.size * 5.4 + Block.size * 2.2 * i), Block.size * 2, Block.size * 2);

			Block b = World.playerInventory[i];
			if (b != null) {
				b.drawInventory(g, (int) (windowWidth - Block.size * 2),
						(int) (windowHeight / 2 - Block.size * 5.2 + Block.size * 2.2 * i), (int) (Block.size * 1.6),
						i == World.selectedInventory, i);
			}

			if (World.selectedInventory == i) {
				g.setColor(Color.yellow.brighter());
				g.setStroke(new BasicStroke(3));
				g.drawRect((int) (windowWidth - Block.size * 2.2),
						(int) (windowHeight / 2 - Block.size * 5.4 + Block.size * 2.2 * i), Block.size * 2,
						Block.size * 2);
				g.setStroke(new BasicStroke(1));
			}
		}
	}

	@Override
	public void keyTyped(KeyEvent e) {
	}

	private ArrayList<Integer> pressedKeys = new ArrayList<Integer>();

	@Override
	public void keyPressed(KeyEvent e) {
		if (e.getKeyCode() == KeyEvent.VK_F1) {
			statistics = !statistics;
		}

		synchronized (pressedKeys) {
			int key = e.getKeyCode();
			if (!pressedKeys.contains(key)) {
				connection.sendMessage("key;down;" + key, true);
				pressedKeys.add(key);
			}
		}
	}

	@Override
	public void keyReleased(KeyEvent e) {
		int key = e.getKeyCode();
		connection.sendMessage("key;up;" + key, true);
		synchronized (pressedKeys) {
			if (pressedKeys.contains(key))
				pressedKeys.remove(pressedKeys.indexOf(key));
		}
	}

	public static ArrayList<Entity> getEntities() {
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

	public static ArrayList<Integer> getRemovedEntityIDs() {
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

	private static int serverTickRate = 0;

	public static void setServerTickRate(int tickrate) {
		serverTickRate = tickrate;
	}

	@Override
	public void mouseClicked(MouseEvent e) {

	}

	@Override
	public void mousePressed(MouseEvent e) {
		synchronized (pressedKeys) {
			int key = e.getButton();
			if (!pressedKeys.contains(key)) {
				connection.sendMessage("key;down;" + key, true);
				pressedKeys.add(key);
			}
		}
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		synchronized (pressedKeys) {
			int key = e.getButton();
			connection.sendMessage("key;up;" + key, true);
			pressedKeys.remove(pressedKeys.indexOf(key));
		}
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
		mousePositionUpdate = "mouse;" + (e.getX() - (double) windowWidth / 2) / Block.size + ";"
				+ (e.getY() - (double) windowHeight / 2) / Block.size;
	}

	@Override
	public void mouseMoved(MouseEvent e) {
		mousePositionUpdate = "mouse;" + (e.getX() - (double) windowWidth / 2) / Block.size + ";"
				+ (e.getY() - (double) windowHeight / 2) / Block.size;
	}

	@Override
	public void mouseWheelMoved(MouseWheelEvent e) {
		if (pressedKeys.contains(17)) {
			Block.size -= e.getWheelRotation() * 10;
			if (Block.size <= 10)
				Block.size = 10;
			if (Block.size >= 100)
				Block.size = 100;
			return;
		}
		World.wantedSelectedInventory += e.getWheelRotation();
		World.wantedSelectedInventory = Math.min(4, Math.max(0, World.wantedSelectedInventory));
		if (World.wantedSelectedInventory != World.selectedInventory)
			connection.sendMessage("scroll;" + World.wantedSelectedInventory, true);

	}
}

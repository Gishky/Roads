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

import javax.print.attribute.HashAttributeSet;
import javax.swing.JPanel;
import javax.swing.Timer;

import GameObjects.Entity;
import GameObjects.World;
import HelperObjects.VirtualKeyboard;
import Networking.ConnectionEvaluator;
import Networking.ServerConnection;

public class Panel extends JPanel implements ActionListener, KeyListener {

	private Timer t = new Timer(50, this);
	private static ServerConnection connection;

	public static int windowWidth, windowHeight;

	private static ArrayList<Entity> entities;
	private static VirtualKeyboard actualKeyboard = new VirtualKeyboard();
	private static VirtualKeyboard serverKeyboard = new VirtualKeyboard();

	public Panel() {
		this.addKeyListener(this);
		this.setFocusable(true);
		t.start();
		connection = new ServerConnection();

		entities = new ArrayList<Entity>();
	}

	private int pingCounter = 0;

	@Override
	public void actionPerformed(ActionEvent arg0) {
		repaint();

		for (String key : actualKeyboard.getKeys()) {
			boolean isPressed = actualKeyboard.getKey(key);
			if (serverKeyboard.getKey(key) != actualKeyboard.getKey(key)) {
				connection.sendMessage("key;" + (isPressed ? "down" : "up") + ";" + key);
			}
		}

		pingCounter--;
		if (pingCounter <= 0) {
			pingCounter = 10;
			long milis = System.currentTimeMillis();
			ConnectionEvaluator.addPackage(milis);
			connection.sendMessage("ping;" + milis);
		}
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
		g.drawString("" + ConnectionEvaluator.getPing(), 2, 15);
		g.setColor(Color.orange);
		g.drawString(ConnectionEvaluator.getPackagePercentile() + "%", 2, 30);
	}

	@Override
	public void keyTyped(KeyEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void keyPressed(KeyEvent e) {
		connection.sendMessage("key;down;" + e.getKeyCode());
		actualKeyboard.inputReceived("key;down;" + e.getKeyCode());
	}

	@Override
	public void keyReleased(KeyEvent e) {
		connection.sendMessage("key;up;" + e.getKeyCode());
		actualKeyboard.inputReceived("key;up;" + e.getKeyCode());
	}

	public static ArrayList<Entity> getEntities() {
		return entities;
	}

	public static ServerConnection getServerConnection() {
		return connection;
	}

	public static VirtualKeyboard getServerKeyboard() {
		return serverKeyboard;
	}
}

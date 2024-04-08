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
import Networking.ServerConnection;

public class Panel extends JPanel implements ActionListener, KeyListener {

	private Timer t = new Timer(50, this);
	private ServerConnection connection;

	public static int windowWidth, windowHeight;

	private static ArrayList<Entity> entities;

	public Panel() {
		this.addKeyListener(this);
		this.setFocusable(true);
		t.start();
		connection = new ServerConnection();

		entities = new ArrayList<Entity>();
	}

	@Override
	public void actionPerformed(ActionEvent arg0) {
		repaint();
		connection.sendMessage("ping;" + System.currentTimeMillis());
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

		float avg = 0;
		for (float p : ServerConnection.ping) {
			avg += p;
		}
		avg /= ServerConnection.ping.size();
		g.setColor(Color.green);
		g.setFont(new Font("Arial", Font.PLAIN, 15));
		g.drawString("" + (int) avg, 2, 15);
	}

	@Override
	public void keyTyped(KeyEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void keyPressed(KeyEvent e) {
		connection.sendMessage("key;down;" + e.getKeyCode());
	}

	@Override
	public void keyReleased(KeyEvent e) {
		connection.sendMessage("key;up;" + e.getKeyCode());
	}

	public static ArrayList<Entity> getEntities() {
		return entities;
	}
}

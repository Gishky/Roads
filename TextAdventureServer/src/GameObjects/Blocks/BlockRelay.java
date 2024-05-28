package GameObjects.Blocks;

import java.util.ArrayList;
import java.util.LinkedList;

import AdminConsole.AdminConsole;
import GameObjects.World;
import GameObjects.Entities.PlayerCharacter;
import HelperObjects.JSONObject;
import HelperObjects.Position;
import Server.GameMaster;

public class BlockRelay extends Block {

	private ArrayList<Position> activationList = new ArrayList<Position>();
	private int activationSize = 5;

	public BlockRelay() {
		id = 12;
		friction = 2;

		breakable = true;
		breakThreshhold = 1;
	}

	public BlockRelay(JSONObject block) {
		id = 12;
		friction = 2;

		breakable = true;
		breakThreshhold = 1;

		if (block == null)
			return;

		if (block.get("x") != null)
			setX(Integer.parseInt(block.get("x")));
		if (block.get("y") != null)
			setY(Integer.parseInt(block.get("y")));
	}

	public void activateAbility(PlayerCharacter e) {
		Position pos = new Position((int) (e.getMousePosition().getX() + e.getPos().getX()),
				(int) (e.getMousePosition().getY() + e.getPos().getY()));
		for (Position p : activationList)
			if (p.getX() == pos.getX() && p.getY() == pos.getY())
				return;

		activationList.add(pos);
		if (activationList.size() > activationSize)
			activationList.clear();

		e.updateInventory();
	}

	@Override
	public int getAbilityCooldown() {
		return 2;
	}

	public void activate(ArrayList<Block> activationchain) {
		if (activationchain.contains(this))
			return;
		activationchain.add(this);

		ArrayList<Block> blockList = new ArrayList<Block>();
		for (int i = 0; i < activationList.size(); i++) {
			Position p = activationList.get(i);
			if (!blockList.contains(World.getBlock((int) p.getX(), (int) p.getY())))
				blockList.add(World.getBlock((int) p.getX(), (int) p.getY()));
		}
		for (int i = 0; i < blockList.size(); i++)
			blockList.get(i).activate(activationchain);

		GameMaster.sendToAll("{action:activate,x:" + x + ",y:" + y + "}", true);
	}

	public String toJSON() {
		JSONObject json = new JSONObject();
		json.put("id", "" + id);
		json.put("x", "" + getX());
		json.put("y", "" + getY());
		for (int i = 0; i < activationList.size(); i++) {
			json.put("" + i, (int) (activationList.get(i).getX()) + "/" + (int) (activationList.get(i).getY()));
		}
		return json.getJSON();
	}

	public Block clone() {
		return new BlockRelay();
	}
}

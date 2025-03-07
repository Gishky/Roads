package GameObjects.Blocks;

import java.util.ArrayList;

import GameObjects.World;
import GameObjects.Entities.PlayerCharacter;
import HelperObjects.JSONObject;
import Server.GameMaster;

public class BlockMachine extends Block {

	private int dirx = 0;
	private int diry = -1;

	public BlockMachine() {
		id = 14;
		friction = 2;

		breakable = true;
		breakThreshhold = 5;
	}

	public BlockMachine(JSONObject block) {
		id = 14;
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
		if (!canAbilityActivate())
			return;

		if (Math.abs(e.getMousePosition().getX()) > Math.abs(e.getMousePosition().getY())) {
			dirx = (e.getMousePosition().getX() > 0 ? 1 : -1);
			diry = 0;
		} else {
			diry = (e.getMousePosition().getY() > 0 ? 1 : -1);
			dirx = 0;
		}
		if (World.getBlock((int) e.getPos().getX() + dirx, (int) e.getPos().getY() + diry).isBlocksMovement())
			if (pushBlock((int) e.getPos().getX() + dirx, (int) e.getPos().getY() + diry, 100)) {
				World.setBlock((int) e.getPos().getX() + dirx, (int) e.getPos().getY() + diry, new BlockAir());
			}
		e.updateInventory();
	}

	public Block clone() {
		return new BlockMachine();
	}

	private boolean push = false;

	public void activate(ArrayList<Block> activationchain) {
		if (activationchain.contains(this))
			return;
		activationchain.add(this);

		push = true;
		scheduleUpdate();
		GameMaster.sendToAll("{action:activate,x:" + x + ",y:" + y + "}", true);
	}

	protected int getAbilityCooldown() {
		return 1000;
	}

	@Override
	public void update() {
		if (!push)
			return;

		push = false;
		if (World.getBlock(x + dirx, y + diry).isBlocksMovement())
			if (pushBlock(x + dirx, y + diry, 100)) {
				World.setBlock(x + dirx, y + diry, new BlockAir());
			}
	}

	private boolean pushBlock(int x, int y, int force) {
		if (!World.getBlock(x + dirx, y + diry).isBlocksMovement()) {
			World.setBlock(x + dirx, y + diry, World.getBlock(x, y));
			return true;
		} else {
			if (force >= World.getBlock(x, y).breakThreshhold)
				if (pushBlock(x + dirx, y + diry, force - World.getBlock(x, y).breakThreshhold)) {
					World.setBlock(x + dirx, y + diry, World.getBlock(x, y));
					return true;
				}
		}

		return false;
	}

	public String toJSON() {
		JSONObject json = new JSONObject();
		json.put("id", "" + id);
		json.put("x", "" + getX());
		json.put("y", "" + getY());
		json.put("dir", "" + (dirx != 0 ? dirx + 2 : 0 + diry != 0 ? diry + 3 : 0));
		return json.getJSON();
	}
}

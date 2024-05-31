package GameObjects.Blocks;

import java.util.LinkedList;

import AdminConsole.AdminConsole;
import AdminConsole.MessageInterpreter;
import GameObjects.World;
import GameObjects.Entities.PlayerCharacter;
import HelperObjects.JSONObject;

public class BlockChest extends Block {

	public BlockChest() {
		id = 17;
		friction = 2;

		breakable = true;
		breakThreshhold = 5;
		
		fuelValue = 180;

		inventory = new LinkedList<Block>();
	}

	public BlockChest(JSONObject block) {
		id = 17;
		friction = 2;

		breakable = true;
		breakThreshhold = 5;

		fuelValue = 180;
		
		inventory = new LinkedList<Block>();

		if (block == null)
			return;

		if (block.get("x") != null)
			setX(Integer.parseInt(block.get("x")));
		if (block.get("y") != null)
			setY(Integer.parseInt(block.get("y")));
		int blockid = 0;
		while (block.get("" + blockid) != null) {
			JSONObject inventoryblock = new JSONObject(block.get("" + blockid));
			inventory.add(blockid,
					MessageInterpreter.getBlockFromIdentifier(inventoryblock.get("" + id), inventoryblock));
			blockid++;
		}
	}

	public Block clone() {
		return new BlockChest();
	}

	@Override
	public void inventoryUpdate(PlayerCharacter e) {
		if (this.inventory.size() == 0)
			return;
		for (int i = 0; i < 5; i++) {
			Block b = e.getInventory(i);
			if (b.getId() != this.inventory.getFirst().getId())
				continue;
			this.inventory.addLast(b);
			e.setInventoryBlock(null, i);
		}
	}

	public void update() {
		if (inventory.size() != 0 && World.getBlock(x, y - 1).getId() != inventory.getFirst().getId())
			return;
		if (World.getBlock(x, y - 1).isBreakable()) {
			inventory.addLast(World.getBlock(x, y - 1));
			World.setBlock(x, y - 1, new BlockAir());
			World.setBlock(x, y, this);
		}
	}

	public void activateAbility(PlayerCharacter e) {
		if (inventory.size() == 0)
			return;

		int dirx = 0;
		int diry = 0;

		if (Math.abs(e.getMousePosition().getX()) > Math.abs(e.getMousePosition().getY())) {
			dirx = (e.getMousePosition().getX() > 0 ? 1 : -1);
			diry = 0;
		} else {
			diry = (e.getMousePosition().getY() > 0 ? 1 : -1);
			dirx = 0;
		}

		if (!World.getBlock((int) e.getPos().getX() + dirx, (int) e.getPos().getY() + diry).isBlocksMovement()) {
			World.setBlock((int) e.getPos().getX() + dirx, (int) e.getPos().getY() + diry, inventory.pop());
		}

		e.updateInventory();
	}

	public int getAbilityCooldown() {
		return 2;
	}

	@Override
	public int getFuelValue() {
		if (inventory.size() == 0)
			return super.getFuelValue();
		return 0;
	}

	public String toJSON() {
		JSONObject json = new JSONObject();
		json.put("id", "" + id);
		json.put("x", "" + getX());
		json.put("y", "" + getY());
		json.put("inventorysize", "" + inventory.size());
		if (inventory.size() > 0) {
			json.put("inventory", "" + inventory.getFirst().getId());
		}
		json.put("fuel", "" + fuelValue);
		return json.getJSON();
	}
}

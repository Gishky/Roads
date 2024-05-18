package GameObjects.Blocks;

import HelperObjects.JSONObject;

public class BlockGoldOre extends Block {
	public BlockGoldOre() {
		id = 8;
		friction = 1.5;

		breakable = true;
		breakThreshhold = 24;
		
		smeltedBlock = new BlockGold();
		requiredFuelForSmelting = 300;
	}
	
	public String toJSON() {
		JSONObject json = new JSONObject();
		json.put("id", "" + id);
		json.put("x", "" + getX());
		json.put("y", "" + getY());
		json.put("smelt", "" + requiredFuelForSmelting);
		return json.getJSON();
	}

	public Block clone() {
		return new BlockGoldOre();
	}
}

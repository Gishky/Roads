package GameObjects.Entities;

import HelperObjects.JSONObject;
import HelperObjects.Position;
import Server.GameMaster;

public class OvenAbilityJet extends Entity {

	private int lifetime = 0;

	public OvenAbilityJet(int entityid, double[] velocity, double boostPower) {
		this.pos = new Position(entityid, entityid);
		this.velocity = velocity;
		lifetime = (int) (5 * boostPower);

		createEntity();
		GameMaster.removeEntity(this, true);
	}

	public String toJSON() {
		JSONObject json = new JSONObject();
		json.put("type", "ovenAbility");
		json.put("id", "" + id);
		json.put("x", String.format("%.4f", pos.getX()));
		json.put("y", String.format("%.4f", pos.getY()));
		json.put("velx", String.format("%.4f", velocity[0]));
		json.put("vely", String.format("%.4f", velocity[1]));
		json.put("lifetime", "" + lifetime);
		return json.getJSON();
	}
}

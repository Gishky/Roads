package GameObjects;

import GameObjects.Blocks.BlockOven;
import HelperObjects.JSONObject;
import HelperObjects.Position;
import Server.GameMaster;

public class OvenAbilityJet extends Entity {

	private BlockOven oven;

	public OvenAbilityJet(Position initialPosition, double initialVelocityx, double initialVelocityy, BlockOven oven) {
		super( initialPosition);
		parameters = -initialVelocityx + "/" + -initialVelocityy;

		this.oven = oven;
	}

	private int lifetime = 5;

	@Override
	public boolean action() {
		lifetime--;
		if (lifetime < 0) {
			GameMaster.removeEntity(this);
			oven.setJet(null);
		}
		return true;
	}

	public void setVelocity(double velocityx, double velocityy) {
		parameters = -velocityx + "/" + -velocityy;
	}

	public void reactivate() {
		lifetime = 5;
	}
	
	public String toJSON() {
		JSONObject json = new JSONObject();
		json.put("type", "ovenAbility");
		json.put("id", "" + id);
		json.put("x", String.format("%.4f", pos.getX()));
		json.put("y", String.format("%.4f", pos.getY()));
		json.put("velx", String.format("%.4f", velocity[0]));
		json.put("vely", String.format("%.4f", velocity[1]));
		return json.getJSON();
	}
}

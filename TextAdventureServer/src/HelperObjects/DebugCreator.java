package HelperObjects;

import Server.GameMaster;

public class DebugCreator {
	public static void createDebugLine(double xFrom, double yFrom, double xTo, double yTo, int lifeTime, int red,
			int green, int blue) {
		JSONObject json = new JSONObject();
		json.put("action", "addDebug");
		json.put("type", "line");
		json.put("xFrom", "" + xFrom);
		json.put("yFrom", "" + yFrom);
		json.put("xTo", "" + xTo);
		json.put("yTo", "" + yTo);
		json.put("lifeTime", "" + lifeTime);
		json.put("red", "" + red);
		json.put("green", "" + green);
		json.put("blue", "" + blue);
		GameMaster.sendToAll(json.getJSON(), true);
	}
	public static void createDebugText(double x, double y, String text, int lifeTime, int red,
			int green, int blue) {
		JSONObject json = new JSONObject();
		json.put("action", "addDebug");
		json.put("type", "text");
		json.put("x", "" + x);
		json.put("y", "" + y);
		json.put("text", "" + text);
		json.put("lifeTime", "" + lifeTime);
		json.put("red", "" + red);
		json.put("green", "" + green);
		json.put("blue", "" + blue);
		GameMaster.sendToAll(json.getJSON(), true);
	}
}

package HelperObjects;

import java.util.HashMap;
import java.util.Map.Entry;

public class JSONObject {

	private HashMap<String, String> map = new HashMap<String, String>();

	public void put(String name, String value) {
		map.put(name, value);
	}

	public void put(String name, JSONObject value) {
		map.put(name, value.getJSON());
	}

	public String getJSON() {
		String json = "{";
		for (Entry<String, String> s : map.entrySet()) {
			json += "" + s.getKey() + ":" + s.getValue() + ",";
		}
		json = json.substring(0, json.length() - 1);
		json += "}";
		return json;
	}
}

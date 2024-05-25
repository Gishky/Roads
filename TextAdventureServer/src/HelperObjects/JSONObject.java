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

	public String get(String key) {
		if (map.containsKey(key))
			return map.get(key);
		return null;
	}

	public JSONObject() {

	}

	public JSONObject(String json) {
		fromJSON(json);
	}

	public void fromJSON(String json) {
		json = json.substring(1, json.length() - 1);
		int openCount = 0;
		for (int i = 0; i < json.length(); i++) {
			if (json.charAt(i) == ',') {
				if (openCount == 0) {
					putMap(json.substring(0, i));
					json = json.substring(i + 1);
					i = 0;
				}
			} else if (json.charAt(i) == '{') {
				openCount++;
			} else if (json.charAt(i) == '}') {
				openCount--;
			}
		}
		putMap(json);
	}

	private void putMap(String substring) {
		// System.out.println(substring);
		String[] pair = substring.split(":");
		map.put(pair[0], substring.substring(pair[0].length() + 1));
	}

}

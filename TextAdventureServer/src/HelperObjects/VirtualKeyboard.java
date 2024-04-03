package HelperObjects;

import java.util.HashMap;

public class VirtualKeyboard {
	private HashMap<String,Boolean> keyboard;
	
	public VirtualKeyboard() {
		keyboard = new HashMap<String,Boolean>();
	}
	
	public void inputReceived(String input) {
		String[] inputs = input.split(";");
		
		boolean value = inputs[1].equals("down");
		setKey(inputs[2], value);
	}
	
	private void setKey(String key, boolean value) {
		keyboard.put(key, value);
		
		//System.out.println(key+": "+value);
		
		/*
		for (Entry<String, Boolean> entry : keyboard.entrySet()) {
		     String key1 = entry.getKey();
		     Boolean value1 = entry.getValue();
		     System.out.println("Key=" + key1 + ", Value=" + value1);
		}*/
	}
	
	public boolean getKey(String key) {
		if(keyboard.get(key)==null)
			return false;
		return keyboard.get(key);
	}
}

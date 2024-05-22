package AdminConsole;

import HelperObjects.JSONObject;

public class MessageInterpreter {

	public static String receivedMessage(String receivedMessage) {
		if (!receivedMessage.contains("}")) {
			return "Error";
		}
		JSONObject message = new JSONObject(receivedMessage);

		switch (message.get("action")) {
		
		default:
			return "unknown command: " + receivedMessage;
		}
	}
}

package UDPServer;

public interface UDPClientObject {

	public void setClientConnection(UDPClientConnection con);

	public void receivedMessage(String message);

	public void disconnected();

	public void remove();
}

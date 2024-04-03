package Networking;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;

public class ClientConnection extends Thread {
    private PrintWriter out;
    private BufferedReader in;
    
    public void run(){
    	String inputLine;
    	try {
        	inputLine = in.readLine();
			while((inputLine = in.readLine()) != null) {
				
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
    
    public void sendMessage(String message) {
    	out.println(message);
    }

    public void setWriter(PrintWriter out) {
        this.out = out;
    }

    public void setReader(BufferedReader in) {
        this.in = in;
    }
}

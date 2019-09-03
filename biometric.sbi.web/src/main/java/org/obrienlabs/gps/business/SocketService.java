package org.obrienlabs.gps.business;

//import javax.websocket.OnMessage;
//import javax.websocket.server.ServerEndpoint;

//@ServerEndpoint("/echo")
public class SocketService {
	private String returnMessage = "I got this message '%s' and am sending it back in reverse '%s'";
	
	//@OnMessage
	public String echo(String message) {
		System.out.println("_websocket: " + message);
		return String.format(returnMessage,
				message,
				new StringBuilder(message).reverse());
	}
}

package MultiThreadedServer;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class ClientThread extends Thread{
	
	private Socket socket = null;
	
	public ClientThread(Socket socket){
		this.socket = socket;
	}
	public void run(){
		try {
					// Sending a single message to the client
					PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
					out.println("Hello Client");
					
					// Reading data from the client
					BufferedReader input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
					String clientInput = input.readLine();
					System.out.println(clientInput);
					
					input.close();
					out.close();
					socket.close();
			} catch (IOException e) {
				System.out.println("Port is already being used or blocked");
			}
	}
}

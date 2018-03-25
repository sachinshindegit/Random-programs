package MultiThreadedServer;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class MultiThreadedServer {

	public static void main(String[] args) {
		int portNumber = 9091; // Port number of '0' means port is assigned automatically
		try {
			
			ServerSocket serverSocket = new ServerSocket(portNumber);
			boolean stop = false;
			
			// Here we are looping for new client connection
			while(!stop){
				System.out.println("Waiting for clients");
				
				Socket socket = serverSocket.accept(); // The program execution will wait here until a connection is present
				
				System.out.println("Client connected");
				
				// Here once we get a client conn, we are assigning a thread to it
				ClientThread clientThread = new ClientThread(socket);
				clientThread.start();
						
				
			}
			
		} catch (IOException e) {
			System.out.println("Port is already being used or blocked");
		}

	}

}

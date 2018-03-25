package SingleThreadedserver;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class SingleThreadServer {
	
	public static void main(String[] args){
		int portNumber = 9091; // Port number of '0' means port is assigned automatically
		try {
			
			ServerSocket serverSocket = new ServerSocket(portNumber);
			Socket socket = serverSocket.accept(); // The program execution will wait here until a connection is present
			
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

package Client;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;

public class SocketClientApplication {
	
	public static void main(String[] args){
		try{
			InetAddress serverAddress = InetAddress.getByName("localhost");
			System.out.println("server ip address: "+serverAddress.getHostAddress());
			Socket socket = new Socket(serverAddress,9091);
			
			BufferedReader input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			PrintWriter out = new PrintWriter(socket.getOutputStream());
			
			System.out.println("Message from server"+ input.readLine());
			out.println("hello server"); // Sending message to the server
			
			input.close();
			out.close();
			socket.close();
			
		}
		catch(UnknownHostException e){
			
		} catch (IOException e) {
			
			e.printStackTrace();
		}
	}
	
}

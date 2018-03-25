
import java.net.InetAddress;
import java.net.UnknownHostException;

public class MyInetAddressExample {

	public static void main(String[] args) {
		
		try {
			InetAddress inetAddress = InetAddress.getLocalHost();
			System.out.println(inetAddress.getHostName());
			System.out.println(inetAddress.getHostAddress());
			
			InetAddress inetAddress2 = InetAddress.getByName("google.com");
			System.out.println(inetAddress.getHostName());
			System.out.println(inetAddress.getHostAddress());
				
			
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}

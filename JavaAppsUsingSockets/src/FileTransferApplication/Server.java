/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package FileTransferApplication;

import java.io.*;
import java.net.*;


public class Server 
{
    public static void main(String [] args)
    {
        try
        {
            //initialize the Server Socket class
        	// Server will listen on port 9090
            ServerSocket serverSocket = new ServerSocket(9090);
        
            //boolean variable to stop the server
            boolean isStopped = false;
            
            // This will accept a new connection and assign it to a new thread
            while(!isStopped)
            {
                //create client socket object
                Socket clientSocket = serverSocket.accept();
                //create and start client thread
                ServerThread serverThread = new ServerThread(clientSocket);
                serverThread.start();
            }
        }
        catch(IOException e)
        {
            System.out.println("Port 9090 is already opened! Please use another port.");
        }
        catch(Exception e)
        {
            System.out.println(e.toString());
        }
    }
}

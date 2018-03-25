/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package FileTransferApplication;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class Client 
{
    public static void main(String [] args)
    {
        try
        {
            InputStreamReader in = new InputStreamReader(System.in);     
            BufferedReader reader  = new BufferedReader(in);
            
            String ipAddress = "";
            String fileName = "";
            
            boolean isValid = false;
            
            
               System.out.print("Please enter a valid Server Ip address: ");
               //read the entered ip address
               ipAddress = reader.readLine();
            
            
            System.out.print("Please enter a file name: ");
            fileName = reader.readLine();

            
            Socket socket = new Socket(ipAddress, 9090);
            InputStream inputByte = socket.getInputStream();
            BufferedInputStream input = new BufferedInputStream(inputByte);
            PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
            
            //send desired filename
            out.println(fileName);
            //outputFile.write(new String("catalin").getBytes());
            int code = input.read();
            if (code == 1) 
            {
                BufferedOutputStream outputFile = new BufferedOutputStream(new FileOutputStream("D:\\download\\" + fileName));                
                byte[] buffer = new byte[1024];
                int bytesRead = 0;
                while ((bytesRead = input.read(buffer)) != -1) {
                    System.out.print("."); //acts as a download indicator
                    outputFile.write(buffer, 0, bytesRead);
                    outputFile.flush();
                }
                System.out.println();
                System.out.println("File: " + fileName + " was successfully downloaded!");
            }
            else
            {
                System.out.println("File is not present on the server!");
            }
            
        }
        catch(Exception e)
        {
            System.out.println(e.toString());
        }
    }
}

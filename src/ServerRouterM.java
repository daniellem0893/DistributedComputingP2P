import java.net.*;
import java.io.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class ServerRouterM {

	static int SockNum = 8181;
	public final static int MAX = 500;
	
	static RoutingTable[] tableM = new RoutingTable[MAX];
	
	 public static void main(String[] args) throws IOException 
	   { 
		 int index = 0;
		 
	    ServerSocket serverSocket = null; 

	    try { 
	         serverSocket = new ServerSocket(SockNum); 
	        } 
	    catch (IOException e) 
	        { 
	         System.err.println("Could not listen on port: " +SockNum + "."); 
	         System.exit(1); 
	        } 

	    Socket clientSocket = null; 
	    System.out.println ("Waiting for connection.....");
	    
	    try {
			Thread.sleep(500);
		} catch (InterruptedException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

	    try { 
	         clientSocket = serverSocket.accept(); 
	         RoutingTable temp = new RoutingTable();
	         temp.address = clientSocket.getInetAddress().toString();
	         temp.name = "Machine " + String.valueOf(index);
	         tableM[index] = temp;
	         index++;
	        } 
	    catch (IOException e) 
	        { 
	         System.err.println("Accept failed."); 
	         System.exit(1); 
	        } 

	    System.out.println ("Connection successful");
	    System.out.println ("Waiting for input.....");

	    PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), 
	                                      true); 
	    BufferedReader in = new BufferedReader( 
	            new InputStreamReader( clientSocket.getInputStream())); 
	    

	    String inputLine; 
	    String machineName = "";
	    String machineIP = "";

	    while ((inputLine = in.readLine()) != null) 
	        { 
	         System.out.println ("User: " + inputLine); 
	         
	         if(inputLine.contains("IP Request for: ")){
	        	 machineName = inputLine.substring(16);
	        	 
	        	 InetAddress addr = InetAddress.getLocalHost();
	        	 String host = addr.getHostAddress();
	        	 String altAddress = "edit me";
	        	 
	        	 try {
	 				Thread.sleep(500);
	 			} catch (InterruptedException e1) {
	 				// TODO Auto-generated catch block
	 				e1.printStackTrace();
	 			}
	        	 //switch host for altAddress when running on separate machines
	        	 String serverHostname = new String (host);
	        	 int SockNum2 = 9999;

	             if (args.length > 0)
	                serverHostname = args[0];
	             System.out.println ("Attemping to connect to host " +
	     		serverHostname + " on port" + SockNum2 + ".");

	             Socket echoSocket = null;
	             PrintWriter PWout = null;
	             BufferedReader BRin = null;

	             try {
	                 echoSocket = new Socket(serverHostname, SockNum2);
	                 PWout = new PrintWriter(echoSocket.getOutputStream(), true);
	                 BRin = new BufferedReader(new InputStreamReader(
	                                             echoSocket.getInputStream()));
	             } catch (UnknownHostException e) {
	                 System.err.println("Don't know about host: " + serverHostname);
	                 System.exit(1);
	             } catch (IOException e) {
	                 System.err.println("Couldn't get I/O for "
	                                    + "the connection to: " + serverHostname);
	                 System.exit(1);
	             }

		     	BufferedReader stdIn = new BufferedReader(
		                                        new InputStreamReader(System.in));
		     	String userInput;
	
		             //System.out.print ("input: ");
		     	    PWout.println(machineName);
		     	   try {
						Thread.sleep(5000);
					} catch (InterruptedException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
		     	   String tempIP;
		     	    tempIP = BRin.readLine();
		     	    machineIP = tempIP;
		     	    System.out.println(machineIP);
		     	   // machineIP = "192.168.244.1";
		     	    
		     	
		     	PWout.close();
		    	BRin.close();
		    	stdIn.close();
		    	echoSocket.close();
	         }
	         System.out.println(machineIP);
	         out.println(machineIP);
	         try {
					Thread.sleep(500);
				} catch (InterruptedException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
	         
	        } 

	    out.close(); 
	    in.close(); 
	    clientSocket.close(); 
	    serverSocket.close(); 
	   } 
    
    
}
import java.net.*;
import java.io.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class ServerRouterN {

	static int SockNum = 8282;
	public final static int MAX = 500;
	
	static RoutingTable[] tableN = new RoutingTable[MAX];
	
	 public static void main(String[] args) throws IOException 
	   { 
		 int index = 0;
		 int index2 = 0;
		 
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
	         clientSocket = serverSocket.accept(); 
	         RoutingTable temp = new RoutingTable();
	         temp.address = clientSocket.getInetAddress().toString().substring(1);
	         temp.name = "Machine " + index;
	         tableN[index] = temp;
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
	    String machineName;
	    String machineIP;

	    while ((inputLine = in.readLine()) != null) 
	        { 
	         System.out.println ("User: " + inputLine); 
	         
	         if(inputLine.contains("Waiting for connections")){
	        	 
	        			int SockNum2 = 9999;
	        		    ServerSocket serverSocket2 = null; 

	        		    try { 
	        		         serverSocket2 = new ServerSocket(SockNum2); 
	        		        } 
	        		    catch (IOException e) 
	        		        { 
	        		         System.err.println("Could not listen on port: " +SockNum2 + "."); 
	        		         System.exit(1); 
	        		        } 

	        		    Socket clientSocket2 = null; 
	        		    System.out.println ("Waiting for connection.....");

	        		    try { 
	        		         clientSocket2 = serverSocket2.accept(); 
	        		        } 
	        		    catch (IOException e) 
	        		        { 
	        		         System.err.println("Accept failed."); 
	        		         System.exit(1); 
	        		        } 

	        		    System.out.println ("Connection successful");
	        		    System.out.println ("Waiting for input.....");

	        		    PrintWriter out2 = new PrintWriter(clientSocket2.getOutputStream(), 
	        		                                      true); 
	        		    BufferedReader in2 = new BufferedReader( 
	        		            new InputStreamReader( clientSocket2.getInputStream())); 
	        		    

	        		    String inputLine2; 
	        		    String machineName2;
	        		    String machineIP2;
	        		    
	        		   inputLine2 = in2.readLine();
	        		   
	        		   System.out.println(inputLine2);
	        	        
	        		    	if(inputLine2.contains("Machine"))
	        		    	{
	        		    		machineName2 = inputLine2;
	        		    		for(int i = 0; i < index; i++)
	        		    		{
	        		    			if(tableN[i].name.contains(machineName2))
	        		    			{
	        		    				System.out.println("Machine IP for: " + machineName2 + " " + tableN[i].address);
	        		    				out2.println(tableN[i].address);
	        		    			}
	        		    		}
	        		    	
	        	        }
	        		    out2.close(); 
	        		    in2.close(); 
	        		    clientSocket2.close(); 
	        		    serverSocket2.close();

	         }
	         //
	        } 

	    out.close(); 
	    in.close(); 
	    clientSocket.close(); 
	    serverSocket.close(); 
	   } 
    
    
}
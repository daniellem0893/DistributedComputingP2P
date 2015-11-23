import java.io.*;
import java.net.*;
import java.util.*;
	
public class SThread extends Thread 
{
    private Object [][] RTable; // routing table
    private PrintWriter out, outTo; // writers (for writing back to the machine and to destination)
    private BufferedReader in; // reader (for reading from the machine connected to)
    private String inputLine, outputLine, destination, addr; // communication strings
    private Socket outSocket; // socket for communicating with a destination
    private int ind; // indext in the routing table

    SThread(Object[][]Table, Socket toClient, int index) throws IOException { 
        out = new PrintWriter(toClient.getOutputStream(), true);
        in = new BufferedReader(new InputStreamReader(toClient.getInputStream()));
        RTable = Table;
        addr = toClient.getInetAddress().getHostAddress();
        RTable[index][0] = addr; // IP addresses 
        RTable[index][1] = toClient; // sockets for communication
        ind = index;
    }

    public void run() {
        //Called once for each connection
        try {
            //Initial sends/receives
            destination = in.readLine(); //Destination address
            System.out.println("Forwarding to " + destination);
            out.println("Connected to the router."); // confirmation of connection

            //waits 10 seconds to let the routing table fill with all machines' information
            try{
                Thread.sleep(10000); 
            } catch(InterruptedException ie){
                System.out.println("Thread interrupted");
            }

            //Check if destination is in the routing table. If it is, get its corresponding socket and instantiate outTo to write to it.
            //long rTableLookupStartTime = System.currentTimeMillis();
            TimeStuff.startTimer();
            for (int i = 0; i < 10; i++) {
                if (destination.equals((String) RTable[i][0])){
                        outSocket = (Socket) RTable[i][1];
                        System.out.println("Found destination: " + destination);
                        outTo = new PrintWriter(outSocket.getOutputStream(), true);
                        
                }
            }
            TimeStuff.stopTimer("Routing table lookup for destination \"" + destination + "\":");
           // TimeStuff.saveMessageToFile("results.txt");
            //Communication loop: Read each line from in and print it to outTo	
            while ((inputLine = in.readLine()) != null) {
                System.out.println("Client/Server said: " + inputLine);
                if (inputLine.equals("Bye."))
                    break;
                outputLine = inputLine; //Passes the input from the machine to the output string for the destination

                if (outSocket != null){				
                    outTo.println(outputLine);
                }			
            }	
        } catch (IOException e) {
           System.err.println("Could not listen to socket.");
           System.exit(1);
        }
    }
}
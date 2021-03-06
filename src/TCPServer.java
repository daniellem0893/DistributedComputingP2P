import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.net.*;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;


public class TCPServer {
	
	static boolean wait = true;
	static int SockNum;
	static int secSockNum;
	static String host;
	static String tempHost;
	static String routerName;
	static String connectName;
	static boolean connectBool = false;
	
    public static void main(String[] args) throws IOException {
      	TimeStuff.initTimer();
        // Variables for setting up connection and communication
        Socket Socket = null; // socket to connect with ServerRouter
        PrintWriter out = null; // for writing to ServerRouter
        BufferedReader in = null; // for reading form ServerRouter
        InetAddress addr = InetAddress.getLocalHost();
        host = addr.getHostAddress(); // Server machine's IP
        tempHost = "Machine 0";
       // String routerName = "j263-08.cse1.spsu.edu"; // ServerRouter host name
        routerName = addr.getHostAddress();
        SockNum = 8282; // port number
        secSockNum = 9191;
        
        JFrame f = new JFrame();
        f.setTitle("Server");
        f.setSize(300, 325);
        f.setLocationRelativeTo(null);
       
        
        final JPanel panel = new JPanel();
        
        JLabel rName = new JLabel("Router Name: ");
      //  JLabel status = new JLabel("Waiting to establish port number.");
        final JTextField rNameText = new JTextField(routerName);
        JLabel hName = new JLabel("Host Name: ");
        final JTextField hostText = new JTextField(host);
        JLabel port = new JLabel("Port Number: ");
        final JTextField portText = new JTextField(Integer.toString(SockNum));
        JLabel connect = new JLabel("Connect To: ");
        final JTextField connectText = new JTextField(tempHost);
        JLabel secSock = new JLabel("On Socket: ");
        final JTextField secSockText = new JTextField(Integer.toString(secSockNum));
        JButton submit = new JButton("submit");
        JButton results = new JButton("results");
        
        JTextArea messages = new JTextArea("Messages will show up here.");
        JScrollPane scroll = new JScrollPane(messages);
        
        rNameText.setEditable(true);
        rNameText.setColumns(15);

        portText.setEditable(true);
        portText.setColumns(15);
        
        hostText.setEditable(true);
        hostText.setColumns(15);
        
        connectText.setEditable(true);
        connectText.setColumns(15);
        
        secSockText.setEditable(true);
        secSockText.setColumns(15);
        
        messages.setColumns(25);
        messages.setRows(5);
        messages.setLineWrap(true);
        messages.setWrapStyleWord(true);

        panel.add(rName);
        panel.add(rNameText);
        panel.add(hName);
        panel.add(hostText);
        panel.add(port);
        panel.add(portText);
        panel.add(connect);
        panel.add(connectText);
        panel.add(secSock);
        panel.add(secSockText);
        panel.add(submit);
        panel.add(scroll);
        panel.add(results);
        //panel.add(status);
        
        f.add(panel, BorderLayout.CENTER);
        
        panel.setVisible(true);
        f.setVisible(true);
        
        submit.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
              String tempRName = rNameText.getText();
              routerName = tempRName;
              String tempHostName = hostText.getText();
              host = tempHostName;
              String tempPort = portText.getText();
              String tempPort2 = secSockText.getText();
              connectName = connectText.getText();
              SockNum = Integer.parseInt(tempPort);
              secSockNum = Integer.parseInt(tempPort2);
              wait = false;
            }
        });
        
        results.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
              TimeStuff.saveMessageToFile("results.txt");
            }
        });
        
        while(wait == true)
        {
        	try {
				Thread.sleep(500);
			} catch (InterruptedException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
        }
			
     // Tries to connect to the ServerRouterN
        
        
     		String serverHostname1 = new String (host);

             if (args.length > 0)
                serverHostname1 = args[0];
             System.out.println ("Attemping to connect to host " +
     		serverHostname1 + " on port " + SockNum + ".");

             Socket echoSocket1 = null;
             PrintWriter out1 = null;
             BufferedReader in1 = null;

             try {
                 echoSocket1 = new Socket(serverHostname1, SockNum);
                 out1 = new PrintWriter(echoSocket1.getOutputStream(), true);
                 in1 = new BufferedReader(new InputStreamReader(
                                             echoSocket1.getInputStream()));
             } catch (UnknownHostException e) {
                 System.err.println("Don't know about host: " + serverHostname1);
                 System.exit(1);
             } catch (IOException e) {
                 System.err.println("Couldn't get I/O for "
                                    + "the connection to: " + serverHostname1);
                 System.exit(1);
             }
     	
     		BufferedReader stdIn1 = new BufferedReader(
     	                                   new InputStreamReader(System.in));
     		String userInput1;
     	
     		    out1.println("Waiting for connections");
     		   // tempHost = in1.readLine();
     		    
     		   try {
   				Thread.sleep(500);
   			} catch (InterruptedException e1) {
   				// TODO Auto-generated catch block
   				e1.printStackTrace();
   			}
     		
     	
     		out1.close();
     		in1.close();
     		stdIn1.close();
     		echoSocket1.close();
     		
     		
     		
     		
     		
		// Waits to connect with the Client
     		try {
				Thread.sleep(500);
			} catch (InterruptedException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
        
        ServerSocket serverSocket = null; 

        try { 
             serverSocket = new ServerSocket(secSockNum); 
            } 
        catch (IOException e) 
            { 
             System.err.println("Could not listen on port: " +secSockNum+"."); 
             System.exit(1); 
            } 

        Socket clientSocket = null; 
        System.out.println ("Waiting for connection.....");

        try { 
             clientSocket = serverSocket.accept(); 
            } 
        catch (IOException e) 
            { 
             System.err.println("Accept failed."); 
             System.exit(1); 
            } 

        System.out.println ("Connection successful");
        System.out.println ("Waiting for input.....");

        PrintWriter PWout = new PrintWriter(clientSocket.getOutputStream(), 
                                          true); 
        BufferedReader BRin = new BufferedReader( 
                new InputStreamReader( clientSocket.getInputStream())); 

        String inputLine; 

        while ((inputLine = BRin.readLine()) != null) 
            { 
             System.out.println ("Server: " + inputLine); 
             PWout.println(inputLine.toUpperCase()); 

             if (inputLine.equals("Bye.")) 
                 break; 
            } 
				
    }
    
    public String getIP(){
    	return host;
    }
}
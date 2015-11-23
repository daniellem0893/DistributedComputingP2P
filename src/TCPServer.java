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
	static String host;
	static String routerName;
	
    public static void main(String[] args) throws IOException {
      	TimeStuff.initTimer();
        // Variables for setting up connection and communication
        Socket Socket = null; // socket to connect with ServerRouter
        PrintWriter out = null; // for writing to ServerRouter
        BufferedReader in = null; // for reading form ServerRouter
        InetAddress addr = InetAddress.getLocalHost();
        host = addr.getHostAddress(); // Server machine's IP			
       // String routerName = "j263-08.cse1.spsu.edu"; // ServerRouter host name
        routerName = addr.getHostAddress();
        SockNum = 8181; // port number
        
        JFrame f = new JFrame();
        f.setTitle("Server");
        f.setSize(300, 275);
        f.setLocationRelativeTo(null);
       
        
        final JPanel panel = new JPanel();
        
        JLabel rName = new JLabel("Router Name: ");
      //  JLabel status = new JLabel("Waiting to establish port number.");
        final JTextField rNameText = new JTextField(routerName);
        JLabel hName = new JLabel("Host Name: ");
        final JTextField hostText = new JTextField(host);
        JLabel port = new JLabel("Port Number: ");
        final JTextField portText = new JTextField(Integer.toString(SockNum));
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
              SockNum = Integer.parseInt(tempPort);
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
			
        // Tries to connect to the ServerRouter
        try {
            Socket = new Socket(routerName, SockNum);
            out = new PrintWriter(Socket.getOutputStream(), true);
            in = new BufferedReader(new InputStreamReader(Socket.getInputStream()));
        } catch (UnknownHostException e) {
            System.err.println("Don't know about router: " + routerName);
            messages.setText("Don't know about router: " + routerName);
            System.exit(1);
        } catch (IOException e) {
            System.err.println("Couldn't get I/O for the connection to: " + routerName);
            messages.setText("Couldn't get I/O for the connection to: " + routerName);
            System.exit(1);
        }
				
      	// Variables for message passing			
        String fromServer; // messages sent to ServerRouter
        String fromClient; // messages received from ServerRouter      
       // String address ="10.5.3.196"; // destination IP (Client)
		String address = addr.getHostAddress();
        
        // Communication process (initial sends/receives)
        out.println(address);// initial send (IP of the destination Client)
        fromClient = in.readLine();// initial receive from router (verification of connection)
        System.out.println("ServerRouter: " + fromClient);
        messages.setText("ServerRouter: " + fromClient);
			         
        // Communication while loop
      	while ((fromClient = in.readLine()) != null) {
            System.out.println("Client said: " + fromClient);
            messages.setText(messages.getText() + "\n" + "Client said: " + fromClient);
            if (fromClient.equals("Bye.")) // exit statement
                break;
            fromServer = fromClient.toUpperCase(); // converting received message to upper case
            System.out.println("Server said: " + fromServer);
            messages.setText(messages.getText() + "\n" + "Server said: " + fromServer);
            out.println(fromServer); // sending the converted message back to the Client via ServerRouter
        }
			
        // closing connections
        out.close();
        in.close();
        Socket.close();
    }
}
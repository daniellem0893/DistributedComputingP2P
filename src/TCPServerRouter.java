import java.net.*;
import java.io.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class TCPServerRouter {
	
	static int SockNum = 8181;
	static boolean wait = true;
	
    public static void main(String[] args) throws IOException {
    	TimeStuff.initTimer();
    	 Socket clientSocket = null; // socket for the thread
    	 //ServerSocket serverSocket = null; // server socket for accepting connections
    	 int ind = 0; // indext in the routing table
    	
        Object [][] RoutingTable = new Object [10][2]; // routing table
         // port number
        Boolean Running = true;
    	
        
        JFrame f = new JFrame();
        f.setTitle("Server Router");
        f.setSize(300, 250);
        f.setLocationRelativeTo(null);
       
        
        final JPanel panel = new JPanel();
        
        JLabel port = new JLabel("Port Number: ");
      //  JLabel status = new JLabel("Waiting to establish port number.");
        final JTextField text = new JTextField();
        JButton submit = new JButton("submit");
        JButton results = new JButton("results");
        
        JTextArea messages = new JTextArea("Messages will show up here.");
        JScrollPane scroll = new JScrollPane(messages);
        
        
        text.setText(Integer.toString(SockNum));
        text.setEditable(true);
        text.setColumns(15);
        
        messages.setColumns(25);
        messages.setRows(5);
        messages.setLineWrap(true);
        messages.setWrapStyleWord(true);
        
        submit.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
              String temp = text.getText();
              SockNum = Integer.parseInt(temp);
              wait = false;
            }          
         });
        
        results.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
              TimeStuff.saveMessageToFile("results.txt");
            }          
         });
        
        panel.add(port);
        panel.add(text);
        panel.add(submit);
        panel.add(scroll);
        panel.add(results);
        //panel.add(status);
        
        
        
        f.add(panel, BorderLayout.CENTER);
        
        panel.setVisible(true);
        f.setVisible(true);
        
        while(wait == true)
        {
        	try {
				Thread.sleep(500);
			} catch (InterruptedException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
        }
        
       // TimeStuff.initTimer();
        
        //Accepting connections
        ServerSocket serverSocket = null; // server socket for accepting connections
        try {
        	//int tempport = SockNum;
            serverSocket = new ServerSocket(SockNum);
            System.out.println("ServerRouter is Listening on port: " + SockNum +".");
            messages.setText("ServerRouter is Listening on port: " + SockNum +".");
        } catch (IOException e) {
            System.err.println("Could not listen on port: " + SockNum + ".");
            messages.setText("Could not listen on port: " + SockNum + ".");
            System.exit(1);
        }
			
        // Creating threads with accepted connections
        while (Running == true)
        {
            try {
                clientSocket = serverSocket.accept();
                SThread t = new SThread(RoutingTable, clientSocket, ind); // creates a thread with a random port
                t.start(); // starts the thread
                ind++; // increments the index
                System.out.println("ServerRouter connected with Client/Server: " + clientSocket.getInetAddress().getHostAddress());
                messages.setText(messages.getText() + "\n" + "ServerRouter connected with Client/Server: " + clientSocket.getInetAddress().getHostAddress());
            } catch (IOException e) {
                System.err.println("Client/Server failed to connect.");
                messages.setText(messages.getText() + "\n" + "Client/Server failed to conenct.");
                System.exit(1);
            }
        }
        
        //closing connections
        clientSocket.close();
        serverSocket.close();
    }
}
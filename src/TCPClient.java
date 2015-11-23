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
import javax.swing.text.DefaultCaret;

public class TCPClient {

	static String host;
	static String routerName;
	static String fileName;
	static int SockNum;
	static boolean wait = true;

	public static void main(String[] args) throws IOException {
		TimeStuff.initTimer();
		// Variables for setting up connection and communication
		Socket socket = null; // socket to connect with ServerRouter
		PrintWriter out = null; // for writing to ServerRouter
		BufferedReader in = null; // for reading form ServerRouter
		InetAddress addr = InetAddress.getLocalHost();
		host = addr.getHostAddress(); // Client machine's IP
		routerName = addr.getHostAddress();// "j263-08.cse1.spsu.edu"; //
											// ServerRouter host name
		SockNum = 8181; // port number

		JFrame f = new JFrame();
		f.setTitle("Client");
		f.setSize(305, 300);
		f.setLocationRelativeTo(null);

		final JPanel panel = new JPanel();

		JLabel rName = new JLabel("Router Name: ");
		// JLabel status = new JLabel("Waiting to establish port number.");
		final JTextField rNameText = new JTextField(routerName);
		JLabel hName = new JLabel("Host Name: ");
		final JTextField hostText = new JTextField(host);
		JLabel port = new JLabel("Port Number: ");
		final JTextField portText = new JTextField(Integer.toString(SockNum));
		JLabel file = new JLabel("File Name: ");
		final JTextField fileText = new JTextField("file.txt");
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

		fileText.setEditable(true);
		fileText.setColumns(15);

		messages.setColumns(25);
		messages.setRows(5);
		messages.setLineWrap(true);
		messages.setWrapStyleWord(true);

		DefaultCaret caret = (DefaultCaret) messages.getCaret();
		caret.setUpdatePolicy(DefaultCaret.OUT_BOTTOM);

		panel.add(rName);
		panel.add(rNameText);
		panel.add(hName);
		panel.add(hostText);
		panel.add(port);
		panel.add(portText);
		panel.add(file);
		panel.add(fileText);
		panel.add(submit);
		panel.add(scroll);
		panel.add(results);
		// panel.add(status);

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
				fileName = fileText.getText();
				wait = false;
			}
		});

		results.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				TimeStuff.saveMessageToFile("results.txt");
			}
		});

		while (wait == true) {
			try {
				Thread.sleep(500);
			} catch (InterruptedException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}

		// Tries to connect to the ServerRouter
		try {
			socket = new Socket(routerName, SockNum);
			// BufferedInputStream bis = new BufferedInputStream(new
			// FileInputStream(fileName));
			// BufferedOutputStream bos = new
			// BufferedOutputStream(socket.getOutputStream());
			out = new PrintWriter(socket.getOutputStream(), true);
			in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
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
		Reader reader = new FileReader(fileName);
		BufferedReader fromFile = new BufferedReader(reader); // reader for the
																// string file
		String fromServer; // messages received from ServerRouter
		String fromUser; // messages sent to ServerRouter
		String address = addr.getHostAddress();// "10.5.2.109"; // destination
												// IP (Server)
		long t0, t1, t;
		long total = 0;

		// Communication process (initial sends/receives
		out.println(address);// initial send (IP of the destination Server)
		fromServer = in.readLine();// initial receive from router (verification
									// of connection)
		System.out.println("ServerRouter: " + fromServer);
		messages.setText("ServerRouter: " + fromServer);
		out.println(host); // Client sends the IP of its machine as initial send
		t0 = System.currentTimeMillis();
		TimeStuff.startTimer();

		// Communication while loop
		while ((fromServer = in.readLine()) != null) {
			System.out.println("Server: " + fromServer.toUpperCase());
			messages.setText(messages.getText() + "\n" + "Server: " + fromServer.toUpperCase());
			 t1 = System.currentTimeMillis();
			 TimeStuff.startTimer();
			if (fromServer.equals("Bye.")) // exit statement
				break;
			 t = t1 - t0;
			 total = total + t;
			 System.out.println("Cycle time: " + t);
			TimeStuff.stopTimer("Cycle time: ");
			System.out.println("Total time: " + total);
			messages.setText(messages.getText() + "\n" + "Total time: " + total);

			fromUser = fromFile.readLine(); // reading strings from a file
			if (fromUser != null) {
				System.out.println("Client: " + fromUser);
				messages.setText(messages.getText() + "\n" + "Client: " + fromUser);
				out.println(fromUser); // sending the strings to the Server via
										// ServerRouter
				t0 = System.currentTimeMillis();
			}
		}
		
		

		// closing connections
		out.close();
		in.close();
		socket.close();
	}
}
package portfolio1;

import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTextField;

import portfolio1.ChatScreen;
import portfolio1.DataModel;
import portfolio1.MyThread;
import portfolio1.LoginScreen;

public class PortfolioClient
{
	public static void main(String[] args)
	{
		// Create a login page, running it's main method will take care of the
		// rest
		LoginScreen loginScreen = new LoginScreen();
		loginScreen.main(args);
	}
}

class LoginScreen extends JFrame
{
	static Socket MySocket;
	static String strClientName;

	public static void main(String[] args)
	{
		EventQueue.invokeLater(new Runnable()
		{
			public void run()
			{
				try
				{
					LoginScreen frame = new LoginScreen();
					frame.setVisible(true);

				} catch (Exception e)
				{
					e.printStackTrace();
				}
			}
		});
	}

	public LoginScreen()
	{
		this.setTitle("Login Page");
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setBounds(100, 100, 450, 422);
		JPanel contentPane = new JPanel();
		this.setContentPane(contentPane);

		// Organize the form into 3 panels
		JPanel topPane = new JPanel();
		JPanel midPane = new JPanel();
		JPanel botPane = new JPanel();

		contentPane.add(topPane);
		contentPane.add(midPane);
		contentPane.add(botPane);

		// Create the label that says "CLIENT"
		JLabel lblClient = new JLabel("CLIENT");
		lblClient.setFont(new Font("Seriff", Font.BOLD, 50));
		topPane.add(lblClient);

		// Create the label that says "Enter your name"
		JLabel lblEnterName = new JLabel("Enter your name");
		lblEnterName.setFont(new Font("Seriff", Font.PLAIN, 40));
		midPane.add(lblEnterName);

		// Create the text box
		JTextField txtName = new JTextField("Please enter your name");
		txtName.setFont(new Font("Seriff", Font.PLAIN, 25));
		botPane.add(txtName);

		// Login Button
		JButton btnLogin = new JButton("Login");
		btnLogin.addActionListener(new ActionListener()
		{

			@Override
			public void actionPerformed(ActionEvent arg0)
			{
				try
				{
					// Connect at port 4444
					MySocket = new Socket("localhost", 4444);

					// Set the Client's name
					strClientName = txtName.getText();

					// Wait for server to be ready
					Thread.sleep(1000);

				} catch (Exception e)
				{
					e.printStackTrace();
				}

				// Create the chat screen and send the client's name to the
				// server
				ChatScreen csChat = new ChatScreen(strClientName, MySocket);
				try
				{
					PrintWriter logon = new PrintWriter(MySocket.getOutputStream());
					logon.println("logon;" + strClientName);
					logon.flush();
					
				} catch (IOException e)
				{
					e.printStackTrace();
				}

				csChat.setVisible(true);
				setVisible(false);
				// ChatScreen.main(null);
			}

		});

		botPane.add(btnLogin);
	}
}

class ChatScreen extends JFrame
{

	public DataModel<String> dmChatHistory = new DataModel<String>();
	public DataModel<String> dmOnlineUsers = new DataModel<String>();

	public static void main(String[] args)
	{
		EventQueue.invokeLater(new Runnable()
		{
			public void run()
			{
				try
				{
					// Everything is taken care of in the constructor

				} catch (Exception e)
				{
					e.printStackTrace();
				}
			}
		});
	}

	public ChatScreen(String strClientName, Socket mySocket)
	{
		this.setTitle("Chat Screen");
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setBounds(100, 100, 450, 422);

		Thread serverListener = new Thread(new MyThread(mySocket, this));
		serverListener.start();

		// Set the main content pane
		JPanel mainContentPane = new JPanel();
		this.setContentPane(mainContentPane);
		mainContentPane.setLayout(new BoxLayout(mainContentPane, BoxLayout.Y_AXIS));
		// Divide the screen into 2 panels
		// Add the Chat recap box to the top panel
		JList<String> listChatRecap = new JList<String>();
		listChatRecap.setModel(dmChatHistory);
		JScrollPane topLeftPanel = new JScrollPane(listChatRecap);
		topLeftPanel.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		JPanel botPanel = new JPanel();

		// Make a list of current online users
		JList<String> listOnlineUsers = new JList<String>();
		listOnlineUsers.setModel(dmOnlineUsers);
		listOnlineUsers.setSelectionBackground(Color.YELLOW);
		
		JScrollPane topRightPanel = new JScrollPane(listOnlineUsers);

		JSplitPane topPanel = new JSplitPane();
		topPanel.setOrientation(JSplitPane.HORIZONTAL_SPLIT);
		topPanel.setDividerLocation(250);
		topPanel.setLeftComponent(topLeftPanel);
		topPanel.setRightComponent(topRightPanel);

		JPanel allTopPanels = new JPanel();
		allTopPanels.add(topPanel);
		mainContentPane.add(allTopPanels);
		mainContentPane.add(botPanel);

		// Add the message stuff to the bottom panel
		JLabel lblMessage = new JLabel("Message");
		JTextField txtMessage = new JTextField("Enter your message here");
		JButton btnSend = new JButton("Send");

		botPanel.add(lblMessage);
		botPanel.add(txtMessage);
		botPanel.add(btnSend);

		btnSend.addActionListener(new ActionListener()
		{

			@Override
			public void actionPerformed(ActionEvent arg0)
			{
				try
				{
					String recipient = listOnlineUsers.getSelectedValue();
					String constructedMessage = strClientName + " > " + recipient+ ": " + txtMessage.getText();
					// Send the message to the server
					PrintWriter out = new PrintWriter(mySocket.getOutputStream());
					out.println("send;" + recipient + ";"
					+ constructedMessage);
					out.flush();
					
					dmChatHistory.addItem(constructedMessage);

				} catch (IOException e)
				{
					e.printStackTrace();
				}

			}

		});

		// When the user closes their chat window, notify the server that they
		// have logged off
		this.addWindowListener(new java.awt.event.WindowAdapter()
		{
			@Override
			public void windowClosing(java.awt.event.WindowEvent windowEvent)
			{
				try
				{
					PrintWriter out = new PrintWriter(mySocket.getOutputStream());
					out.println("logoff;" + strClientName);
					out.flush();
				} catch (IOException e)
				{
					e.printStackTrace();
				}
			}
		});
	}
}

class MyThread implements Runnable
{
	Socket serverSocket;
	ChatScreen clientScreen;

	public MyThread(Socket mySocket, ChatScreen csChatScreen)
	{
		serverSocket = mySocket;
		clientScreen = csChatScreen;
	}

	public void run()
	{
		Scanner in;
		try
		{
			//Setup a scanner to read messages from the server
			in = new Scanner(serverSocket.getInputStream());

			//Loop forever
			while (true)
			{
				//When we get a new message, process it
				String newMessage = in.nextLine();
				processServerMessage(newMessage);
			}
		} catch (IOException e)
		{
			e.printStackTrace();
		}

	}

	private void processServerMessage(String newMessage)
	{
		// Split the message at ';'
		String[] arrServerMessage = newMessage.split(";");
		String keyWord = arrServerMessage[0];

		switch (keyWord)
		{
		case "message": //Add the message to the chat history
			String message = arrServerMessage[1];
			clientScreen.dmChatHistory.addItem(message);
			break;
			
		case "logon": //A new user has logged on, add their name to the list
			String clientLogOn = arrServerMessage[1];
			clientScreen.dmOnlineUsers.addItem(clientLogOn);
			break;
			
		case "logoff": //A user has logged off, remove their name from the list
			String clientLogOff = arrServerMessage[1];
			int index = 0;
			for(String name : clientScreen.dmOnlineUsers.arrListData)
			{
				if (name.equals(clientLogOff))
				{
					break;
				}
				index += 1;
			}
			
			clientScreen.dmOnlineUsers.removeItem(index);
			break;
		}
	}
}
package portfolio1;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Scanner;

import portfolio1.ClientHandler;
import portfolio1.ConnectionToClient;

public class PortfolioServer
{

	public static void main(String[] args) throws IOException
	{

		ServerSocket serverSocket = null;
		ArrayList<ConnectionToClient> connections = new ArrayList<ConnectionToClient>();

		// Create the server socket
		try
		{
			// Listen on port 4444
			serverSocket = new ServerSocket(4444);
			System.out.println(serverSocket);

		} catch (IOException e)
		{
			System.out.println("Could not listen on port: 4444");
			System.exit(-1);
		}

		// Loop forever to keep the server running
		while (true)
		{
			Socket clientSocket = null;
			ConnectionToClient clientCon = null;
			try
			{

				// Wait for client to connect, this is so we know the server is
				// running
				System.out.println("Waiting for client to connect!");
				clientSocket = serverSocket.accept();
				clientCon = new ConnectionToClient(clientSocket);
				connections.add(clientCon);

				// Notify us we successfully connected, and spawn a
				// clienthandler thread
				System.out.println("Server got connected to a client");

				Thread t = new Thread(new ClientHandler(clientSocket, connections));
				t.start();

			} catch (IOException e)
			{
				System.out.println("Accept failed: 4444");
				System.exit(-1);
			}
		}
	}
}

class ClientHandler implements Runnable
{
	// Server socket that connects to the client
	Socket s;
	// List of connections so we can send messages between clients
	ArrayList<ConnectionToClient> connections;

	ClientHandler(Socket s, ArrayList<ConnectionToClient> clientCons)
	{
		this.s = s;
		connections = clientCons;
	}

	// This is the client handling code
	public void run()
	{
		Scanner in;
		try
		{
			while (true)
			{

				// Read the client's message from the socket
				in = new Scanner(s.getInputStream());
				String clientMessage = in.nextLine();

				// Figure out what the client needs the server to do
				proccessClientMessage(clientMessage);

			}
		} catch (IOException e)
		{
			e.printStackTrace();
		}
	}

	private void proccessClientMessage(String clientMessage)
	{
		// Split the string at ';'
		String[] arrSplitMessage = clientMessage.split(";");

		// Look at the first string in the split array, this will tell us what
		// to do
		switch (arrSplitMessage[0])
		{
		case "logon":
			performLogon(arrSplitMessage[1]);
			break;
		case "logoff":
			performLogoff(arrSplitMessage[1]);
			break;
		case "send":
			performSendMessage(arrSplitMessage[1], arrSplitMessage[2]);
		}

	}

	private void performSendMessage(String recipient, String message)
	{
		// Print the message to the chat file
		PrintWriter FileOut;
		PrintWriter serverOut;
		File outFile = new File("Chat.txt");

		try
		{
			FileOut = new PrintWriter(new FileWriter(outFile));

			FileOut.println(message);
			FileOut.close();

			// Send the message to the recipient
			for (ConnectionToClient con : connections)
			{
				if (con.clientName.equals(recipient))
				{
					serverOut = new PrintWriter(con.out);

					serverOut.println("message;" + message);
					serverOut.flush();
				}

			}

		} catch (IOException e)
		{
			e.printStackTrace();
		}
	}

	private void performLogoff(String clientToLogoff)
	{

		PrintWriter FileOut;
		PrintWriter serverOut;
		File outFile = new File("Chat.txt");

		try
		{
			// Record the logoff
			FileOut = new PrintWriter(new FileWriter(outFile));
			FileOut.println(clientToLogoff + " has logged off.");
			FileOut.close();

			// Notify all clients of the logoff
			for (ConnectionToClient con : connections)
			{
				serverOut = new PrintWriter(con.out);
				serverOut.println("logoff;" + clientToLogoff);
				serverOut.flush();
			}

		} catch (IOException e)
		{
			e.printStackTrace();
		}
	}

	private void performLogon(String clientToLogon)
	{
		connections.get(connections.size() - 1).clientName = clientToLogon;
		PrintWriter FileOut;
		PrintWriter serverOut;
		File outFile = new File("Chat.txt");

		try
		{
			// Record the logoff
			FileOut = new PrintWriter(new FileWriter(outFile));
			FileOut.println(clientToLogon + " has logged on.");
			FileOut.close();
			
			// Tell the newest client about all others that are online

			serverOut = new PrintWriter(connections.get(connections.size() - 1).out);
			
			for (ConnectionToClient con2 : connections)
			{
				if (!(con2.clientName.equals(clientToLogon)))
				{
					serverOut.println("logon;" + con2.clientName);
					serverOut.flush();
				}
			}

			// Notify all clients of the logon
			for (ConnectionToClient con : connections)
			{
				serverOut = new PrintWriter(con.out);
				serverOut.println("logon;" + clientToLogon);
				serverOut.flush();

			}

		} catch (IOException e)
		{
			e.printStackTrace();
		}
	}

} // end of class ClientHandler

class ConnectionToClient
{
	InputStream in;
	OutputStream out;
	Socket conSocket;
	String clientName;

	ConnectionToClient(Socket socket)
	{
		conSocket = socket;
		try
		{
			in = conSocket.getInputStream();
			out = conSocket.getOutputStream();
		} catch (IOException e)
		{
			e.printStackTrace();
		}
	}
}

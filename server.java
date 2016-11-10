import java.awt.EventQueue;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Scanner;
import javax.swing.JFrame;

class Server
{
  public static void main(String[] args) throws IOException
  {
    ServerSocket server = null;
    int clientNum = 0;
    ArrayList<ConnectionToClient> connections = new ArrayList<ConnectionToClient>();

    //Create a new server ServerSocket
    try
    {
      //Use port 4444
      server = new ServerSocket(4444);
      System.out.println(server);
    }
    catch(IOException e)
    {
      System.out.println("Could not listen on port: 4444");
      System.exit(-1);
    }

    //Loop forever - Server is always waiting for clients
    while(true)
    {
      Socket client = null;
      ConnectionToClient clientCon = null;
      try
      {
        //Wait for a client to try connecting
        System.out.println("Waiting for a customer to connect.");
        client = server.accept();
        clientCon = new ConnectionToClient(client);
        connections.add(clientCon);

        //Spawn a thread to handle the client
        System.out.println("Client connected to the server.");
        Thread t = new Thread(new ClientHandler(client, connections));
        t.start();
      }
      catch(IOException e)
      {
        System.out.println("Accept failed: 4444");
        System.exit(-1);
      }
    }//End of while loop
  }//end of main()

  class ClientHandler implements Runnable
  {
    Socket s;
    ArrayList<ConnectionToClient> connections;

    ClientHandler(Socket s, ArrayList<ConnectionToClient> clientCons)
    {
      this.s = s;
      connections = clientCons;
    }

    //This is where we will process the client requests,
    //i.e. movie rentals
    public void run()
    {
      Scanner in;
      try
      {
        while(true)
        {
          //Read the request from the socket
          in = new Scanner(s.getInputStream());
          String clientRequest = in.nextLine();

          //Process that request
          proccessRequest(clientRequest);
        }
      }
      catch(IOException e)
      {
        e.printStackTrace();
      }
    }

    private void proccessRequest(String request)
    {
      //Split the request string, delimiter is ';'
      String[] arrSplitRequest = request.split(";");

      //First string in the array carries the command
      switch(arrSplitRequest[0])
      {
        case "isAvailable":
          checkAvailability(arrSplitRequest[1]);
          break;
        case "checkout":
          performCheckout(arrSplitRequest[1]);
          break;
        case "rentalCost":
          checkAmountDue(arrSplitRequest[1], arrSplitRequest[2]);
          break;
        case "return":
          returnMovie(arrSplitRequest[1]);
          break;
      }
    }

    private void checkAvailability(String movieID)
    {
      //TODO
    }

    private void performCheckout(String movieID)
    {
      //TODO
    }

    private void checkAmountDue(String movieID, String numDays)
    {
      //TODO
    }

    private void returnMovie(String movieID)
    {
      //TODO
    }
  }

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
      }
      catch(IOException e)
      {
        e.printStackTrace();
      }
    }
  }
}

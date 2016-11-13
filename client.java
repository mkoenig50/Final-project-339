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

class Client
{
  public static void main(String[] args)
  {
    //Create the login page, it's main method will take it from here
    LoginScreen loginscreen = new LoginScreen();
    loginscreen.main(args);
  }
}

class LoginScreen extends JFrame
{
  static Socket MySocket;
  static String strClientName;

  public static void main(String[] args)
  {
    EventQueue.invokeLater(new RUnnable()
    {
      public void run()
      {
        try
        {
          LoginScreen frame = new LoginScreen();
          frame.setVisible(true);
        }
        catch (Exception e)
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

    //Organize the form into 3 panels
    JPanel topPane = new JPanel();
    JPanel midPane = new JPanel();
    JPanel botPane = new JPanel();

    contentPane.add(topPane);
    contentPane.add(midPane);
    contentPane.add(botPane);

    //Add a few labels
    JLabel lblClient = new JLabel("CLIENT");
    lblClient.setFont(new Font("Seriff", Font.BOLD, 50));
    topPane.add(lblClient);

    JLabel lblEnterName = new JLabel("Enter your name");
    lblEnterName.setFont(new Font("Seriff", Font.PLAIN, 40));
    midPane.add(lblEnterName);

    // Create the text box
		JTextField txtName = new JTextField("Please enter your name");
		txtName.setFont(new Font("Seriff", Font.PLAIN, 25));
		botPane.add(txtName);

    //Login Button
    JBUtton btnLogin = new JButton("Login");
  }
}

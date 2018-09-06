import javax.swing.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.util.Scanner;


/**
 * Trivial client for the date server.
 */
public class SocketMaker{

    /**
     * Runs the client as an application.  First it displays a dialog
     * box asking for the IP address or hostname of a host running
     * the date server, then connects to it and displays the date that
     * it serves.
     */
    public static void main(String[] args) throws IOException {
	        
	String serverAddress = ""; 
	
        Scanner in = new Scanner(System.in);
	
	while (!(serverAddress.equals("exit") || serverAddress.equals("Exit"))){
	System.out.println("Enter a IP address or Exit to End");
        serverAddress = in.nextLine();
	if (serverAddress.equals("Exit")){
		System.out.println("TERMINATING! GOOD BYE!");
		System.exit(0);	
	}else{
        
	Socket s = new Socket(serverAddress, 9090);
        BufferedReader input = new BufferedReader(new InputStreamReader(s.getInputStream()));
        String answer = "";
	while ( (answer = input.readLine()) != null){ 
        	System.out.println(answer);
	
	}
	s.close();
	}
	}	
        System.exit(0);
    }
}

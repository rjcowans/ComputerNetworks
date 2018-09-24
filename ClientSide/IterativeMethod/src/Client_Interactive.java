import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.text.ParseException;
import java.util.Scanner;

/**
 * Trivial client for the date server.
 */
public class SocketMaker  {

    /**
     * Runs the client as an application.  First it displays a dialog
     * box asking for the IP address or hostname of a host running
     * the date server, then connects to it and displays the date that
     * it serves.
     */
    public static void main(String[] args) throws IOException, ParseException {
        String serverAddress = args[0];
        String command = "";
        Scanner in = new Scanner(System.in);
        while (!(command.equals("7"))){
            System.out.println("Enter a command my friend or exit to end Program");
            command  = in.nextLine();
            if (command.equals("exit")) {
                System.out.println("TERMINATING! GOOD BYE!");
                System.exit(0);
            } else {
                Socket s = new Socket(serverAddress, 9090);
                PrintWriter go = new PrintWriter(s.getOutputStream(),true);
                go.println(command);
                System.out.println("Sent " + command);
                BufferedReader input = new BufferedReader(new InputStreamReader(s.getInputStream()));
                System.out.println("Recieved answer");
                String line;
                while ((line = input.readLine()) != null){
                    String answer = line;
                    System.out.println(answer);
                }
                s.close();

            }
        }
        System.exit(0);
    }
}
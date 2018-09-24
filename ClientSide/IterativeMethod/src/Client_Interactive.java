import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.text.ParseException;
import java.util.Scanner;


public class Client_Interactive {

    /**
     * Interactive Client for Demo Purposes
     * TODO: Fill Out Correctly
     */
    public static void main(String[] args) throws IOException, ParseException {

        Scanner input = new Scanner(System.in);

        //Get Server Address
        if (args[0] == null) {//No address given
            //Print error message then exit.
            System.out.print("ERROR: No Server Address Specified. Press Enter to Exit.");
            input.nextLine();
            return;
        }
        String serverAddress = args[0];
        String command = "";


        Socket s = new Socket(serverAddress, 9090);
        PrintWriter go = new PrintWriter(s.getOutputStream(), true);

        while (true) {
            DisplayMenu();
            command = input.nextLine();
            if (command.equals("exit")) {
                System.out.println("TERMINATING! GOOD BYE!");
                break;
            } else {
                go.println(command);
                System.out.println("Sent " + command);
                BufferedReader response = new BufferedReader(new InputStreamReader(s.getInputStream()));
                System.out.println("Recieved answer");
                String line;
                while ((line = response.readLine()) != null) {
                    String answer = line;
                    System.out.println(answer);
                }
            }
        }
        System.exit(0);
    }


    static void DisplayMenu() {
        //Displays the Client Menu
        System.out.println("Welcome to Team 5's Client Application Demo!");
        System.out.println("Please select an option from the menu:");
        System.out.println("1) Get the host's current date and time.");

    }

}
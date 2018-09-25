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

        //Create Server Connection
        Socket s = new Socket(serverAddress, 9090);
        PrintWriter message = new PrintWriter(s.getOutputStream(), true);
        BufferedReader response = new BufferedReader(new InputStreamReader(s.getInputStream()));

        while (true) {
            DisplayMenu();
            command = input.nextLine();
            if (command.matches("^[1-6]$")){//Entered ONLY 1-6

                System.out.printf("Matched input. Sending %s to server.\n", command);
                message.println(command);
                System.out.println(response.readLine());

            }
            else if (command.matches("^7$")){

                System.out.prinf("Matched input %s. Exiting.", command);
                break;

            }
            System.out.printf("%s is an invalid input.\n", command);
        }

        response.close();
        message.close();
        s.close();

        return;
    }


    static void DisplayMenu() {
        //Displays the Client Menu
        System.out.println("Welcome to Team 5's Client Application Demo!");
        System.out.println("Please select an option from the menu:");
        System.out.println("1) Get the host's current date and time.");
        System.out.println("2) Get the host's uptime.");
        System.out.println("3) Get the host's memory usage.");
        System.out.println("4) Run Netstat on the host.");
        System.out.println("5) List the host's current users.");
        System.out.println("6) List the host's running processes.");
        System.out.println("7) Exit the program.\n");
        System.out.println("Enter a number from 1 to 7 to make your choice:\n")
    }

}
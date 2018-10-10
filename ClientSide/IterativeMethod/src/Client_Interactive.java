/*                                                                                          Students part of project: Stephen Sisley
                                                                                                                       Honya Elfayoumy
                                                                                                                       Richard Cowans
                                                                Documentation
Hello Professor,
    This file contains the client-side code for the interactive client-server demo. It requires the IP address
    of the server as a runtime argument, and creates a single connection with the remote server. During this
    connection, the client displays a menu to the user and waits for input. If the input is in the range of 1-6,
    the client sends the command to the server. If the input is 7, the client tells the server it is finished,
    and closes the connection. A hidden command also allows the client to send an "Exit Server" command, so that the
    server may close gracefully.

Many Thanks,
Group Three: Stephen,Honya,Richard

*/

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;


public class Client_Interactive {

    public static void main(String[] args) throws IOException {

        Scanner input = new Scanner(System.in);

        //Get Server Address
        if (args.length == 0) {//No address given
            //Print error message then exit.
            System.out.print("ERROR: No Server Address Specified. Press Enter to Exit.");
            input.nextLine();
            return;
        }
        String serverAddress = args[0];
        String command;

        //Create Server Connection
        Socket s = new Socket(serverAddress, 9090);
        PrintWriter message = new PrintWriter(s.getOutputStream(), true);
        BufferedReader response = new BufferedReader(new InputStreamReader(s.getInputStream()));
        String text;


        System.out.println("Welcome to Team 3's Client Application Demo!");

        while (true) {

            DisplayMenu();
            command = input.nextLine();

            //Parse Command. 1-6 for normal operation, 7 to close the client connection
            //8 tells the server to exit (hidden command).
            if (command.matches("^[1-6]$")) {//Entered ONLY 1-6

                //DEBUG
                //System.out.printf("Matched input. Sending %s to server.\n", command);
                message.println(command);

                //Read response until server sends "END" of message
                boolean endToken = true;
                while (endToken) {
                    text = response.readLine();
                    if (text.equals("END") || text.equals("END\n")) {
                        endToken = false;
                    } else {
                        System.out.println(text);
                    }
                }

                //System.out.println("Finished\n");
                System.out.println();
                continue;

            } else if (command.matches("^7$") || (command.matches("^8$"))) { // Matched exit or server exit command

                System.out.printf("Matched input %s. Exiting.\n", command);
                message.println(command);
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
        System.out.println("Please select an option from the menu:");
        System.out.println("1) Get the host's current date and time.");
        System.out.println("2) Get the host's uptime.");
        System.out.println("3) Get the host's memory usage.");
        System.out.println("4) Run Netstat on the host.");
        System.out.println("5) List the host's current users.");
        System.out.println("6) List the host's running processes.");
        System.out.println("7) Exit the program.\n");
        System.out.println("Enter a number from 1 to 7 to make your choice: ");
    }

}
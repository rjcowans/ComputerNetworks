import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Scanner;

class ClientThread extends Thread{
    //IO and attributes for each thread
    Socket clientSocket;
    PrintWriter out;
    BufferedReader in;
    String option;
    int reqNum;

    //Time attributes
    static ArrayList<Long> times = new ArrayList<Long>();
    long reqTime;

    //Client Thread Constuctor
    ClientThread(String hostName, int portNumber, String option, int i){
        try{
            this.clientSocket = new Socket(hostName, portNumber);
            this.out = new PrintWriter(clientSocket.getOutputStream(), true);
            this.in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            this.option = option;
            this.reqNum = i;
            this.reqTime = 0;
        }catch (UnknownHostException e){
            System.err.println("Dont know about host " + hostName);
            System.exit(1);
        }catch (IOException e){
            System.err.println("Couldn't get I/O for the connection to " + hostName);
            System.exit(1);
        }
    }

    //The code that the threads execute when started
    public void run(){
        String response;
        long startTime = System.currentTimeMillis();
        out.println(this.option); //menu option sent to server
        //Recieve the server's response
        try{
            while ((response = in.readLine()) != "END"){
                //Uncomment the below print statement to view the data on your screen
                response = null;
                //System.out.println(response); //reading from socket
            }
            this.in.close();
            this.out.close();
            this.clientSocket.close();
        }catch (IOException e){
            System.err.println("I/O error with the connection");
            System.exit(1);
        }
        //calculate the time it took and add it to the times list
        this.reqTime = (System.currentTimeMillis() - startTime);
        times.add(this.reqTime);
    }
}


public class Client_Interactive {

    /**
     * Interactive Client for Demo Purposes
     * TODO: Fill Out Correctly
     */
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
        //Socket s = new Socket(serverAddress, 9090);
        //PrintWriter message = new PrintWriter(s.getOutputStream(), true);
        //BufferedReader response = new BufferedReader(new InputStreamReader(s.getInputStream()));
        //String text;

        int clients;

        ClientThread[] threads = new ClientThread[100];

        System.out.println("Welcome to Team 3's Client Application Demo!");
        char option = 'a';

        while ((option < '1') || (option > '7')){
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
            System.out.println("Please enter a number between 1 - 7");
            option = input.next().charAt(0);
        }
        String opts = Character.toString(option);

        clients = 1;
            /*
            while (true) {
                //DisplayMenu();
                command = input.nextLine();
                if (command.matches("^[1-6]$")){//Entered ONLY 1-6

                    System.out.printf("Matched input. Sending %s to server.\n", command);
                    message.println(command);

                    boolean endToken = true;
                    while(endToken){
                        text = response.readLine();
                        if(text.equals("END") || text.equals("END\n")) {
                            endToken = false;
                        }
                        else {
                            System.out.println(text);
                        }
                    }

                    System.out.println("Finished\n");
                    continue;
                }
                else if (command.matches("^7$")){

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
            */
        //create the threads
        for (int i = 0;i < clients;i++){
            threads[i] = new ClientThread(serverAddress,9090,opts,i);
        }
        //run the threads
        for (int j = 0;j < clients;j++){
            threads[j].run();
        }

        //join after all threads have started so that the program waits for all of them to finish
        for (int k = 0; k < clients; k++){
            try{
                threads[k].join();
            }catch (InterruptedException ie){
                System.out.println(ie.getMessage());
            }
        }
        //calculate the average server response time
        long sumOfTimes = 0;
        for(long x: ClientThread.times){
            sumOfTimes += x;
        }
        double avgTime = sumOfTimes / (double)clients;
        ClientThread.times.clear();
        System.out.println("Average time of response = " + avgTime + "ms\n");

    }
}



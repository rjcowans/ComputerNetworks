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
    long threadTime;

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
            this.threadTime = 0;
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
        this.threadTime = System.currentTimeMillis();
        out.println(this.option); //menu option sent to server
        //Recieve the server's response
        try{
            while (!((response = in.readLine()).equals("END"))){
                //Uncomment the below print statement to view the data on your screen
                response = null;
                //System.out.println(response); //reading from socket
            }
            //Calculate total time for send & response
            this.reqTime = (System.currentTimeMillis() - this.threadTime);
            out.println("7");
            this.in.close();
            this.out.close();
            this.clientSocket.close();
        }catch (IOException e){
            System.err.println("I/O error with the connection");
            System.exit(1);
        }
        //Debugging
        //System.out.printf("Thread %s took %sms\n", this.reqNum, this.reqTime);
        times.add(this.reqTime);
    }
}


public class Client_Threading {

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
        }if (args.length != 3) { //Need both address and # of clients and load type to connect with
            //Print error then exit.
            System.out.print("ERROR: Argument Count Mismatch. Press Enter to Exit.");
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

        int clients = Integer.parseInt(args[1]);

        ClientThread[] threads = new ClientThread[100];

        System.out.println("Welcome to Team 3's Client Application Demo!");
        String option = args[2];

       /* while ((option < '1') || (option > '7')){
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
        String opts = Character.toString(option); */

        //create the threads
        for (int i = 0;i < clients;i++){
            threads[i] = new ClientThread(serverAddress,9090,option,i);
        }
        //Start the threads
        for (int j = 0;j < clients;j++){
            threads[j].start();
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

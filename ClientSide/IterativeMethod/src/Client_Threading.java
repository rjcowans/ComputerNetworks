/*                                                                                          Students apart of project: Stephen Sisley
                                                                                                                       Honya Elfayoumy
                                                                                                                       Richard Cowans
                                                                Documentation

    This program contains the client-side code for the non-interactive, multi-threaded portion of the project. It requires the IP
    address, number of clients to create, and load type (Date/1 or NetStat/4) as run arguments. It then creates the
    specified number of threads and runs through each thread in a second loop to start the threads. Each thread takes
    a timestamp just before it sends its request to the server and another just after it receives the "END" token and
    adds the difference calculated to a timeArray, which we then loop through to calculate the mean response time.
    A separate bash script is used to quickly and easily run through each of the combinations of client size and load
    type to generate the graphs for our project.

Many Thanks,
Group Three: Stephen,Honya,Richard

*/

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Scanner;


public class Client_Threading {

    public static void main(String[] args) throws IOException {

        Scanner input = new Scanner(System.in);

        if (args.length != 3) { //Needs IP address, # of clients, and load type to connect with
            //Print error then exit.
            System.out.print("ERROR: Argument Count Mismatch. Press Enter to Exit.");
            input.nextLine();
            return;
        }

        String serverAddress = args[0]; //IP Address
        int clients = Integer.parseInt(args[1]); //# Of Clients to Connect With
        String option = args[2]; //The load type: 1 for Date&Time, 4 for NetStat.

        ClientThread[] threads = new ClientThread[clients];

        //create the threads
        for (int i = 0; i < clients; i++) {
            threads[i] = new ClientThread(serverAddress, 9090, option, i);
        }
        //Start the threads
        for (int j = 0; j < clients; j++) {
            threads[j].start();
        }
        //join after all threads have started so that the program waits for all of them to finish
        for (int k = 0; k < clients; k++) {
            try {
                threads[k].join();
            } catch (InterruptedException ie) {
                System.out.println(ie.getMessage());
            }
        }
        //calculate the average server response time

        long sumOfTimes = 0;
        for (long x : ClientThread.times) {
            sumOfTimes += x;
        }
        double avgTime = sumOfTimes / (double) clients;
        ClientThread.times.clear();
        System.out.println("Average time of response for " + clients + " clients is = " + avgTime + "ms\n");
    }
}


class ClientThread extends Thread {

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

    //Client Thread Constructor
    ClientThread(String hostName, int portNumber, String option, int i) {
        try {
            this.clientSocket = new Socket(hostName, portNumber);
            this.out = new PrintWriter(clientSocket.getOutputStream(), true);
            this.in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            this.option = option;
            this.reqNum = i;
            this.reqTime = 0;
            this.threadTime = 0;
        } catch (UnknownHostException e) {
            System.err.println("Dont know about host " + hostName);
            System.exit(1);
        } catch (IOException e) {
            System.err.println("Couldn't get I/O for the connection to " + hostName);
            System.exit(1);
        }
    }

    //The code that the threads execute when started
    public void run() {

        this.threadTime = System.currentTimeMillis();
        out.println(this.option); //menu option sent to server

        //Receive the server's response
        try {
            while (!((in.readLine()).equals("END"))) {
                //Read server's output but don't do anything with it.
            }
            //Calculate total time for send & response
            this.reqTime = (System.currentTimeMillis() - this.threadTime);

            //Close and clean up the connection
            out.println("7");
            this.in.close();
            this.out.close();
            this.clientSocket.close();

        } catch (IOException e) {
            System.err.println("I/O error with the connection");
            System.exit(1);
        }

        //Debugging
        //System.out.printf("Thread %s took %sms\n", this.reqNum, this.reqTime);

        //Record this thread's response time
        times.add(this.reqTime);
    }
}




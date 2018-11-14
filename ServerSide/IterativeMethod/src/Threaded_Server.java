/*                                                                              Project Made By:Stephen Sisiley
                                                                                                Honya Elfayoumy
                                                                                                Richard Cowans
                                                                Documentation
This is our Threaded server that runs on a concurrent basis instead of iterative basis. We decided to move the connection
the server makes to the thread class that way everytime it connect to a instance from the client it spawns a new child. We
expect to see drastic change in the average time compared to our Iterative method. The backend was the only part of the project
that should have changed so when the result are show this will be a good use of comparision.
*/

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.atomic.AtomicInteger;

public class Threaded_Server {

    public static void main(String[] args) throws IOException {

        //Start our Server
        ServerSocket listener = new ServerSocket(9090);
        System.out.println("Opening port " + listener.getLocalPort() + " and waiting on an IP address");
        boolean startServer = true;
        while (startServer) {

            //Listen for and process client Connections concurrently
            System.out.println("Waiting on an IP address");
            Socket socket = listener.accept();
            System.out.println("Established Connection with a socket of the address " + socket.getInetAddress());
            System.out.print("Attempting to pass socket off to a thread...");

            try {
                new connectionThread(socket).start();
                System.out.print("success.\n");
            } catch (Exception e) {
                System.out.print("failed.\n");
                e.printStackTrace();
            }
        }
        //Stop Server
        listener.close();
    }
}

class connectionThread extends Thread {

    Socket clientSocket;
    BufferedReader clientInput;
    PrintWriter serverOutput;
    Stats1 backend;

    //This is for numbering the threads out of interest.
    //Tests both with and without this feature showed no appreciable difference in execution times
    static AtomicInteger count = new AtomicInteger(0);
    int threadNumber;

    public connectionThread(Socket connection) {

        clientSocket = connection;
        try {
            clientInput = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            serverOutput = new PrintWriter(connection.getOutputStream(), true);
            backend = new Stats1(serverOutput);
        } catch (IOException e) {
            System.err.println("Error creating I/O for" + connection.getLocalAddress());
        }

        threadNumber = count.incrementAndGet();

    }

    public void run() { //Code is nearly identical to the iterative server, but placed in the run() method here

        boolean keepConnected = true;

        while (keepConnected) {
            try {
                System.out.printf("Thread %d is attempting to read input from client.\n", threadNumber);
                String intake = clientInput.readLine();
                //System.out.printf("Input: %s\n", intake);
                int command = Integer.parseInt(intake);

                switch (command) {
                    case 1:
                        backend.Date();
                        break;
                    case 2:
                        backend.Time();
                        break;
                    case 3:
                        backend.Mem();
                        break;
                    case 4:
                        backend.NetStat();
                        break;
                    case 5:
                        backend.who();
                        break;
                    case 6:
                        backend.runs();
                        break;
                    case 7:
                        System.out.println("Client sends exit.");
                        keepConnected = false;
                        break;
                    default:
                        serverOutput.println("Improper entry. Try again");
                        System.out.println("Improper entry. Try again.");
                        break;
                }//End of Switch

            } catch (NumberFormatException e) {
                //Prevent Server Crashing if Client unexpectedly exits
                System.out.println("Connection interrupted: ending.");
                keepConnected = false;
                break;
            } catch (IOException e) {
                System.out.println("I/O Error, aborting connection.");
                keepConnected = false;
                break;
            }

        }//End of while

        //Connection ended, try to clean up our I/O
        try {
            clientInput.close();
            serverOutput.close();
            clientSocket.close();
            System.out.printf("Thread %d has finished.", threadNumber);
        } catch (IOException e) {
            System.err.println("Error closing socket IO in thread" + threadNumber);
        }
    }//End of run method

}//End of connectionThread class

class Stats1 {   //Runs the requested programs

    PrintWriter out;

    //Constructor takes the Socket's OutputWriter
    public Stats1(PrintWriter out) {
        this.out = out;
    }

    public void Date() throws IOException {
        String date = "";
        Process uptimeDate = Runtime.getRuntime().exec("date");
        BufferedReader in = new BufferedReader(new InputStreamReader(uptimeDate.getInputStream()));
        date = in.readLine();
        in.close();
        out.println(date);
        out.println("END");
        System.out.println("Sent DATE to client.");

        return;
    }

    public void Time() throws IOException {
        String uptime = "";
        //This prints the uptime
        Process uptimeTime = Runtime.getRuntime().exec("uptime");
        BufferedReader in = new BufferedReader(new InputStreamReader(uptimeTime.getInputStream()));
        uptime = in.readLine();
        in.close();
        out.println(uptime);
        out.println("END");
        System.out.println("Sent TIME to client.");

        return;
    }

    public void Mem() throws IOException {
        //This print the memory usage
        String headerUsage = "";
        String usage = "";
        Process memStat = Runtime.getRuntime().exec("free");
        BufferedReader in_2 = new BufferedReader(new InputStreamReader(memStat.getInputStream()));
        String line_2;
        while ((line_2 = in_2.readLine()) != null) {
            out.println(line_2);
        }
        //out.println(headerUsage);
        //out.println(usage);
        out.println("END");

        in_2.close();
        System.out.println("Sent MEMSTAT to client.");

        return;
    }

    public void NetStat() throws IOException {
        //This prints back the netstat
        Process uptimeNetstat = Runtime.getRuntime().exec("netstat -an ");
        BufferedReader in_2 = new BufferedReader(new InputStreamReader(uptimeNetstat.getInputStream()));
        String line_3;
        while ((line_3 = in_2.readLine()) != null) {
            out.println(line_3);
        }

        out.println("END");
        in_2.close();
        System.out.println("Sent NETSTAT to client.");

        return;
    }

    public void who() throws IOException {
        //This prints back the current user
        Process uptimeUsers = Runtime.getRuntime().exec("who");
        BufferedReader in_4 = new BufferedReader(new InputStreamReader(uptimeUsers.getInputStream()));
        String line_4;
        while ((line_4 = in_4.readLine()) != null) {
            out.println(line_4);
        }

        out.println("END");
        in_4.close();
        System.out.println("Sent WHO to client.");

        return;
    }

    public void runs() throws IOException {
        //This shows the process that are running
        Process uptimeCurrentProcess = Runtime.getRuntime().exec("ps -e");
        BufferedReader in_6 = new BufferedReader(new InputStreamReader(uptimeCurrentProcess.getInputStream()));
        String line_5;
        while ((line_5 = in_6.readLine()) != null) {
            out.println(line_5);
        }

        out.println("END");
        in_6.close();
        System.out.println("Sent RUNTIME to client.");

        return;
    }

}

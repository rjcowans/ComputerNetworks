/*                                                                                          Students apart of project: Stephen Sisley
                                                                                                                       Honya Elfayoumy
                                                                                                                       Richard Cowans
                                                                Documentation
Hello Professor, 
    Quick documetation on what is going on in the this part of the code. This file is simulating the server side of a iterative sever.
It waits for connection to a socket and then waits for commands.  Once a command is reciveved it then performs the function linked
to the command, which are of the following: Uptime, Date,Memory, users, process,netstat,exit. We also added a hidden command,
for the server that shuts it down from the client position. This server is running in Iterative, which means that there is a 1 by 1
accepting of each client. The OS system running it the Server is Linux so have to run Linux type command to get the output we desired.
BufferedReader is used to taken in input from the Client and PrintWriter is used to send back. Please Enjoy the code!!!

Many Thanks,
Group Three: Stephen,Honya,Richard

*/
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.text.ParseException;

public class ServerSideLinux {


    public static void main(String[] args) throws IOException, ParseException {

        //Start our Server
        ServerSocket listener = new ServerSocket(9090);
        System.out.println("Opening port " + listener.getLocalPort() + " and waiting on an IP address");
        boolean startServer = true;
        while (startServer) {

            //Listen for and process client Connections iteratively
            System.out.println("Waiting on an IP address");
            Socket socket = listener.accept();
            System.out.println("Established Connection with a socket of the address " + socket.getInetAddress());


            BufferedReader clientInput = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            PrintWriter serverOutput = new PrintWriter(socket.getOutputStream(), true);
            Stats backend = new Stats(serverOutput);

            //Keep connection alive until Client sends exit.
            boolean a = true;

            while (a) {
                try {
                    System.out.println("Attempting to read input from client.");
                    String intake = clientInput.readLine();
                    System.out.printf("Input: %s\n", intake);
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
                            a = false;
                            break;
                        case 8:
                            System.out.println("Client request for shutdown.....\n Shutting Down");
                            startServer = false;
                            a = false;
                            break;
                        default:
                            serverOutput.println("Improper entry. Try again");
                            System.out.println("Improper entry. Try again.");
                            break;
                    }
                } catch (NumberFormatException e) {
                    //Prevent Server Crashing if Client unexpectedly exits
                    System.out.println("Connection interupted ending");
                    break;
                }
            }
            //Close Client Connection
            socket.close();
            System.out.println("Socket closed.");

        }
        //Stop Server
        listener.close();
    }
}

class Stats {   //Runs the requested programs

    PrintWriter out;

    //Constructor takes the Socket's OutputWriter
    public Stats(PrintWriter out) {
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
        Process memStat = Runtime.getRuntime().exec("free -m");
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

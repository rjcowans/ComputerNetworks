import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.text.ParseException;

public class ServerSideLinux_Threading{


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
                            Stats2 Date = new Stats2(serverOutput,1);
                            Date.start();
                            break;
                        case 2:
                            Stats2 Time = new Stats2(serverOutput,2);
                            Time.start();
                            break;
                        case 3:
                            Stats2 Mem = new Stats2(serverOutput,3);
                            Mem.start();
                            break;
                        case 4:
                            Stats2 NetStat = new Stats2(serverOutput,4);
                            NetStat.start();
                            break;
                        case 5:
                            Stats2 who = new Stats2(serverOutput,5);
                            who.start();
                            break;
                        case 6:
                            Stats2 proc = new Stats2(serverOutput,6);
                            proc.start();
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
            serverOutput.close();
        }
        //Stop Server
        listener.close();

    }
}

class Stats2 extends Thread {   //Runs the requested programs

    PrintWriter out;
    int command;
    //Constructor takes the Socket's OutputWriter
    public Stats2(PrintWriter out,int intake) {
        this.out = out;
        this.command = intake;
    }
    @Override
    public void run(){
        System.out.println("Spawning NewThread for command: " + command);
        try {
            switch (command) {
                case 1:
                    this.Date();
                    break;
                case 2:
                    this.Time();
                    break;
                case 3:
                    this.Mem();
                    break;
                case 4:
                    this.NetStat();
                    break;
                case 5:
                    this.who();
                    break;
                case 6:
                    this.process();
                    break;
            }
        }catch(IOException e){
            System.out.println("OOPS it DID NOT LIKE THAT");
        }
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

    }

    public void Mem() throws IOException {
        //This print the memory usage
        Process memStat = Runtime.getRuntime().exec("free");
        BufferedReader in_2 = new BufferedReader(new InputStreamReader(memStat.getInputStream()));
        String line_2;
        while ((line_2 = in_2.readLine()) != null) {
            out.println(line_2);
        }
        out.println("END");

        in_2.close();
        System.out.println("Sent MEMSTAT to client.");

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

    }

    public void process() throws IOException {
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

    }

}
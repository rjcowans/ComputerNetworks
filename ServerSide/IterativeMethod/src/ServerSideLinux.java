package testfile;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.text.ParseException;

import static java.lang.System.out;

public class ServerSideLinux {
    public static String sender = "null";
    public static String[] BigSender;
    public static int  amount;
    public static void main(String[] args) throws IOException, ParseException {
        Process uptimeDate = Runtime.getRuntime().exec("date");
        BufferedReader in2 = new BufferedReader(new InputStreamReader(uptimeDate.getInputStream()));
        String date = in2.readLine();
        String dateHolder[] = date.split(" ");
        File Logging = new File("./" + dateHolder[1] + "_" + dateHolder[2]);
        BufferedWriter output = new BufferedWriter(new FileWriter(Logging));
        ServerSocket listener = new ServerSocket(9090);
        out.println("Opening port " + listener.getLocalPort()  + "and  waiting on IP address of " + listener.getInetAddress() );
        try {
            while (true) {
                Socket socket = listener.accept();
                out.println("Estblished Connection with a socket of the address " + socket.getInetAddress());

                try {
                    out.println("Attempting to read input from port");
                    BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                    String intake = in.readLine();
                    int command = Integer.parseInt(intake);
                    Stats backend = new Stats();
                    switch (command){
                        case 1:
                            sender = backend.Date();
                            break;
                        case 2:
                            sender = backend.Time();
                            break;
                        case 3:
                            BigSender = backend.Mem();
                            break;
                        case 4:
                            BigSender = backend.NetStat();
                            break;
                        case 5:
                            BigSender = backend.who();
                            break;
                        case 6:
                            BigSender = backend.runs();
                            amount = BigSender.length;
                            break;
                        case 7:
                            sender = "Quiting program";
                            break;
                        default:
                            sender = "Improper Entry try again";
                    }
                    PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
                    if (!(sender.equals("null"))){
                        System.out.println(sender);
                        out.write(sender);
                        output.write(sender + "\n");
                    }
                    else{
                        System.out.println("Sending Info");
                        for (int i = 0; i < amount; i++){
                                System.out.println(BigSender[i]);
                                out.write(BigSender[i]);
                                output.write(BigSender[i] + "\n");
                        }
                    }
                } finally {
                    socket.close();
                    output.close();
                }
            }
        } finally {
            listener.close();
        }

    }
}

class Stats {

    private String uptime;
    String[] Memoray;
    private String usage;
    private String headerUsage;
    String[] ps;
    String[] users;
    String[] netStuff;
    String date;

    public Stats() throws IOException{
        String uptime = "";
        String usage = "";
        String headerUsage = "";
        String[] Mem = new String[1];
    }

    protected  String Date() throws IOException{
        Process uptimeDate = Runtime.getRuntime().exec("date");
        BufferedReader in = new BufferedReader(new InputStreamReader(uptimeDate.getInputStream()));
        date = in.readLine();
        return date;
    }

    protected String Time() throws IOException{
        //This prints the uptime
        Process uptimeTime = Runtime.getRuntime().exec("uptime");
        BufferedReader in = new BufferedReader(new InputStreamReader(uptimeTime.getInputStream()));
        uptime = in.readLine();
        return uptime;
    }

    protected String[] Mem() throws IOException {
        //This print the memory usage
        Process uptimeUsage = Runtime.getRuntime().exec("free -m");
        BufferedReader in_2 = new BufferedReader(new InputStreamReader(uptimeUsage.getInputStream()));
        String line_2;
        while ((line_2 = in_2.readLine()) != null) {
            if (line_2.startsWith("             total")) {
                headerUsage = line_2;
            }
            if (line_2.startsWith("Mem")) {
                usage = line_2;
                break;
            }
        }
        out.println(headerUsage);
        out.println(usage);
        Memoray[0] = headerUsage;
        Memoray[1] = usage;
        return Memoray;
    }

    protected String[] NetStat() throws IOException {
//This prints back the netstat
        Process uptimeNetstat = Runtime.getRuntime().exec("netstat -an ");
        BufferedReader in_2 = new BufferedReader(new InputStreamReader(uptimeNetstat.getInputStream()));
        BufferedReader in_3 = new BufferedReader(new InputStreamReader(uptimeNetstat.getInputStream()));
        String line_3;
        int i = 0;
        while ((line_3 = in_2.readLine()) != null) {
            out.println(line_3);
            i++;
        }

        netStuff = new String[i];
        i = 0;
        while ((line_3 = in_3.readLine()) != null){
            netStuff[i]=line_3;
            i++;
        }
        return netStuff;
    }

    protected String[] who() throws IOException {
        //This prints bacl the current user
        Process uptimeUsers = Runtime.getRuntime().exec("who");
        BufferedReader in_4 = new BufferedReader(new InputStreamReader(uptimeUsers.getInputStream()));
        BufferedReader in_5 = new BufferedReader(new InputStreamReader(uptimeUsers.getInputStream()));
        String line_4;
        int i = 0;
        while ((line_4 = in_4.readLine()) != null) {
            i++;
        }
        users = new String[i];
        i = 0;
        while ((line_4 = in_5.readLine()) != null){
            users[i]=line_4;
            i++;
        }
        return users;
    }
    protected String[] runs() throws IOException{
        //This show the process that are running
        Process uptimeCurrentProcess = Runtime.getRuntime().exec("ps");
        BufferedReader in_6 = new BufferedReader(new InputStreamReader(uptimeCurrentProcess.getInputStream()));
        BufferedReader in_7 = new BufferedReader(new InputStreamReader(uptimeCurrentProcess.getInputStream()));
        String line_5;
        int i = 0;
        while ((line_5 = in_6.readLine()) != null) {
            out.println(line_5);
            i++;
        }
        ps = new String[i];
        i = 0;
        while ((line_5 = in_7.readLine()) != null){
            ps[i]=line_5;
            i++;
        }
        return ps;
    }
}



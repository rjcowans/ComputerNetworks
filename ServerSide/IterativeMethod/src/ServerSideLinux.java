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
        in2.close();
        String dateHolder[] = date.split(" ");
        File Logging = new File("./" + dateHolder[1] + "_" + dateHolder[2]);
        BufferedWriter output = new BufferedWriter(new FileWriter(Logging));
        ServerSocket listener = new ServerSocket(9090);
        System.out.println("Opening port " + listener.getLocalPort()  + "and  waiting on IP address");
        boolean a = true;
            while (a != false) {
                Socket socket = listener.accept();
                System.out.println("Estblished Connection with a socket of the address " + socket.getInetAddress());
                BufferedReader toy = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                    System.out.println("Attempting to read input from port");
                    String intake = toy.readLine();
                    System.out.println(intake);
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
                            amount = BigSender.length;
                            break;
                        case 4:
                            BigSender = backend.NetStat();
                            amount = BigSender.length;
                            break;
                        case 5:
                            BigSender = backend.who();
                            amount = BigSender.length;
                            break;
                        case 6:
                            BigSender = backend.runs();
                            amount = BigSender.length;
                            break;
                        case 7:
                            sender = "Quiting program";
                            a = false;
                            break;
                        default:
                            sender = "Improper Entry try again";
                    }
                PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
                    if (command == 3 || command == 4 || command == 5 || command == 6){
                        System.out.println("Sending Info");
                        for (int i = 0; i < amount; i++){
                                //System.out.println(BigSender[i]);
                                out.println(BigSender[i]);
                                output.write(BigSender[i] + "\n");
                        }
                    }
                  else{
                        System.out.println(sender);
                        out.println(sender);
                        output.write(sender + "\n");
                        sender = "null";

}
            socket.close();

        }
            listener.close();
            output.close();
    }
}

class Stats {

    public String uptime;
    public String[] Memoray = new String[2];
    public String usage;
    public String headerUsage;
    public String[] netStuff;
    public String[] users;
    public String[] ps;

    public String date;

    public Stats() throws IOException{
        String uptime = "";
        String usage = "";
        String headerUsage = "";
        String[] Mem = new String[2];
    }

    public  String Date() throws IOException{
        Process uptimeDate = Runtime.getRuntime().exec("date");
        BufferedReader in = new BufferedReader(new InputStreamReader(uptimeDate.getInputStream()));
        date = in.readLine();
        in.close();
        return date;
    }

    public String Time() throws IOException{
        //This prints the uptime
        Process uptimeTime = Runtime.getRuntime().exec("uptime");
        BufferedReader in = new BufferedReader(new InputStreamReader(uptimeTime.getInputStream()));
        uptime = in.readLine();
        in.close();
        return uptime;
    }

    public String[] Mem() throws IOException {
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
        in_2.close();
        return Memoray;
    }

    public String[] NetStat() throws IOException {
//This prints back the netstat
        Process uptimeNetstat = Runtime.getRuntime().exec("netstat -an ");
        BufferedReader in_2 = new BufferedReader(new InputStreamReader(uptimeNetstat.getInputStream()));
        BufferedReader in_3 = new BufferedReader(new InputStreamReader(uptimeNetstat.getInputStream()));
        String line_3;
        String line_4;
        int i = 0;
        while ((line_3 = in_2.readLine()) != null) {
            System.out.println(line_3);
            i++;
        }

        netStuff = new String[i];
        i = 0;
        while ((line_4 = in_3.readLine()) != null){
            netStuff[i]=line_4;
            i++;
        }
        in_2.close();in_3.close();
        return netStuff;
    }

    public String[] who() throws IOException {
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
        in_5.close();in_4.close();
        return users;
    }
    public String[] runs() throws IOException{
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


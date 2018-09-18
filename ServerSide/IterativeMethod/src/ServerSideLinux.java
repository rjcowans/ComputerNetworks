package testfile;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.text.ParseException;
import java.util.ArrayList;

import static java.lang.System.out;

public class ServerSideLinux {
    public static String sender = "null";
    public static void main(String[] args) throws IOException, ParseException {
        ArrayList<String> BigSender = new ArrayList<String>();
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
        while (a) {
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
                    break;
                case 4:
                    BigSender = backend.NetStat();
                    break;
                case 5:
                    BigSender = backend.who();
                    break;
                case 6:
                    BigSender = backend.runs();
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
                for (int i = 0; i < BigSender.size(); i++){
                    //System.out.println(BigSender[i]);
                    out.println(BigSender.get(i));
                    output.write(BigSender.get(i) + "\n");
                }
            }
            else{
                System.out.println(sender);
                out.println(sender);
                output.write(sender + "\n");
                sender = "null";

            }
            backend.EmptyList();
            socket.close();

        }
        listener.close();
        output.close();
    }
}

class Stats {
    public ArrayList<String> BigSender = new ArrayList<String>();

    public void EmptyList(){
            BigSender.clear();
    }
    

    public  String Date() throws IOException{
        String date = "";
        Process uptimeDate = Runtime.getRuntime().exec("date");
        BufferedReader in = new BufferedReader(new InputStreamReader(uptimeDate.getInputStream()));
        date = in.readLine();
        in.close();
        return date;
    }

    public String Time() throws IOException{
        String uptime = "";
        //This prints the uptime
        Process uptimeTime = Runtime.getRuntime().exec("uptime");
        BufferedReader in = new BufferedReader(new InputStreamReader(uptimeTime.getInputStream()));
        uptime = in.readLine();
        in.close();
        return uptime;
    }

    public ArrayList<String> Mem() throws IOException {
        //This print the memory usage
        String headerUsage = "";
        String usage = "";
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
        BigSender.add(headerUsage);
        BigSender.add(usage);
        in_2.close();
        return BigSender;
    }

    public ArrayList<String> NetStat() throws IOException {
//This prints back the netstat
        Process uptimeNetstat = Runtime.getRuntime().exec("netstat -an ");
        BufferedReader in_2 = new BufferedReader(new InputStreamReader(uptimeNetstat.getInputStream()));
        String line_3;
        while ((line_3 = in_2.readLine()) != null){
            BigSender.add(line_3);
        }
        in_2.close();
        return BigSender;
    }

    public ArrayList<String> who() throws IOException {
        //This prints bacl the current user
        Process uptimeUsers = Runtime.getRuntime().exec("who");
        BufferedReader in_4 = new BufferedReader(new InputStreamReader(uptimeUsers.getInputStream()));
        String line_4;
        while ((line_4 = in_4.readLine()) != null){
            BigSender.add(line_4);
        }
        in_4.close();
        return BigSender;
    }
    public ArrayList<String> runs() throws IOException{
        //This show the process that are running
        Process uptimeCurrentProcess = Runtime.getRuntime().exec("ps");
        BufferedReader in_6 = new BufferedReader(new InputStreamReader(uptimeCurrentProcess.getInputStream()));
        String line_5;
        while ((line_5 = in_6.readLine()) != null) {
            BigSender.add(line_5);
        }
        return BigSender;
    }
}

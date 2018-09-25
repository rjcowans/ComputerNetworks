import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.text.ParseException;
import java.util.ArrayList;

public class ServerSideLinux {
    public static String sender = "null";
    public static void main(String[] args) throws IOException, ParseException {
        Process uptimeDate = Runtime.getRuntime().exec("date");
        BufferedReader in2 = new BufferedReader(new InputStreamReader(uptimeDate.getInputStream()));
        String date = in2.readLine();
        in2.close();
        String dateHolder[] = date.split(" ");
        File Logging = new File("./" + dateHolder[1] + "_" + dateHolder[2]);
        BufferedWriter output = new BufferedWriter(new FileWriter(Logging));
        ServerSocket listener = new ServerSocket(9090);
        System.out.println("Opening port " + listener.getLocalPort()  + " and waiting on IP address");
        Socket socket = listener.accept();
        System.out.println("Established Connection with a socket of the address " + socket.getInetAddress());
        BufferedReader toy = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
        Stats backend = new Stats(out);
        boolean a = true;
        while (a) {

            System.out.println("Attempting to read input from port");
            String intake = toy.readLine();
            System.out.printf("Input: %s\n", intake);
            int command = Integer.parseInt(intake);

            switch (command){
                case 1:
                    sender = backend.Date();
                    break;
                case 2:
                    sender = backend.Time();
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
                    sender = "Quiting program";
                    a = false;
                    break;
                default:
                    sender = "Improper Entry try again";
            }
            if (command <=2){
                System.out.println(sender);
                out.println(sender);
                output.write(sender + "\n");
                sender = "null";
            }

            backend.EmptyList();

        }
        socket.close();
        listener.close();
        output.close();
    }
}

class Stats {
    public ArrayList<String> BigSender = new ArrayList<String>();
    PrintWriter out;


    public Stats (PrintWriter out){
        this.out = out;
    }

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

    public void Mem() throws IOException {
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
    }

    public void NetStat() throws IOException {
        //This prints back the netstat
        Process uptimeNetstat = Runtime.getRuntime().exec("netstat -an ");
        BufferedReader in_2 = new BufferedReader(new InputStreamReader(uptimeNetstat.getInputStream()));
        String line_3;
        while ((line_3 = in_2.readLine()) != null){
            BigSender.add(line_3);
            out.println(line_3);
        }
        in_2.close();
        return;
    }

    public void who() throws IOException {
        //This prints back the current user
        Process uptimeUsers = Runtime.getRuntime().exec("who");
        BufferedReader in_4 = new BufferedReader(new InputStreamReader(uptimeUsers.getInputStream()));
        String line_4;
        while ((line_4 = in_4.readLine()) != null){
            BigSender.add(line_4);
            out.println(line_4);
        }
        in_4.close();
        return;
    }
    public void runs() throws IOException{
        //This shows the process that are running
        Process uptimeCurrentProcess = Runtime.getRuntime().exec("ps");
        BufferedReader in_6 = new BufferedReader(new InputStreamReader(uptimeCurrentProcess.getInputStream()));
        String line_5;
        while ((line_5 = in_6.readLine()) != null) {
            BigSender.add(line_5);
            out.println(line_5);
        }
        return;
    }
}

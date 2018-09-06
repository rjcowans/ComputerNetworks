//package testfile;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.InputStreamReader;
import java.text.ParseException;
import java.net.Socket;
import java.net.ServerSocket;
import java.util.Date;

public class ServerSideLinux {
    public static void main(String[] args) throws IOException, ParseException {
        
	String uptime = "";
        String usage = "";
        String headerUsage = "";

        //This prints the uptime
        Process uptimeTime = Runtime.getRuntime().exec("uptime");
        BufferedReader in = new BufferedReader(new InputStreamReader(uptimeTime.getInputStream()));
        String line;
        line = in.readLine();
        uptime = line;
        System.out.println(uptime);

        
	//This print the memory usage
        Process uptimeUsage = Runtime.getRuntime().exec("free -m");
        BufferedReader in_2 = new BufferedReader(new InputStreamReader(uptimeUsage.getInputStream()));
        String line_2;
        while ((line_2 = in_2.readLine()) != null) {
            if (line_2.startsWith("             total")){
                headerUsage = line_2;
            }
            if (line_2.startsWith("Mem")) {
                usage = line_2;
                break;
            }
        }
        System.out.println(headerUsage);
        System.out.println(usage);

        /**
	//This prints back the netstat
        Process uptimeNetstat = Runtime.getRuntime().exec("netstat -an ");
        BufferedReader in_3 = new BufferedReader(new InputStreamReader(uptimeNetstat.getInputStream()));
        String line_3;
        while ((line_3 = in_3.readLine()) != null) {
            System.out.println(line_3);
        }

        //This prints bacl the current user
        Process uptimeUsers = Runtime.getRuntime().exec("who");
        BufferedReader in_4 = new BufferedReader(new InputStreamReader(uptimeUsers.getInputStream()));
        String line_4;
        while ((line_4 = in_4.readLine()) != null) {
            System.out.println(line_4);
        }
        //This show the process that are running
        Process uptimeCurrentProcess = Runtime.getRuntime().exec("ps");
        BufferedReader in_5 = new BufferedReader(new InputStreamReader(uptimeCurrentProcess.getInputStream()));
        String line_5;
        while ((line_5 = in_5.readLine()) != null) {
            System.out.println(line_5);
        }
	**/
        
         //* Runs the server.


         ServerSocket listener = new ServerSocket(9090);
         try {
         while (true) {
         Socket socket = listener.accept();
         System.out.println("O Looks like we got something\n");
         try {
         PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
         String date = (new Date().toString());
         System.out.println("Sending back " + date + " to " + socket.getInetAddress());
         out.println(uptime);
	 out.println(headerUsage);
	 out.println(usage);
         } finally {
         socket.close();
         }
         }
         } finally {
         listener.close();
         }
    }
}

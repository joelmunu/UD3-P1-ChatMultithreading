package client;
import java.io.*;
import java.net.*;
import java.util.*;

public class Client {

    public static final String HOST = "localhost";

    public static final int PORT = 1234;


    public static boolean closed = false;

    public static void main(String[] args) throws IOException {

        Socket clientSocket = new Socket(HOST, PORT);
        System.out.println("Conectado al servidor " + HOST + ":" + PORT);

        BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
        PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);

        Scanner sc = new Scanner(System.in);

        System.out.print("Introduce tu nombre: ");
        String username = sc.nextLine();

        out.println(username);

        ServerReader serverReader = new ServerReader(in);

        serverReader.start();

        while (true) {
    

            String message = sc.nextLine();

            if (message.equals("bye")) {
                out.println(message);
                break;
            }

            if (!message.isEmpty()) {
                out.println(message);
            }
        }

        closed = true;
        
         System.out.println("Desconectado del servidor");
    }
}

class ServerReader extends Thread {

    private BufferedReader in;

    public ServerReader(BufferedReader in) {
        this.in = in;
    }

    public void run() {
       try {
           while (true) {
               if (!Client.closed) {

                   String message = in.readLine();

                   if (message.equals("goodbye")) {
                       break;
                   }

                   if (message != null) {

                       System.out.println(message);
                   }
               } else {
                   break;
               }
           }
       } catch (IOException e) {

           e.printStackTrace();
       }
   }
}
package server.threads;

import java.io.*;
import java.net.*;
import java.util.*;

import server.Server;

public class ClientHandler extends Thread {

    private Socket clientSocket;
    private String username;

    public ClientHandler(Socket clientSocket) {
        this.clientSocket = clientSocket;
    }

    public void run() {
        try {
            BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
        
            username = in.readLine();
            System.out.println("Usuario " + username + " conectado");

        
            out.println("Lista de mensajes:");

            for (String message : Server.messages) {
                out.println(message);
            }

            out.println("Fin de la lista");
        
            while (true) {
            
                String message = in.readLine();
            
                if (message.equals("bye")) {
                    out.println("goodbye");
                    break;
                }

                if (message.startsWith("message:")) {
                
                    String text = message.substring(8);
                
                    String time = String.format("[%tT]", new Date());

                
                    String formattedMessage = time + " <" + username + ">: " + text;
                
                    Server.messages.add(formattedMessage);

                    for (Socket socket : Server.clients) {
                        PrintWriter out2 = new PrintWriter(socket.getOutputStream(), true);
                        out2.println(formattedMessage);
                    }
                    
                } else {
                
                    out.println("Mensaje no válido");
                }
            }

            clientSocket.close(); // Cierre
            System.out.println("Usuario " + username + " desconectado");

            Server.clients.remove(clientSocket);

        } catch (IOException e) {
        
            e.printStackTrace();
        }
    }
}
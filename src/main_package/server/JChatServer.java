package main_package.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Vector;

public class JChatServer {
    private Vector<ClientHandler> clientsList;

    public JChatServer() {
        try {
            ServerSocket serverSocket = new ServerSocket(8189);

            while (true) {
                System.out.println("Waiting for client's to connect...");
                Socket socket = serverSocket.accept();
                new ClientHandler(socket);
            }


        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}


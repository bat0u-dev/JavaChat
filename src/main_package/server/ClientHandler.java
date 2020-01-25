package main_package.server;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class ClientHandler {
    private JChatServer server;
    private Socket clientSocket;
    private DataInputStream Input;
    private DataOutputStream Output;
    int clientID;


    public ClientHandler(JChatServer server, Socket clientSocket, int clientID) {
        try {
            this.server = server;
            this.clientSocket = clientSocket;
            this.clientID = clientID;
            this.Input = new DataInputStream(clientSocket.getInputStream());
            this.Output = new DataOutputStream(clientSocket.getOutputStream());
            new Thread(() -> {
                try {
                    while (true) {
                        String inputMsg = Input.readUTF();
                        System.out.println("Client " + this.clientID + " message :" + inputMsg);
                        if (inputMsg.equals("/end")) {
                            System.out.println("Client aborted the connection.");
                            Output.writeUTF("Connection has been aborted by client.");
                            break;
                        }
                        server.broadcastMessage("Client " + this.clientID + " : " + inputMsg);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                } finally {
                    try {
                        Input.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    try {
                        Output.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    try {
                        clientSocket.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    server.unsubscribe(this);
                }
            }).start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void sendMsg(String inputMsg) {
        try {
            Output.writeUTF(inputMsg);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}




package main_package.server;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class ClientHandler {
    Socket clientSocket;
    DataInputStream Input;
    DataOutputStream Output;

    public ClientHandler(Socket clientSocket) {
        try {
            this.clientSocket = clientSocket;
            this.Input = new DataInputStream(clientSocket.getInputStream());
            this.Output = new DataOutputStream(clientSocket.getOutputStream());

            new Thread(new Runnable() {
                @Override
                public void run() {
                    while(true){
                        try {
                            String inputMsg = Input.readUTF();
                            System.out.println("Client's message :" + inputMsg);
                            sendMsg("ECHO : " + inputMsg);
                            if(inputMsg.equals("/end")){
                                System.out.println("Client aborted the connection.");
                                Output.writeUTF("Connection has been aborted by client.");
                                break;
                            }
                        } catch (IOException e){
                            e.printStackTrace();
                        }finally {
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

                        }

                    }
                }
            }).start();
        }catch (IOException e){
            e.printStackTrace();
        }
    }

    public void sendMsg(String inputMsg){
        try {
            Output.writeUTF(inputMsg);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

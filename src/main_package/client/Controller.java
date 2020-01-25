package main_package.client;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.SocketException;
import java.net.URL;
import java.util.ResourceBundle;

public class Controller implements Initializable {
    @FXML
    TextArea textArea;

    @FXML
    TextField textField;

    Socket clientSocket;
    DataInputStream clientInput;
    DataOutputStream clientOutput;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        try {
            clientSocket = new Socket("127.0.0.1", 8189);
            System.out.println("Connection to server successful:)");
            clientInput = new DataInputStream(clientSocket.getInputStream());
            clientOutput = new DataOutputStream(clientSocket.getOutputStream());
            new Thread(() -> {
                try {
                    while (true) {
                        String inputMsg = clientInput.readUTF();
                        if (inputMsg.equals("")) continue;
                        textArea.appendText(inputMsg);
                        textArea.appendText("\n");
                        if (inputMsg.equals("Echo : /end")) {
                            textArea.appendText("Connection has been closed by your command.");
                            textArea.appendText("\n");
                            clientSocket.close();
                            break;
                        }
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }).start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void sendMessage() {
        try {
            String message = textField.getText().trim();
            if (message.equals("")) {
                System.out.println("\n");
                return;
            }
            clientOutput.writeUTF(message);
            textField.clear();
        } catch (IOException e) {
            textField.clear();
            System.out.println("Not connected to server. Socket has been closed. Restart the application!");
            textArea.appendText("Not connected to server. Socket has been closed. Restart the application!");
            textArea.appendText("\n");
        }

    }

    public void clearTextArea() {
        textArea.clear();
        textField.requestFocus();
    }
}

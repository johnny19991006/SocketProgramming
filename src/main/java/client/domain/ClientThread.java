package client.domain;


import client.view.ClientMultiGUI;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.Socket;

public class ClientThread extends Thread {
    Socket socket;
    ClientMultiGUI clientMultiGUI;

    public ClientThread(Socket socket, ClientMultiGUI clientMultiGUI) {
        this.socket = socket;
        this.clientMultiGUI = clientMultiGUI;
    }

    public void run() {
        try {
            InputStream inputStream = socket.getInputStream();
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
            BufferedReader reader = new BufferedReader(inputStreamReader);

            while (true) {
                String message = reader.readLine();
                if (message == null)
                    break;
                clientMultiGUI.append(message + "\n");
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
}

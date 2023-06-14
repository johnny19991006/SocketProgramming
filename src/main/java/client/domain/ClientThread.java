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
            InputStream is = socket.getInputStream();
            InputStreamReader isr = new InputStreamReader(is);
            BufferedReader reader = new BufferedReader(isr);

            while (true) {
                String str = reader.readLine();
                if (str == null)
                    break;
                clientMultiGUI.append(str + "\n");
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
}

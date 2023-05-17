package server.domain;

import server.view.ServerGUI;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class ServerMulti {
    public ServerMulti(int port, ServerGUI sc){
        ServerSocket serverSocket = null;
        try {
            serverSocket = new ServerSocket(port);
            while(true){
                //클라이언트 대기
                Socket socket = serverSocket.accept();
                Thread th = new server.domain.ClientThread(socket, sc);
                th.start();
            }
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
}


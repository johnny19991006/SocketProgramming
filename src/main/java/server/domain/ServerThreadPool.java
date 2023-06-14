package server.domain;

import server.view.ServerGUI;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ServerThreadPool {
    private final ServerInfo serverInfo;
    private final ServerGUI serverGUI;
    private final ExecutorService threadPool;

    public ServerThreadPool(ServerInfo serverInfo, ServerGUI serverGUI) {
        this.serverInfo = serverInfo;
        this.serverGUI = serverGUI;
        this.threadPool = Executors.newFixedThreadPool(10);
    }

    public void startServer() {
        try {
            ServerSocket serverSocket = new ServerSocket(serverInfo.getPort());
            serverGUI.chatList("서버 시작\n");

            while (true) {
                Socket socket = serverSocket.accept();
                threadPool.execute(new ServerThread(socket, serverGUI, serverInfo));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

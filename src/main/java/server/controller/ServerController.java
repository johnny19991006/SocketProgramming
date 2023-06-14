package server.controller;

import server.domain.ServerInfo;
import server.domain.ServerThreadPool;
import server.view.ServerGUI;

public class ServerController {
    public void run() {

        ServerInfo serverInfo = new ServerInfo();
        ServerGUI serverGUI = new ServerGUI(serverInfo);
        ServerThreadPool serverThreadPool = new ServerThreadPool(serverInfo, serverGUI);
        serverThreadPool.startServer();
    }
}

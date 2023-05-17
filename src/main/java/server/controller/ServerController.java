package server.controller;

import server.view.ServerGUI;


public class ServerController {

    public void run(){
        ServerGUI serverGUI = new ServerGUI(22222);
        serverGUI.startServer();
    }
}

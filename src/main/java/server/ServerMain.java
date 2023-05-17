package server;

import server.controller.ServerController;

public class ServerMain {
    public static void main(String[] args) {
        new ServerController().run();
    }
}

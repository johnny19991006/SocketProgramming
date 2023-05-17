package client.controller;

import client.view.ClientMultiGUI;

public class ClientController {
    public void run() {
        new ClientMultiGUI("127.0.0.1", 1500);
    }
}

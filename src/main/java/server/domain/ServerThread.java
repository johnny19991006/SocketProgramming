package server.domain;

import server.view.ServerGUI;

import javax.security.sasl.AuthenticationException;
import java.io.*;
import java.net.Socket;
import java.util.Vector;

import static server.utils.ErrorMessage.STRING_INVALID_PASSWORD;
import static server.utils.OutputMessage.*;

class ServerThread implements Runnable {
    static Vector<PrintWriter> list = new Vector<>();

    private Socket socket;
    private PrintWriter writer;
    private ServerGUI serverGUI;
    private ServerInfo serverInfo;

    public ServerThread(Socket socket, ServerGUI serverGUI,ServerInfo serverInfo) {
        this.socket = socket;
        this.serverGUI = serverGUI;
        this.serverInfo = serverInfo;
        PrintWriter tempWriter = null;
        try {
            tempWriter = new PrintWriter(socket.getOutputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
        writer = tempWriter;
        list.add(writer);
    }

    @Override
    public void run() {
        String name = null;
        String password = null;
        try (InputStream inputStream = socket.getInputStream();
             InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
             BufferedReader reader = new BufferedReader(inputStreamReader)) {

            name = reader.readLine();
            password = reader.readLine();

            validatePassword(name, password);
            sendAll("#" + name + STRING_LOGIN_MESSAGE.getMessage());
            addUser(name);

            while (true) {
                String str = reader.readLine();
                if (str == null)
                    break;
                sendAll("[" + name + "]" + str);
            }

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            list.remove(writer);
            sendAll("#" + name + STRING_LOGOUT_MESSAGE.getMessage());
            try {
                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    void validatePassword(String name, String password) throws AuthenticationException {
        if (!password.equals(serverInfo.getPassword())) {
            sendAll("#" + name + STRING_LOGIN_ERROR_MESSAGE.getMessage());
            throw new AuthenticationException(STRING_INVALID_PASSWORD.getMessage());
        }
    }

    private void sendAll(String str) {
        serverGUI.chatList(str + "\n");
        for (PrintWriter writer : list) {
            writer.println(str);
            writer.flush();
        }
    }

    private void addUser(String str) {
        serverGUI.updateUserList(str);
    }
}

package server.domain;

import server.view.ServerGUI;

import javax.security.sasl.AuthenticationException;
import java.io.*;
import java.net.Socket;
import java.util.Vector;

class ServerThread implements Runnable {
    static Vector<PrintWriter> list = new Vector<>();

    private final Socket socket;
    private final PrintWriter writer;
    private final ServerGUI serverGUI;
    private ServerInfo serverInfo;

    public ServerThread(Socket socket, ServerGUI serverGUI) {
        this.socket = socket;
        this.serverGUI = serverGUI;
        this.serverInfo = new ServerInfo();
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
            sendAll("#" + name + "님이 들어오셨습니다.");
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
            sendAll("#" + name + "님이 나가셨습니다.");
            try {
                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void validatePassword(String name, String password) throws AuthenticationException {
        if (!password.equals(serverInfo.getPassword())) {
            sendAll("#" + name + "님이 로그인을 실패했습니다");
            throw new AuthenticationException("Invalid password");
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

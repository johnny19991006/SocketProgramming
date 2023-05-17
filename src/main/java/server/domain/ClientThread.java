package server.domain;

import server.view.ServerGUI;

import java.io.*;
import java.net.Socket;
import java.util.Vector;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

class ClientThread extends Thread {
    static Vector<PrintWriter> list = new Vector<>();
    private static ExecutorService threadPool = Executors.newCachedThreadPool();//쓰레드풀 적용

    Socket socket;
    PrintWriter writer;
    ServerGUI sg;

    public ClientThread(Socket socket, ServerGUI sg) {
        this.socket = socket;
        this.sg = sg;
        try {
            writer = new PrintWriter(socket.getOutputStream());
            list.add(writer);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void run() {
        String name = null;
        try (InputStream is = socket.getInputStream();
             InputStreamReader isr = new InputStreamReader(is);
             BufferedReader reader = new BufferedReader(isr)) {

            name = reader.readLine();
            sendAll("#" + name + "님이 들어오셨습니다.");

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

    private void sendAll(String str) {
        sg.chatstart(str + "\n");
        list.forEach(writer -> {
            writer.println(str);
            writer.flush();
        });
    }

    public static void execute(Socket socket, ServerGUI sg) {
        threadPool.execute(new ClientThread(socket, sg));
    }
}

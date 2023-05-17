package client.domain;



import client.view.ClientMultiGUI;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.Socket;

public class ReceiverThreadM extends Thread {
    Socket socket;
    ClientMultiGUI CMGUI;

    public ReceiverThreadM(Socket socket, ClientMultiGUI cg) {
        this.socket = socket;
        this.CMGUI = cg;
    }

    public void run() {
        try {
            // 소켓으로부터 입력스트림을 준비하고 read가 편하게 하기위해
            // BufferedReader를 사용하겠음
            // 한줄씩 읽어오는 메소드를 활용하기 위함

            // 소켓으로부터 입력스트림을 가져오고
            InputStream is = socket.getInputStream();
            InputStreamReader isr = new InputStreamReader(is);
            BufferedReader reader = new BufferedReader(isr);

            while (true) {
                // reader.readLine() 부분에서 데이터를 수신할때까지 대기
                String str = reader.readLine();
                if (str == null)
                    break;
                CMGUI.append(str + "\n");
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
}

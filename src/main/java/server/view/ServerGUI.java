package server.view;

import server.domain.ServerMulti;

import javax.swing.*;
import java.awt.*;
import java.net.InetAddress;
import java.net.UnknownHostException;

public class ServerGUI extends JFrame {
    private JButton stopStart;
    public JTextArea chat;
    public JTextArea now;
    public JTextField tPortNumber;
    public ServerMulti server;

    public ServerGUI(int port) {
        super("메신저 서버");
        server = null;
        JPanel west = new JPanel();
        west.add(new JLabel("포트 이름: "));
        tPortNumber = new JTextField("  " + port);
        west.add(tPortNumber);
        // to stop or start the server, we start with "Start"
        west.add(new JLabel("IP 주소: "));
        String addr="";
        try {
            addr = InetAddress.getLocalHost().getHostAddress();
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
        west.add(new JTextField(addr));
        add(west, BorderLayout.SOUTH);

        //JPanel center = new JPanel(new GridLayout(2,1));
        JPanel center = new JPanel(new GridLayout(2,1));
        chat = new JTextArea( 40, 40);
        now = new JTextArea("접속자 목록\n",10,40);
        chat.setEditable(false);
        now.setEditable(false);
        chatstart("채팅서버에 입장하셨습니다.\n");
        //center.add(new JScrollPane(chat));
        center.add(new JScrollPane(chat));
        center.add(new JScrollPane(now));
//      add(new JScrollPane(binder));
        setSize(500, 500);
        add(center, BorderLayout.CENTER);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);

    }
    public void startServer() {
        int port;
        try {
            port = Integer.parseInt(tPortNumber.getText().trim());
        }
        catch(Exception e) {
            e.printStackTrace();
            return;
        }
        // ceate a new Server
        server = new ServerMulti(port, this);
        // and start it as a thread
        tPortNumber.setEditable(false);
    }

    public void chatstart(String str) {
        chat.append(str);
        chat.setCaretPosition(chat.getText().length() - 1); //스크롤 될때 마지막 위치 표시
    }
    /*void chatwho(String str) {
        now.append(str);
        now.setCaretPosition(now.getText().length() - 1);
    }*/
}

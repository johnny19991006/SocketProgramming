package server.view;

import server.domain.ServerInfo;
import server.domain.ServerThreadPool;

import javax.swing.*;
import java.awt.*;

public class ServerGUI extends JFrame {
    private final ServerInfo serverInfo;
    public JTextArea chat;
    public JTextArea now;
    public JTextField tPortNumber;
    public ServerThreadPool server;

    public ServerGUI(ServerInfo serverInfo) {
        super("메신저 서버");
        this.serverInfo = serverInfo;

        JPanel west = new JPanel();
        west.add(new JLabel("포트 이름: "));
        tPortNumber = new JTextField("  " + serverInfo.getPort());
        west.add(tPortNumber);
        west.add(new JLabel("IP 주소: "));
        west.add(new JTextField(serverInfo.getAddress()));
        add(west, BorderLayout.SOUTH);

        JPanel center = new JPanel(new GridLayout(2, 1));
        chat = new JTextArea(40, 40);
        now = new JTextArea("누적 접속자 목록\n", 10, 40);
        chat.setEditable(false);
        now.setEditable(false);
        center.add(new JScrollPane(chat));
        center.add(new JScrollPane(now));
        setSize(500, 500);
        add(center, BorderLayout.CENTER);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);
    }

    public void chatList(String str) {
        chat.append(str);
        chat.setCaretPosition(chat.getText().length() - 1);
    }

    public void updateUserList(String userList) {
        now.append(userList);
        now.append("\n");
        now.setCaretPosition(now.getText().length() -1);
    }
}

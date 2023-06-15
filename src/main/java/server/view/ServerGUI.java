package server.view;

import server.domain.ServerInfo;
import server.domain.ServerThreadPool;

import javax.swing.*;
import java.awt.*;

public class ServerGUI extends JFrame {
    private JTextArea chat;
    private JTextArea user;
    private JTextField portNumber;
    private ServerThreadPool server;
    private ServerInfo serverInfo;

    private final static String ServerTitle = "메신저 서버";
    private final static String PortNumber = "포트번호: ";
    private final static String IPNumber = "IP 주소: ";
    private final static String TotalUserList = "누적 접속자 목록\n";

    public ServerGUI(ServerInfo serverInfo) {
        super(ServerTitle);
        this.serverInfo = serverInfo;

        JPanel west = new JPanel();
        west.add(new JLabel(PortNumber));
        portNumber = new JTextField("  " + serverInfo.getPort());
        west.add(portNumber);
        west.add(new JLabel(IPNumber));
        west.add(new JTextField(serverInfo.getAddress()));
        add(west, BorderLayout.SOUTH);

        JPanel center = new JPanel(new GridLayout(2, 1));
        chat = new JTextArea(40, 40);
        user = new JTextArea(TotalUserList, 10, 40);
        chat.setEditable(false);
        user.setEditable(false);
        center.add(new JScrollPane(chat));
        center.add(new JScrollPane(user));
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
        user.append(userList);
        user.append("\n");
        user.setCaretPosition(user.getText().length() - 1);
    }
}

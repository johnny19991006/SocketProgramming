package client.view;


import client.domain.ReceiverThreadM;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;

public class ClientMultiGUI extends JFrame implements ActionListener {
    private JLabel label;
    private JTextField tf;
    private JTextField tfName;
    private JTextField tfServer, tfPort;
    private JButton connect;
    private JButton disconnect;
    private JTextArea ta;
    private boolean connected;
    private PrintWriter writer;

    public ClientMultiGUI(String host, int port) {
        super("메신저 프로그램");

        JPanel northPanel = new JPanel(new GridLayout(3, 1));
        JPanel southPanel = new JPanel(new GridLayout(3, 1));
        JPanel server = new JPanel(new GridLayout(1, 3, 1, 3));
        JPanel Port = new JPanel(new GridLayout(1, 3, 1, 3));

        JPanel sendmassage1 = new JPanel(new GridLayout(1, 1));
        tfServer = new JTextField(host);
        tfPort = new JTextField("" + port);
//      tfPort.setHorizontalAlignment(SwingConstants.RIGHT);
        sendmassage1.add(new JLabel("보낼 메시지"));
        server.add(new JLabel("서버 주소:"));

        server.add(tfServer);
        Port.add(new JLabel("포트 번호:"));

        Port.add(tfPort);
        northPanel.add(server);
        northPanel.add(Port);
        southPanel.add(sendmassage1);

        JPanel userAndConnect = new JPanel(new GridLayout(1, 5, 1, 3));

        connect = new JButton("연결");
        disconnect = new JButton("나가기");
        connect.addActionListener(this);
        userAndConnect.add(new JLabel("이름 :"));
        tfName = new JTextField("우주최강");
        userAndConnect.add(tfName);
        userAndConnect.add(connect);
        tf = new JTextField("");
        tf.setBackground(Color.PINK);
        southPanel.add(tf);
        southPanel.add(disconnect);
        userAndConnect.add(new JLabel(""));
        northPanel.add(userAndConnect);


        add(northPanel, BorderLayout.NORTH);
        add(southPanel, BorderLayout.SOUTH);

        disconnect.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });

        ta = new JTextArea("채팅방에 오신것을 환영합니다\n", 40, 40);
        JPanel centerPanel = new JPanel(new GridLayout(1, 1));
        centerPanel.add(new JScrollPane(ta));

        ta.setEditable(false);
        add(centerPanel, BorderLayout.CENTER);

        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(600, 600);
        setVisible(true);
        tf.requestFocus();

    }

    public void append(String str) {
        ta.append(str);
        ta.setCaretPosition(ta.getText().length() - 1);
    }

    public void actionPerformed(ActionEvent e) {
        Object o = e.getSource();
        if (connected) { //connected가 true이고 이벤트가 있다면 text 입력 이벤트임
            writer.println(tf.getText());
            writer.flush();
            tf.setText("");
            return;
        }

        if (o == connect) {
            String username = tfName.getText().trim();
            if (username.length() == 0)
                return;
            String server = tfServer.getText().trim();
            if (server.length() == 0)
                return;
            String portNumber = tfPort.getText().trim();
            if (portNumber.length() == 0)
                return;
            int port = 0;
            try {
                port = Integer.parseInt(portNumber);
            } catch (Exception en) {
                return;   // 포트번호가 없으면 할 일이 없음
            }

            Socket sc = null;
            try {
                sc = new Socket(server, port);
                writer = new PrintWriter(sc.getOutputStream());
                //내이름부터 서버로 보내준 이후에. 화면입력을 보내는 방식. 일종의 로그인 절차
                writer.println(username);
                writer.flush();
            } catch (UnknownHostException e1) {
                e1.printStackTrace();
            } catch (IOException e1) {
                e1.printStackTrace();
            }

            if (sc == null) {
                return;
            }

            Thread receiver = new ReceiverThreadM(sc, this);
            receiver.start();

            connected = true;
            connect.setEnabled(false);
            tfServer.setEditable(false);
            tfPort.setEditable(false);
            tfName.setEditable(false);
            // 메시지 입력가능하도록
            tf.addActionListener(this);
        }
    }
}

package client.view;

import client.domain.ClientInfo;
import client.domain.ClientThread;
import client.utils.EncryptionUtils;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;

import static client.utils.OutputMessage.STRING_WELCOME_MESSAGE;

public class ClientMultiGUI extends JFrame implements ActionListener {
    private JFrame loginFrame;
    private JTextField clientName, clientPassword, clientServer, clientPort;
    private JButton connectButton;
    private JTextArea client;  // 수정된 부분
    private JTextArea clientMessage;
    private boolean connected;
    private PrintWriter writer;
    private ClientInfo clientInfo = new ClientInfo();

    private final static String ClientName = "메신저 서버";
    private final static String Message = "보낼 메시지";

    public ClientMultiGUI() {
        initLoginWindow();
    }

    private void initLoginWindow() {
        loginFrame = new JFrame("로그인");
        loginFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        loginFrame.setSize(300, 400);

        // 로그인 창의 배경을 노란색으로 설정하는 부분 시작
        loginFrame.getContentPane().setBackground(Color.YELLOW);
        // 로그인 창의 배경을 노란색으로 설정하는 부분 끝

        loginFrame.setLayout(new GridLayout(5, 2, 10, 10));

        clientServer = new JTextField("");
        clientPort = new JTextField("");
        clientName = new JTextField("");
        clientPassword = new JTextField("");

        connectButton = new JButton("연결");
        connectButton.addActionListener(this);

        loginFrame.add(new JLabel("서버 주소:"));
        loginFrame.add(clientServer);
        loginFrame.add(new JLabel("포트 번호:"));
        loginFrame.add(clientPort);
        loginFrame.add(new JLabel("닉네임:"));
        loginFrame.add(clientName);
        loginFrame.add(new JLabel("비밀번호:"));
        loginFrame.add(clientPassword);
        loginFrame.add(new JLabel(""));
        loginFrame.add(connectButton);

        loginFrame.setVisible(true);
    }

    private void initChatWindow() {
        getContentPane().removeAll();

        JPanel northPanel = new JPanel(new GridLayout(3, 1));
        JPanel southPanel = new JPanel(new GridLayout(2, 1));
        JPanel server = new JPanel(new GridLayout(1, 3, 1, 3));
        JPanel Port = new JPanel(new GridLayout(1, 3, 1, 3));

        JPanel messagePanel = new JPanel(new GridLayout(1, 1));
        server.add(new JLabel("보낼 메시지"));
        server.add(clientServer);
        Port.add(new JLabel("서버 주소: "));
        Port.add(clientPort);
        northPanel.add(server);
        northPanel.add(Port);
        southPanel.add(messagePanel);

        client = new JTextArea("");
        client.setLineWrap(true);
        // 수정된 부분 시작
        client.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                // 사용자가 텍스트를 입력할 때마다 처리할 내용
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                // 사용자가 텍스트를 삭제할 때마다 처리할 내용
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                // 스타일이나 속성이 변경될 때 처리할 내용
            }
        });
        // 수정된 부분 끝

        float hue = 209f;
        float saturation = 13f;
        float brightness = 96f;

        Color customColor = Color.getHSBColor(hue / 360f, saturation / 100f, brightness / 100f);

        client.setBackground(customColor);
        southPanel.add(new JScrollPane(client));

        JButton sendButton = new JButton("전송");
        sendButton.addActionListener(this);
        southPanel.add(sendButton);

        sendButton.addActionListener(this);
        northPanel.add(southPanel);

        JButton disconnectButton = new JButton("나가기");
        disconnectButton.addActionListener(this);
        northPanel.add(disconnectButton);

        disconnectButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });

        clientMessage = new JTextArea(STRING_WELCOME_MESSAGE.getMessage(), 40, 40);
        JPanel centerPanel = new JPanel(new GridLayout(1, 1));
        centerPanel.add(new JScrollPane(clientMessage));

        clientMessage.setEditable(false);
        add(centerPanel, BorderLayout.CENTER);

        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(600, 600);
        setVisible(true);
        client.requestFocus();
    }

    public void append(String message) {
        clientMessage.append(message + "\n");
        clientMessage.setCaretPosition(clientMessage.getText().length() - 1);
    }

    public void actionPerformed(ActionEvent e) {
        Object o = e.getSource();
        if (connected) {
            // 수정된 부분 시작
            if (o instanceof JButton && ((JButton) o).getText().equals("전송")) {
                writer.println(client.getText());
                writer.flush();
                client.setText("");
            }
            // 수정된 부분 끝
            return;
        }

        if (o == connectButton) {
            String username = clientName.getText().trim();
            String password = clientPassword.getText().trim();
            if (username.length() == 0)
                return;
            String server = clientServer.getText().trim();
            if (server.length() == 0)
                return;
            String portNumber = clientPort.getText().trim();
            if (portNumber.length() == 0)
                return;
            int port = 0;
            try {
                port = Integer.parseInt(portNumber);
            } catch (Exception en) {
                return;
            }

            Socket socket = null;
            try {
                socket = new Socket(server, port);
                writer = new PrintWriter(socket.getOutputStream());
                writer.println(username);
                writer.flush();
                String encryptedPassword = EncryptionUtils.encrypt(password, clientInfo.getKey());
                writer.println(encryptedPassword);
                writer.flush();
            } catch (UnknownHostException e1) {
                e1.printStackTrace();
            } catch (Exception e1) {
                e1.printStackTrace();
            }

            if (socket == null) {
                return;
            }

            Thread receiver = new ClientThread(socket, this);
            receiver.start();

            connected = true;
            connectButton.setEnabled(false);
            clientServer.setEditable(false);
            clientPort.setEditable(false);
            clientName.setEditable(false);
            clientPassword.setEditable(false);
            initChatWindow();
            loginFrame.dispose();  // Close the login window
        }
    }
}




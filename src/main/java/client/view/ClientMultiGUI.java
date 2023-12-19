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
        JPanel southPanel = new JPanel(new GridLayout(3, 1));  // 수정된 부분

        // 수정된 부분 시작
        clientMessage = new JTextArea(STRING_WELCOME_MESSAGE.getMessage(), 40, 40);
        JScrollPane messageScrollPane = new JScrollPane(clientMessage);
        clientMessage.setEditable(false);
        northPanel.add(messageScrollPane);
        // 수정된 부분 끝

        // 수정된 부분 시작
        client = new JTextArea("");
        client.setLineWrap(true);

        float hue = 209f;
        float saturation = 13f;
        float brightness = 96f;

        Color customColor = Color.getHSBColor(hue / 360f, saturation / 100f, brightness / 100f);

        client.setBackground(customColor);
        southPanel.add(new JScrollPane(client));
        // 수정된 부분 끝

        JButton sendButton = new JButton("전송");
        sendButton.addActionListener(this);
        southPanel.add(sendButton);

        JButton disconnectButton = new JButton("나가기");
        disconnectButton.addActionListener(this);
        southPanel.add(disconnectButton);

        disconnectButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });

        JPanel centerPanel = new JPanel(new GridLayout(1, 1));
        centerPanel.add(northPanel);
        centerPanel.add(southPanel);  // 수정된 부분
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
                String message = client.getText();
                append("You: " + message);
                writer.println(message);
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




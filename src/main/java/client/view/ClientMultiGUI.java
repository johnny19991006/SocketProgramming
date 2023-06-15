package client.view;


import client.domain.ClientInfo;
import client.domain.ClientThread;
import client.utils.EncryptionUtils;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;

import static client.utils.OutputMessage.STRING_WELCOME_MESSAGE;

public class ClientMultiGUI extends JFrame implements ActionListener {
    private JLabel label;
    private JTextField client;
    private JTextField clientName, clientPassword;
    private JTextField clientServer, clientPort;
    private JButton connect;
    private JButton disconnect;
    private JTextArea clientMessage;
    private boolean connected;
    private PrintWriter writer;
    private ClientInfo clientInfo = new ClientInfo();

    private final static String ClientName = "메신저 서버";
    private final static String Message = "보낼 메시지";
    private final static String IPAddress = "서버 주소: ";
    private final static String PortNumber = "포트 번호: ";
    private final static String UserName = "이름: ";
    private final static String PassWord = "비밀번호: ";

    public ClientMultiGUI() {
        super(ClientName);
        JPanel northPanel = new JPanel(new GridLayout(3, 1));
        JPanel southPanel = new JPanel(new GridLayout(3, 1));
        JPanel server = new JPanel(new GridLayout(1, 3, 1, 3));
        JPanel Port = new JPanel(new GridLayout(1, 3, 1, 3));

        JPanel massage = new JPanel(new GridLayout(1, 1));
        clientServer = new JTextField("IpAddress");
        clientPort = new JTextField("PortNumber");
        massage.add(new JLabel(Message));
        server.add(new JLabel(IPAddress));
        server.add(clientServer);
        Port.add(new JLabel(PortNumber));

        Port.add(clientPort);
        northPanel.add(server);
        northPanel.add(Port);
        southPanel.add(massage);

        JPanel userAndConnect = new JPanel(new GridLayout(1, 5, 1, 3));

        connect = new JButton("연결");
        disconnect = new JButton("나가기");
        connect.addActionListener(this);
        userAndConnect.add(new JLabel(UserName));
        clientName = new JTextField("nickname");
        userAndConnect.add(clientName);
        userAndConnect.add(new JLabel(PassWord));
        clientPassword = new JTextField("password");
        userAndConnect.add(clientPassword);
        userAndConnect.add(connect);
        client = new JTextField("");
        float hue = 209f;
        float saturation = 13f;
        float brightness = 96f;

        Color customColor = Color.getHSBColor(hue / 360f, saturation / 100f, brightness / 100f);

        client.setBackground(customColor);
        southPanel.add(client);
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
        clientMessage.append(message);
        clientMessage.setCaretPosition(clientMessage.getText().length() - 1);
    }

    public void actionPerformed(ActionEvent e) {
        Object o = e.getSource();
        if (connected) {
            writer.println(client.getText());
            writer.flush();
            client.setText("");
            return;
        }

        if (o == connect) {
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
            connect.setEnabled(false);
            clientServer.setEditable(false);
            clientPort.setEditable(false);
            clientName.setEditable(false);
            clientPassword.setEditable(false);
            client.addActionListener(this);
        }
    }
}

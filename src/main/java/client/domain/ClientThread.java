package client.domain;

import client.view.ClientMultiGUI;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.Socket;

import org.w3c.dom.Document;
import org.xml.sax.InputSource;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

public class ClientThread extends Thread {
    private Socket socket;
    private ClientMultiGUI clientMultiGUI;

    public ClientThread(Socket socket, ClientMultiGUI clientMultiGUI) {
        this.socket = socket;
        this.clientMultiGUI = clientMultiGUI;
    }

    public void run() {
        try {
            InputStream inputStream = socket.getInputStream();
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
            BufferedReader reader = new BufferedReader(inputStreamReader);

            while (true) {
                String xmlMessage = reader.readLine();
                if (xmlMessage == null)
                    break;

                Document messageDocument = parseXML(xmlMessage);
                handleMessage(messageDocument);
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    private Document parseXML(String xmlString) {
        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            InputSource inputSource = new InputSource(new java.io.StringReader(xmlString));
            return builder.parse(inputSource);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private void handleMessage(Document messageDocument) {
        // Handle the received XML message as needed
        // For example, you can extract information and update the GUI
        // clientMultiGUI.append(message + "\n");
    }
}

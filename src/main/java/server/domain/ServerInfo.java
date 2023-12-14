package server.domain;

import java.io.IOException;
import java.io.InputStream;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Properties;

import org.w3c.dom.Document;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

public class ServerInfo {

    private int port;
    private String address;
    private String password;
    private String key;

    {
        try {
            address = InetAddress.getLocalHost().getHostAddress();
        } catch (UnknownHostException e) {
            throw new RuntimeException(e);
        }
    }

    public ServerInfo() {
        loadProperties();
    }

    private void loadProperties() {
        try (InputStream input = getClass().getClassLoader().getResourceAsStream("application.properties")) {
            Properties properties = new Properties();
            properties.load(input);

            port = Integer.parseInt(properties.getProperty("server.port"));
            password = properties.getProperty("server.password");
            key = properties.getProperty("server.key");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String getAddress() {
        return address;
    }

    public int getPort() {
        return port;
    }

    public String getPassword() {
        return password;
    }

    public String getKey() {
        return key;
    }

    public Document toXmlDocument() {
        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document document = builder.newDocument();

            // Create root element
            org.w3c.dom.Element serverInfoElement = document.createElement("ServerInfo");
            document.appendChild(serverInfoElement);

            // Add elements to the XML document
            appendElement(document, serverInfoElement, "Address", address);
            appendElement(document, serverInfoElement, "Port", Integer.toString(port));
            appendElement(document, serverInfoElement, "Password", password);
            appendElement(document, serverInfoElement, "Key", key);

            return document;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private void appendElement(Document document, org.w3c.dom.Element parent, String tagName, String textContent) {
        org.w3c.dom.Element element = document.createElement(tagName);
        element.appendChild(document.createTextNode(textContent));
        parent.appendChild(element);
    }
}

package client.domain;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class ClientInfo {

    String key;

    public ClientInfo() {
        loadProperties();
    }

    private void loadProperties() {
        try (InputStream input = getClass().getClassLoader().getResourceAsStream("application.properties")) {
            Properties properties = new Properties();
            properties.load(input);

            key = properties.getProperty("client.key");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String getKey() {
        return key;
    }

    public Document toXmlDocument() {
        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();

            Document document = builder.newDocument();
            Element rootElement = document.createElement("ClientInfo");
            document.appendChild(rootElement);

            Element keyElement = document.createElement("key");
            keyElement.appendChild(document.createTextNode(key));
            rootElement.appendChild(keyElement);

            return document;
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
            return null;
        }
    }
}

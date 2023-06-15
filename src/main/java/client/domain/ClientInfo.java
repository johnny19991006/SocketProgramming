package client.domain;

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

}

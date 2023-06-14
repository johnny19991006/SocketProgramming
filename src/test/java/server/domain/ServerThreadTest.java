package server.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import server.view.ServerGUI;

import javax.security.sasl.AuthenticationException;
import java.net.Socket;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class ServerThreadTest {
    @DisplayName("비밀번호 검증 검사")
    @Test
    void testValidatePassword_InvalidPassword() {
        ServerInfo serverInfo = new ServerInfo();
        ServerGUI serverGUI = new ServerGUI(serverInfo);
        Socket socket = new Socket();
        ServerThread serverThread = new ServerThread(socket, serverGUI, serverInfo);


        String name = "John";
        String invalidPassword = "wrong_password";

        AuthenticationException exception = assertThrows(AuthenticationException.class, () -> {
            serverThread.validatePassword(name, invalidPassword);
        });

        assertEquals("Invalid password", exception.getMessage());
    }


}

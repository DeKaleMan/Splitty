package client.utils;

import ch.qos.logback.core.net.server.Client;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.messaging.simp.stomp.StompSession;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;


class ServerUtilsTest {
    private ServerUtils serverUtils;
    private Client mockClient;


    @Test
    void send() {
        StompSession stompSession = mock(StompSession.class);

        ServerUtils serverUtils = new ServerUtils();
        serverUtils.setSession(stompSession);

        String destination = "/test/destination";
        Object payload = new Object();
        serverUtils.send(destination, payload);

        verify(stompSession).send(Mockito.eq(destination), Mockito.eq(payload));

    }

    @Test
    void connectTest() {
        String url = "ws://localhost:8080/websocket";
        mockClient = mock(Client.class);
        serverUtils = new ServerUtils();
        StompSession stomp = serverUtils.connect(url);
        assertNotNull(stomp);
        assertTrue(stomp.isConnected());
    }
}
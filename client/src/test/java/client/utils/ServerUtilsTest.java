package client.utils;

import commons.Event;
import jakarta.ws.rs.client.Client;
import jakarta.ws.rs.client.Invocation;
import jakarta.ws.rs.client.WebTarget;
import jakarta.ws.rs.core.GenericType;
import jakarta.ws.rs.core.MediaType;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import server.database.EventRepository;

import java.util.Date;
import java.util.List;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@SpringBootTest(classes = ServerUtils.class)
class ServerUtilsTest {

    @MockBean
    private Client mockClient;
    private WebTarget mockWebTarget;
    private Invocation.Builder mockBuilder;
    private ServerUtils serverUtils;

    @Mock
    private EventRepository eventRepository;

    @BeforeEach
    public void setUp(){

        mockClient = mock(Client.class);
        mockWebTarget = mock(WebTarget.class);
        mockBuilder = mock(Invocation.Builder.class);
        serverUtils = new ServerUtils(mockClient);


        when(mockClient.target(anyString())).thenReturn(mockWebTarget);
        when(mockWebTarget.path(anyString())).thenReturn(mockWebTarget);
        when(mockWebTarget.request(MediaType.APPLICATION_JSON)).thenReturn(mockBuilder);
        when(mockBuilder.accept(MediaType.APPLICATION_JSON)).thenReturn(mockBuilder);

    }

    // @Test
    // public void getAllEvents() {
    //     Date d = new Date(2004, 07,16);
    //     Event e1 = new Event("test", d, "stijn", "this is an event");
    //     Event e2 = new Event("test2", d, "stijn2", "this is an event2");

    //     List<Event> expected = List.of(e1, e2);
    //     when(mockWebTarget
    //             .path(anyString())
    //             .request(MediaType.APPLICATION_JSON)
    //             .accept(MediaType.APPLICATION_JSON)
    //             .get(new GenericType<List<Event>>() {
    //             }))
    //             .thenReturn(expected);
    //     List<Event> serverTest = serverUtils.getAllEvents();

    //     Assertions.assertEquals(expected, serverTest);
    // }
}

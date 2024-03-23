package client.utils;

//import jakarta.ws.rs.client.Client;

import jakarta.ws.rs.client.Client;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.mock.mockito.MockBean;
import server.database.EventRepository;

import static org.mockito.Mockito.mock;

@ExtendWith(MockitoExtension.class)
class ServerUtilsTest {

    @MockBean
    private Client mockClient;


    @Mock
    private EventRepository eventRepository;

    @InjectMocks
    private ServerUtils serverUtils;

    public void setUp(){
        mockClient = mock(Client.class);
        serverUtils = new ServerUtils();
    }

    @Test
    public void addEvent() {
//        Date d = new Date(2004, 15, 3);
//        Event event = Event.builder()
//                .name("stijn event")
//                .date(d)
//                .owner("stinna event")
//                .description("description event").build();
//        EventDTO eventDTO = EventDTO.builder()
//                .name("stijn")
//                .date(d)
//                .owner("owner stijn")
//                .description("this is a test event description").build();
//
//        when(eventRepository.save(Mockito.any(Event.class))).then(event);
//
//        EventDTO savedEvent = eventRepository.create
    }

    @Test
    public void getQuotesTheHardWay() {
    }

    @Test
    void getQuotes() {
    }

    @Test
    void addQuote() {
    }

    @Test
    void getExpense() {
    }

    @Test
    void getExpenseByEmail() {
    }

    @Test
    void addExpense() {
    }

    @Test
    void getDebtByEventCode() {
    }

    @Test
    void getDebtByParticipant() {
    }

    @Test
    void getDebtByExpense() {
    }

    @Test
    void saveDebt() {
    }

    @Test
    void deleteExpense() {
    }

    @Test
    void getParticipants() {
    }

    @Test
    void getAllEvents() {
    }

    @Test
    void getEventById() {
    }

    @Test
    void deleteEventById() {
    }

    @Test
    void createParticipant() {
    }

    @Test
    void deleteParticipant() {
    }

    @Test
    void updateParticipant() {
    }
}
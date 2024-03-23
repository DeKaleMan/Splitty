package client.utils;

//import jakarta.ws.rs.client.Client;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;

@ExtendWith(MockitoExtension.class)
@SpringBootTest(classes = ServerUtils.class)
class ServerUtilsTest {

    @Mock


    private ServerUtils serverUtils;

    public void setUp(){
        serverUtils = new ServerUtils();
    }

    @Test
    void getQuotesTheHardWay() {
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
    void addEvent() {
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
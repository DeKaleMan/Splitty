package client.utils;

import commons.Event;
import commons.Expense;
import commons.Participant;
import commons.Type;
import commons.dto.ExpenseDTO;
import jakarta.ws.rs.client.Client;
import jakarta.ws.rs.client.Entity;
import jakarta.ws.rs.client.Invocation;
import jakarta.ws.rs.client.WebTarget;
import jakarta.ws.rs.core.GenericType;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ServerUtilsTest {

    @Mock
    private Client mockClient;
    @Mock
    private WebTarget mockWebTarget;
    @Mock
    private Invocation.Builder mockBuilder;

//    @Mock
//    private Expense mockExpense;
    @Mock
    private Response mockResponse;

    ServerUtils serverUtils;

    @BeforeEach
    public void setUp(){
        serverUtils = new ServerUtils(mockClient);
    }

    @Test
    public void getAllEventsTest() {
        // Mock setup
        when(mockClient.target(anyString())).thenReturn(mockWebTarget);
        when(mockWebTarget.path(anyString())).thenReturn(mockWebTarget);
        when(mockWebTarget.request(MediaType.APPLICATION_JSON)).thenReturn(mockBuilder);
        when(mockBuilder.accept(MediaType.APPLICATION_JSON)).thenReturn(mockBuilder);

        Date d = new Date(2004, Calendar.AUGUST,16);
        Event e1 = new Event("test", d, "stijn", "this is an event");
        Event e2 = new Event("test2", d, "stijn2", "this is an event2");
        List<Event> mockEvents = List.of(e1, e2);
        when(mockBuilder.get(new GenericType<List<Event>>() {})).thenReturn(mockEvents);
        List<Event> events = serverUtils.getAllEvents();

        // Verifying the results
        verify(mockClient).target(ServerUtils.SERVER);
        verify(mockWebTarget).path("api/event/all");
        verify(mockWebTarget).request(MediaType.APPLICATION_JSON);
        verify(mockBuilder).accept(MediaType.APPLICATION_JSON);
        assertEquals(mockEvents, events);
    }

    @Test
    public void getExpenseByEventIdTest() {
        // Mock Setup
        when(mockClient.target(anyString())).thenReturn(mockWebTarget);
        when(mockWebTarget.path(anyString())).thenReturn(mockWebTarget);
        when(mockWebTarget.queryParam(anyString(), anyInt())).thenReturn(mockWebTarget);
        when(mockWebTarget.request(MediaType.APPLICATION_JSON)).thenReturn(mockBuilder);
        when(mockBuilder.accept(MediaType.APPLICATION_JSON)).thenReturn(mockBuilder);

        Date d = new Date(2004, Calendar.AUGUST,16);
        Event e1 = new Event("test", d, "stijn", "this is an event");
        Participant p1 = new Participant("testp", 50, "84342521345", "3252345", "kajshd", "","uuidtest", e1);
        List<Expense> mockExpenses = Arrays.asList(
                new Expense(e1, "Test expense", Type.Food, d, 100, p1),
                new Expense(e1, "Test expense 2", Type.Drinks, d, 150, p1)
        );
        when(mockBuilder.get(new GenericType<List<Expense>>() {})).thenReturn(mockExpenses);

        List<Expense> expenses = serverUtils.getExpense(1);

        // Verifying the results
        verify(mockClient).target(ServerUtils.SERVER);
        verify(mockWebTarget).path("api/expenses");
        verify(mockWebTarget).queryParam("eventCode", 1);
        verify(mockWebTarget).request(MediaType.APPLICATION_JSON);
        verify(mockBuilder).accept(MediaType.APPLICATION_JSON);
        verify(mockBuilder).get(new GenericType<List<Expense>>() {});
        assertEquals(mockExpenses, expenses);
    }
        @Test
    public void getExpenseByEmailTest(){
        when(mockClient.target(anyString())).thenReturn(mockWebTarget);
        when(mockWebTarget.path(anyString())).thenReturn(mockWebTarget);
        when(mockWebTarget.resolveTemplate(anyString(), anyString())).thenReturn(mockWebTarget);
        when(mockWebTarget.queryParam(anyString(), anyInt())).thenReturn(mockWebTarget);
        when(mockWebTarget.request(MediaType.APPLICATION_JSON)).thenReturn(mockBuilder);
        when(mockBuilder.accept(MediaType.APPLICATION_JSON)).thenReturn(mockBuilder);

        Date d1 = new Date(2004, Calendar.JULY,16);
        Event e1 = new Event("test", d1, "stijn", "this is an event");
        Participant p1 = new Participant("stijn", 70.0, "1234567890", "123456", "bal@gmail.com", "","uuidtest", e1);
        Expense exp1 = new Expense(e1, "this is a expense", Type.Drinks, d1, 100.0, p1);
        List<Expense> expListMock = List.of(exp1);

        when(mockBuilder.get(new GenericType<List<Expense>>() {})).thenReturn(expListMock);

        List<Expense> withServerUtils = serverUtils.getExpenseByEmail(1, "bal.gmail.com");

        verify(mockClient).target(ServerUtils.SERVER);
        verify(mockWebTarget).path("api/expenses/{payerEmail}");
        verify(mockWebTarget).resolveTemplate("payerEmail", "bal.gmail.com");
        verify(mockWebTarget).queryParam("eventCode" ,1);
        verify(mockWebTarget).request(MediaType.APPLICATION_JSON);
        verify(mockBuilder).accept(MediaType.APPLICATION_JSON);
        verify(mockBuilder).get(new GenericType<List<Expense>>(){
        });
        assertEquals(expListMock, withServerUtils);

    }

    @Test
    public void getEventByIdTestSucces() {
        // Mock setup
        when(mockClient.target(anyString())).thenReturn(mockWebTarget);
        when(mockWebTarget.path(anyString())).thenReturn(mockWebTarget);
        when(mockWebTarget.queryParam(anyString(), anyInt())).thenReturn(mockWebTarget);
        when(mockWebTarget.request(MediaType.APPLICATION_JSON)).thenReturn(mockBuilder);
        when(mockBuilder.accept(MediaType.APPLICATION_JSON)).thenReturn(mockBuilder);
        when(mockResponse.getStatus()).thenReturn(Response.Status.OK.getStatusCode());
        Date d = new Date(2004, Calendar.AUGUST,16);
        Event mockEvent = new Event("test", d, "stijn", "this is an event");
        when(mockResponse.readEntity(new GenericType<Event>(){})).thenReturn(mockEvent);

        when(mockBuilder.get(new GenericType<Response>(){})).thenReturn(mockResponse);
        Event event = serverUtils.getEventById(1);

        // Verifying the results
        verify(mockClient).target(ServerUtils.SERVER);
        verify(mockWebTarget).path("api/event");
        verify(mockWebTarget).queryParam("id", 1);
        verify(mockWebTarget).request(MediaType.APPLICATION_JSON);
        verify(mockBuilder).accept(MediaType.APPLICATION_JSON);
        assertEquals(mockEvent, event);
    }

    @Test
    public void getEventByIdTestFailure() {
        // Mock setup
        when(mockClient.target(anyString())).thenReturn(mockWebTarget);
        when(mockWebTarget.path(anyString())).thenReturn(mockWebTarget);
        when(mockWebTarget.queryParam(anyString(), anyInt())).thenReturn(mockWebTarget);
        when(mockWebTarget.request(MediaType.APPLICATION_JSON)).thenReturn(mockBuilder);
        when(mockBuilder.accept(MediaType.APPLICATION_JSON)).thenReturn(mockBuilder);
        when(mockResponse.getStatus()).thenReturn(Response.Status.BAD_REQUEST.getStatusCode());
        when(mockBuilder.get(new GenericType<Response>(){})).thenReturn(mockResponse);

        // Verifying the results
        assertThrows(RuntimeException.class, () ->
                serverUtils.getEventById(1));
        verify(mockClient).target(ServerUtils.SERVER);
        verify(mockWebTarget).path("api/event");
        verify(mockWebTarget).queryParam("id", 1);
        verify(mockWebTarget).request(MediaType.APPLICATION_JSON);
        verify(mockBuilder).accept(MediaType.APPLICATION_JSON);
    }

    @Test
    public void addExpenseTest(){
        when(mockClient.target(anyString())).thenReturn(mockWebTarget);
        when(mockWebTarget.path(anyString())).thenReturn(mockWebTarget);
        when(mockWebTarget.request(MediaType.APPLICATION_JSON)).thenReturn(mockBuilder);
        when(mockBuilder.accept(MediaType.APPLICATION_JSON)).thenReturn(mockBuilder);

        Date d1 = new Date(2004, Calendar.AUGUST,16);
        Event e1 = new Event("test", d1, "stijn", "this is an event");
        Participant p1 = new Participant("stijn", 70.0, "1234567890", "123456", "bal@gmail.com", "","uuidtest", e1);
        Expense mockExp = new Expense(e1, "this is a expense", Type.Drinks, d1, 100.0, p1);

        when(mockBuilder.post(any(Entity.class), eq(Expense.class))).thenReturn(mockExp);

        Date d = new Date(2004, Calendar.AUGUST,16);
        ExpenseDTO expenseDTO = new ExpenseDTO(1, "test DTO Expense", Type.Food, d, 100.0, "uuidtest");

        Expense res = serverUtils.addExpense(expenseDTO);

        verify(mockClient).target(ServerUtils.SERVER);
        verify(mockWebTarget).path("api/expenses/addExp");
        verify(mockWebTarget).request(MediaType.APPLICATION_JSON);
        verify(mockBuilder).accept(MediaType.APPLICATION_JSON);

        assertEquals(mockExp, res);
        assertNotNull(res);

    }

}

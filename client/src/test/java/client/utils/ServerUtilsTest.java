package client.utils;

import commons.*;
import commons.dto.DebtDTO;
import commons.dto.ExpenseDTO;
import jakarta.ws.rs.client.Client;
import jakarta.ws.rs.client.Entity;
import jakarta.ws.rs.client.Invocation;
import jakarta.ws.rs.client.WebTarget;
import jakarta.ws.rs.core.GenericType;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.h2.tools.Server;
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

    @Test
    public void getDebtByEventCodeTest(){
        when(mockClient.target(anyString())).thenReturn(mockWebTarget);
        when(mockWebTarget.path(anyString())).thenReturn(mockWebTarget);
        when(mockWebTarget.resolveTemplate(anyString(), anyInt())).thenReturn(mockWebTarget);
        when(mockWebTarget.request(MediaType.APPLICATION_JSON)).thenReturn(mockBuilder);
        when(mockBuilder.accept(MediaType.APPLICATION_JSON)).thenReturn(mockBuilder);

        Date d = new Date(2020, Calendar.AUGUST, 23);
        Event e = new Event("test", d, "stijn", "this is an event");
        Participant p = new Participant("jaap", 56.0, "123", "123", "qwer@gmail.com", "", "uuid", e);
        Expense mockExp = new Expense(e, "this is a expense", Type.Drinks, d, 100.0, p);
        Debt mockDebt = new Debt(mockExp, 100.0, p);
        List<Debt> mockDebtList = List.of(mockDebt);
        when(mockBuilder.get(new GenericType<List<Debt>>(){})).thenReturn(mockDebtList);

        List<Debt> debtList = serverUtils.getDebtByEventCode(123);

        verify(mockClient).target(ServerUtils.SERVER);
        verify(mockWebTarget).path("api/debts/{eventId}");
        verify(mockWebTarget).resolveTemplate("eventId", 123);
        verify(mockWebTarget).request(MediaType.APPLICATION_JSON);
        verify(mockBuilder).accept(MediaType.APPLICATION_JSON);

        assertNotNull(debtList);
        assertEquals(mockDebtList, debtList);
    }

    @Test
    public void getDebtByParticipantTest(){
        when(mockClient.target(anyString())).thenReturn(mockWebTarget);
        when(mockWebTarget.path(anyString())).thenReturn(mockWebTarget);
        when(mockWebTarget.resolveTemplate(anyString(), anyInt())).thenReturn(mockWebTarget);
        when(mockWebTarget.resolveTemplate(anyString(), anyString())).thenReturn(mockWebTarget);
        when(mockWebTarget.request(MediaType.APPLICATION_JSON)).thenReturn(mockBuilder);
        when(mockBuilder.accept(MediaType.APPLICATION_JSON)).thenReturn(mockBuilder);

        Date d = new Date(2020, Calendar.AUGUST, 23);
        Event e = new Event("test", d, "stijn", "this is an event");
        Participant p = new Participant("jaap", 56.0, "123", "123", "qwer@gmail.com", "", "uuid", e);
        Expense mockExp = new Expense(e, "this is a expense", Type.Drinks, d, 100.0, p);
        Debt mockDebt = new Debt(mockExp, 100.0, p);
        List<Debt> mockDebtList = List.of(mockDebt);
        when(mockBuilder.get(new GenericType<List<Debt>>(){})).thenReturn(mockDebtList);

        int eventCode = 123;
        String email = "test@gmail.com";
        List<Debt> debtList = serverUtils.getDebtByParticipant(eventCode, email);

        verify(mockClient).target(ServerUtils.SERVER);
        verify(mockWebTarget).path("api/debts/{eventId}/participant/{email}");
        verify(mockWebTarget).resolveTemplate("eventId", 123);
        verify(mockWebTarget).resolveTemplate("email", "test@gmail.com");
        verify(mockWebTarget).request(MediaType.APPLICATION_JSON);
        verify(mockBuilder).accept(MediaType.APPLICATION_JSON);

        assertNotNull(debtList);
        assertEquals(mockDebtList, debtList);
    }

    @Test
    public void getDebtByExpenseTest(){
        when(mockClient.target(anyString())).thenReturn(mockWebTarget);
        when(mockWebTarget.path(anyString())).thenReturn(mockWebTarget);
        when(mockWebTarget.resolveTemplate(anyString(), anyInt())).thenReturn(mockWebTarget);
        when(mockWebTarget.resolveTemplate(anyString(), anyInt())).thenReturn(mockWebTarget);
        when(mockWebTarget.request(MediaType.APPLICATION_JSON)).thenReturn(mockBuilder);
        when(mockBuilder.accept(MediaType.APPLICATION_JSON)).thenReturn(mockBuilder);

        Date d = new Date(2020, Calendar.AUGUST, 23);
        Event e = new Event("test", d, "stijn", "this is an event");
        Participant p = new Participant("jaap", 56.0, "123", "123", "qwer@gmail.com", "", "uuid", e);
        Expense mockExp = new Expense(e, "this is a expense", Type.Drinks, d, 100.0, p);
        Debt mockDebt = new Debt(mockExp, 100.0, p);
        List<Debt> mockDebtList = List.of(mockDebt);
        when(mockBuilder.get(new GenericType<List<Debt>>(){})).thenReturn(mockDebtList);

        List<Debt> debtList = serverUtils.getDebtByExpense(123, 123);

        verify(mockClient).target(ServerUtils.SERVER);
        verify(mockWebTarget).path("api/debts/{eventId}/expense/{expenseId}");
        verify(mockWebTarget).resolveTemplate("eventId", 123);
        verify(mockWebTarget).resolveTemplate("expenseId", 123);
        verify(mockWebTarget).request(MediaType.APPLICATION_JSON);
        verify(mockBuilder).accept(MediaType.APPLICATION_JSON);

        assertEquals(mockDebtList, debtList);
    }

    @Test
    public void saveDebtTest(){
        when(mockClient.target(anyString())).thenReturn(mockWebTarget);
        when(mockWebTarget.path(anyString())).thenReturn(mockWebTarget);
        when(mockWebTarget.request(MediaType.APPLICATION_JSON)).thenReturn(mockBuilder);
        when(mockBuilder.accept(MediaType.APPLICATION_JSON)).thenReturn(mockBuilder);

        Date d = new Date(2020, Calendar.AUGUST, 23);
        Event e = new Event("test", d, "stijn", "this is an event");
        Participant p = new Participant("jaap", 56.0, "123", "123", "qwer@gmail.com", "", "uuid", e);
        Expense mockExp = new Expense(e, "this is a expense", Type.Drinks, d, 100.0, p);
        Debt mockDebt = new Debt(mockExp, 100.0, p);

        when(mockBuilder.post(any(Entity.class), eq(Debt.class))).thenReturn(mockDebt);

        double balance = 100.0;
        int expenseId = 123;
        int eventID = 123;
        DebtDTO debtt = new DebtDTO(balance, eventID, expenseId, "uuid");

        Debt realDebt = serverUtils.saveDebt(debtt);

        verify(mockClient).target(ServerUtils.SERVER);
        verify(mockWebTarget).path("api/debts");
        verify(mockWebTarget).request(MediaType.APPLICATION_JSON);
        verify(mockBuilder).accept(MediaType.APPLICATION_JSON);

        assertEquals(realDebt, mockDebt);
    }


    @Test
    public void deleteExpenseTest() {
        // Mocking necessary components
        when(mockClient.target(anyString())).thenReturn(mockWebTarget);
        when(mockWebTarget.path(anyString())).thenReturn(mockWebTarget);
        when(mockWebTarget.queryParam(anyString(), anyInt())).thenReturn(mockWebTarget);
        when(mockWebTarget.queryParam(anyString(), any(ExpenseId.class))).thenReturn(mockWebTarget);
        when(mockWebTarget.request(MediaType.APPLICATION_JSON)).thenReturn(mockBuilder);
        when(mockBuilder.accept(MediaType.APPLICATION_JSON)).thenReturn(mockBuilder);
        when(mockResponse.getStatus()).thenReturn(Response.Status.OK.getStatusCode());

        Date d = new Date(2020, Calendar.AUGUST, 23);
        Event e = new Event("test", d, "stijn", "this is an event");
        Participant p = new Participant("jaap", 56.0, "123", "123", "qwer@gmail.com", "", "uuid", e);
        Expense mockExp = new Expense(e, "this is a expense", Type.Drinks, d, 100.0, p);
        ExpenseId expenseId = new ExpenseId(e, mockExp.getExpenseId());

        when(mockResponse.readEntity((Class<Object>) any())).thenReturn(mockExp);
        when(mockBuilder.delete()).thenReturn(mockResponse);

        Expense exp = serverUtils.deleteExpense(mockExp);

        verify(mockClient).target(ServerUtils.SERVER);
        verify(mockWebTarget).path("api/expenses");
        verify(mockWebTarget).queryParam("eventID", e.getId());
        verify(mockWebTarget).queryParam("expenseID", expenseId); // Use getExpenseId()
        verify(mockWebTarget).request(MediaType.APPLICATION_JSON);
        verify(mockBuilder).accept(MediaType.APPLICATION_JSON);
        verify(mockBuilder).delete();

        assertNotNull(exp);
        assertEquals(exp, mockExp);
    }

    @Test
    public void getParticipantsTest(){
        when(mockClient.target(anyString())).thenReturn(mockWebTarget);
        when(mockWebTarget.path(anyString())).thenReturn(mockWebTarget);
        when(mockWebTarget.queryParam(anyString(), anyInt())).thenReturn(mockWebTarget);
        when(mockWebTarget.request(MediaType.APPLICATION_JSON)).thenReturn(mockBuilder);
        when(mockBuilder.accept(MediaType.APPLICATION_JSON)).thenReturn(mockBuilder);

        Date d = new Date(2020, Calendar.AUGUST, 23);
        Event e = new Event("test", d, "stijn", "this is an event");
        Participant p = new Participant("jaap", 56.0, "123", "123", "qwer@gmail.com", "", "uuid", e);
        List<Participant> participantList = List.of(p);
        when(mockBuilder.get(new GenericType<List<Participant>>(){})).thenReturn(participantList);

        List<Participant> realPartList = serverUtils.getParticipants(123);

        verify(mockClient).target(ServerUtils.SERVER);
        verify(mockWebTarget).path("api/participants");
        verify(mockWebTarget).queryParam("eventID", 123);
        verify(mockWebTarget).request(MediaType.APPLICATION_JSON);
        verify(mockBuilder).accept(MediaType.APPLICATION_JSON);

        assertEquals(realPartList, participantList);

    }

    @Test
    public void updateEventTest(){
        when(mockClient.target(anyString())).thenReturn(mockWebTarget);
        when(mockWebTarget.path(anyString())).thenReturn(mockWebTarget);
        when(mockWebTarget.queryParam(anyString(), anyString())).thenReturn(mockWebTarget);
        when(mockWebTarget.request(MediaType.APPLICATION_JSON)).thenReturn(mockBuilder);
        when(mockBuilder.accept(MediaType.APPLICATION_JSON)).thenReturn(mockBuilder);

        Date d = new Date(2020, Calendar.AUGUST, 23);
        Event e = new Event("test", d, "stijn", "this is an event");
        String newName = "newName";

        when(mockBuilder.put(any(), eq(Response.class))).thenReturn(mockResponse);

        when(mockResponse.getStatus()).thenReturn(Response.Status.OK.getStatusCode());
        when(mockResponse.readEntity(Event.class)).thenReturn(new Event(newName, d, "stijn", "this is an event"));

        Event updatedEvent = serverUtils.updateEvent(e, newName);

        verify(mockClient).target(ServerUtils.SERVER);
        verify(mockWebTarget).path("api/event/updateName");
        verify(mockWebTarget).queryParam("newName", newName);
        verify(mockWebTarget).request(MediaType.APPLICATION_JSON);
        verify(mockBuilder).accept(MediaType.APPLICATION_JSON);

        assertEquals(updatedEvent.getName(), newName);

    }
    @Test
    public void deleteEventByIdTestFailure() {
        // Mock setup
        when(mockClient.target(anyString())).thenReturn(mockWebTarget);
        when(mockWebTarget.path(anyString())).thenReturn(mockWebTarget);
        when(mockWebTarget.queryParam(anyString(), anyInt())).thenReturn(mockWebTarget);
        when(mockWebTarget.request(MediaType.APPLICATION_JSON)).thenReturn(mockBuilder);
        when(mockBuilder.accept(MediaType.APPLICATION_JSON)).thenReturn(mockBuilder);
        when(mockResponse.getStatus()).thenReturn(Response.Status.BAD_REQUEST.getStatusCode());
        when(mockBuilder.delete()).thenReturn(mockResponse);

        // Verifying the results
        assertThrows(RuntimeException.class, () ->
                serverUtils.deleteEventById(123));
        verify(mockClient).target(ServerUtils.SERVER);
        verify(mockWebTarget).path("api/event");
        verify(mockWebTarget).queryParam("id", 123);
        verify(mockWebTarget).request(MediaType.APPLICATION_JSON);
        verify(mockBuilder).accept(MediaType.APPLICATION_JSON);
    }

    @Test
    public void deleteEventByIdTest(){
        when(mockClient.target(anyString())).thenReturn(mockWebTarget);
        when(mockWebTarget.path(anyString())).thenReturn(mockWebTarget);
        when(mockWebTarget.queryParam(anyString(), anyInt())).thenReturn(mockWebTarget);
        when(mockWebTarget.request(MediaType.APPLICATION_JSON)).thenReturn(mockBuilder);
        when(mockBuilder.accept(MediaType.APPLICATION_JSON)).thenReturn(mockBuilder);
        when(mockResponse.getStatus()).thenReturn(Response.Status.OK.getStatusCode());

        int eventId = 123;
        Date date = new Date(2004, Calendar.APRIL, 15);
        Event mockEvent = new Event("event Name", date, "owner", "description");
        when(mockResponse.readEntity(new GenericType<Event>(){})).thenReturn(mockEvent);
        when(mockBuilder.delete()).thenReturn(mockResponse);

        Event serverUtilsEvent = serverUtils.deleteEventById(eventId);

        verify(mockClient).target(ServerUtils.SERVER);
        verify(mockWebTarget).path("api/event");
        verify(mockWebTarget).queryParam("id", eventId);
        verify(mockWebTarget).request(MediaType.APPLICATION_JSON);
        verify(mockBuilder).accept(MediaType.APPLICATION_JSON);
        assertEquals(serverUtilsEvent, mockEvent);
    }

    @Test
    public void addEventTest(){
        when(mockClient.target(anyString())).thenReturn(mockWebTarget);
        when(mockWebTarget.path(anyString())).thenReturn(mockWebTarget);
        when(mockWebTarget.request(MediaType.APPLICATION_JSON)).thenReturn(mockBuilder);
        when(mockBuilder.accept(MediaType.APPLICATION_JSON)).thenReturn(mockBuilder);
        when(mockResponse.getStatus()).thenReturn(Response.Status.OK.getStatusCode());
        when(mockBuilder.post(any(Entity.class))).thenReturn(mockResponse);


        Date date = new Date(2004, Calendar.APRIL, 15);
        Event mockEvent = new Event("event Name", date, "owner", "description");
        when(mockResponse.readEntity(any(GenericType.class))).thenReturn(mockEvent); // Correct stubbing

        ServerUtils serverUtils = new ServerUtils(mockClient);
        EventDTO eventDTO = new EventDTO("name", date, "owner", "description");
        Event resultEvent = serverUtils.addEvent(eventDTO);


        verify(mockClient).target(ServerUtils.SERVER);
        verify(mockWebTarget).path("api/event");
        verify(mockWebTarget).request(MediaType.APPLICATION_JSON);
        verify(mockBuilder).accept(MediaType.APPLICATION_JSON);

        assertEquals(mockEvent, resultEvent);
    }

}

/*
 * Copyright 2021 Delft University of Technology
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package client.utils;

import static jakarta.ws.rs.core.MediaType.APPLICATION_JSON;

import java.lang.reflect.Type;
import java.util.concurrent.ExecutionException;
import java.util.function.Consumer;
import java.util.*;

import commons.*;
import commons.dto.DebtDTO;
import commons.dto.ExpenseDTO;
import commons.dto.ParticipantDTO;
import jakarta.ws.rs.client.Client;
import commons.dto.PaymentDTO;
import jakarta.ws.rs.core.Response;

import javafx.util.Pair;
import org.glassfish.jersey.client.ClientConfig;

import jakarta.ws.rs.client.ClientBuilder;
import jakarta.ws.rs.client.Entity;
import jakarta.ws.rs.core.GenericType;
import org.springframework.messaging.converter.MappingJackson2MessageConverter;
import org.springframework.messaging.simp.stomp.StompFrameHandler;
import org.springframework.messaging.simp.stomp.StompHeaders;
import org.springframework.messaging.simp.stomp.StompSession;
import org.springframework.messaging.simp.stomp.StompSessionHandlerAdapter;
import org.springframework.web.socket.client.standard.StandardWebSocketClient;
import org.springframework.web.socket.messaging.WebSocketStompClient;

/**
 * The type Server utils.
 */
public class ServerUtils {

    private Client client;
    private StompSession session;

    /**
     * ONLY USE THIS CONSTRUCTOR FOR TESTING PURPOSES
     * Because the server will not be running during testing websockets won't work if using this constructor
     * @param client You're client mock
     */
    public ServerUtils(Client client) {
        this.client = client;
    }

    /**
     * Constructor to use for the actual program (so not for testing)
     */
    public ServerUtils(){
        this.client = ClientBuilder.newClient(new ClientConfig());
        // This is only called if the serverutils class was constructed from the actual program and not a test
        session = connect("ws://localhost:8080/websocket");
    }

    public static final String SERVER = "http://localhost:8080/";

    /**
     * Gets expense.
     *
     * @param eventCode the event code
     * @return the expense
     */
    public List<Expense> getExpense(int eventCode) {
        return client.target(SERVER).path("api/expenses")
            .queryParam("eventCode", eventCode)
            .request(APPLICATION_JSON)
            .accept(APPLICATION_JSON)
            .get(new GenericType<List<Expense>>() {
            });
    }

    /**
     * Gets expense by email.
     *
     * @param eventCode the event code
     * @param email     the email
     * @return the expense by email
     */
    public List<Expense> getExpenseByEmail(int eventCode, String email) {
        return ClientBuilder.newClient(new ClientConfig())
            .target(SERVER).path("api/expenses/{payerEmail}")
            .resolveTemplate("payerEmail", email)
            .queryParam("eventCode", eventCode)
            .request(APPLICATION_JSON)
            .accept(APPLICATION_JSON)
            .get(new GenericType<List<Expense>>() {
            });
    }


    /**
     * Add expense expense.
     *
     * @param expenseDTO the expense dto
     * @return the expense
     */
    public Expense addExpense(ExpenseDTO expenseDTO) {
        return ClientBuilder.newClient(new ClientConfig())
            .target(SERVER).path("api/expenses")
            .request(APPLICATION_JSON)
            .accept(APPLICATION_JSON)
            .post(Entity.entity(expenseDTO, APPLICATION_JSON), Expense.class);
    }

    /**
     * Gets debt by event code.
     *
     * @param eventCode the event code
     * @return the debt by event code
     */
    public List<Debt> getDebtByEventCode(int eventCode) {
        return ClientBuilder.newClient(new ClientConfig())
            .target(SERVER).path("api/debts/{eventId}")
            .resolveTemplate("eventId", eventCode)
            .request(APPLICATION_JSON)
            .accept(APPLICATION_JSON)
            .get(new GenericType<List<Debt>>() {
            });
    }

    /**
     * Gets debt by participant.
     *
     * @param eventCode the event code
     * @param email     the email
     * @return the debt by participant
     */
    public List<Debt> getDebtByParticipant(int eventCode, String email) {
        return ClientBuilder.newClient(new ClientConfig())
            .target(SERVER).path("api/debts/{eventId}/participant/{email}")
            .resolveTemplate("eventId", eventCode)
            .resolveTemplate("email", email)
            .request(APPLICATION_JSON)
            .accept(APPLICATION_JSON)
            .get(new GenericType<List<Debt>>() {
            });
    }

    /**
     * Gets debt by expense.
     *
     * @param eventCode the event code
     * @param expenseId the expense id
     * @return the debt by expense
     */
    public List<Debt> getDebtByExpense(int eventCode, int expenseId) {
        return ClientBuilder.newClient(new ClientConfig())
            .target(SERVER).path("api/debts/{eventId}/expense/{expenseId}")
            .resolveTemplate("eventId", eventCode)
            .resolveTemplate("expenseId", expenseId)
            .request(APPLICATION_JSON)
            .accept(APPLICATION_JSON)
            .get(new GenericType<List<Debt>>() {
            });
    }

    /**
     * Save debt debt.
     *
     * @param debtDTO the debt dto
     * @return the debt
     */
    public Debt saveDebt(DebtDTO debtDTO) {
        return ClientBuilder.newClient(new ClientConfig())
            .target(SERVER).path("api/debts")
            .request(APPLICATION_JSON)
            .accept(APPLICATION_JSON)
            .post(Entity.entity(debtDTO, APPLICATION_JSON), Debt.class);
    }


    /**
     * Delete expense.
     *
     * @param expense the expense
     */
    public void deleteExpense(Expense expense) {
        ExpenseId expenseId = new ExpenseId(expense.getEvent(), expense.getExpenseId());

        ClientBuilder.newClient(new ClientConfig())
            .target(SERVER)
            .path("api/expenses")
            .queryParam("eventID", expense.getEvent().id)
            .queryParam("expenseID", expense.getExpenseId())
            .request(APPLICATION_JSON)
            .delete();
    }



    /**
     * Gets participants.
     *
     * @param eventCode the event code
     * @return the participants
     */

    public List<Participant> getParticipants(int eventCode) {
        return ClientBuilder.newClient(new ClientConfig())
                .target(SERVER).path("api/participants")
                .queryParam("eventID", eventCode)
                .request(APPLICATION_JSON)
                .accept(APPLICATION_JSON)
                .get(new GenericType<List<Participant>>() {
                });
    }


    /**
     * Gets all events.
     *
     * @return the all events
     */

    public List<Event> getAllEvents() {
        var response = client
                .target(SERVER).path("api/event/all")
                .request(APPLICATION_JSON)
                .accept(APPLICATION_JSON)
                .get(new GenericType<List<Event>>(){});
        return response;
    }

    /**
     * Gets event by id.
     *
     * @param id the id
     * @return the event by id
     */
    public Event getEventById(int id) {
        Response response = client.target(SERVER).path("api/event")
                .queryParam("id", id)
                .request(APPLICATION_JSON)
                .accept(APPLICATION_JSON)
                .get(new GenericType<Response>(){});

        if (response.getStatus() == Response.Status.OK.getStatusCode()) {
            Event event = response.readEntity(new GenericType<Event>() {});
            response.close();
            return event;
        } else {
            response.close();
            throw new RuntimeException(
                "Failed to retrieve event. Status code: " + response.getStatus());
        }
    }

    public Event updateEvent(Event event, String newName){
        Response response = client.target(SERVER).path("api/event/updateName")
                .queryParam("newName", newName)
                .request(APPLICATION_JSON)
                .accept(APPLICATION_JSON)
                .put(Entity.entity(event, APPLICATION_JSON), Response.class);

        if (response.getStatus() == Response.Status.OK.getStatusCode()) {
            Event updatedEvent = response.readEntity(Event.class);
            response.close();
            return updatedEvent;
        } else {
            response.close();
            throw new RuntimeException(
                    "Failed to update event. Status code: " + response.getStatus());
        }
    }

    /**
     * Delete event by id event.
     *
     * @param id the id
     * @return the event
     */
    public Event deleteEventById(int id) {
        Response response = ClientBuilder.newClient(new ClientConfig())
            .target(SERVER).path("api/event")
            .queryParam("id", id)
            .request(APPLICATION_JSON)
            .accept(APPLICATION_JSON)
            .delete();

        if (response.getStatus() == Response.Status.OK.getStatusCode()) {
            Event event = response.readEntity(new GenericType<>() {
            });
            response.close();
            return event;
        } else {
            response.close();
            throw new RuntimeException(
                "Failed to delete event. Status code: " + response.getStatus());
        }
    }

    public Event addEvent(EventDTO newEvent) {
        Response response = ClientBuilder.newClient(new ClientConfig())
            .target(SERVER).path("api/event")
            .request(APPLICATION_JSON)
            .accept(APPLICATION_JSON)
            .post(Entity.entity(newEvent, APPLICATION_JSON));

        if (response.getStatus() == Response.Status.OK.getStatusCode()) {
            Event event = response.readEntity(new GenericType<>() {
            });
            response.close();
            return event;
        } else {
            response.close();
            throw new RuntimeException("Failed to add event. Status code: " + response.getStatus());
        }

    }

    // stomp session which means you are connected to your websocket

    public void setSession(StompSession session) {
        this.session = session;
    }

    /**
     * This method creates the websocket connection
     * @param url this is the url you want to connect to
     * @return StompSession
     */
    StompSession connect(String url){
        var client = new StandardWebSocketClient();
        var stomp = new WebSocketStompClient(client);
        stomp.setMessageConverter(new MappingJackson2MessageConverter());
        try{
            return stomp.connect(url, new StompSessionHandlerAdapter() {}).get();
        } catch (InterruptedException e){
            Thread.currentThread().interrupt();
        } catch (ExecutionException e){
            throw new RuntimeException(e);
        }
        throw new IllegalStateException();
    }

    /** This is the method to register for updates
     * the StompFrameHandler() needs two methods. the getPayloadType
     * which type is the message we receive (in this case an expense)
     * The handleFrame you can cast Object payload to an expense since
     * you have the MappingJackson2MessageConverter() in the connect method
     * @param destination this is for example /topic/expense
     * @param consumer
     */
    public <T> void registerForExpenseWS(String destination, Class<T> type, Consumer<T> consumer){
        session.subscribe(destination, new StompFrameHandler() {
            @Override
            public Type getPayloadType(StompHeaders headers) {
                return type;
            }

            @Override
            public void handleFrame(StompHeaders headers, Object payload) {
                consumer.accept((T) payload);
            }
        });
    }

    public void send(String destination, Object o){
        session.send(destination, o);
    }



    public Participant createParticipant(ParticipantDTO p) {
        Response response = ClientBuilder.newClient(new ClientConfig())
                .target(SERVER).path("api/participants")
                .request(APPLICATION_JSON)
                .accept(APPLICATION_JSON)
                .post(Entity.entity(p, APPLICATION_JSON));

        if (response.getStatus() == Response.Status.OK.getStatusCode()) {
            Participant participant = response.readEntity(new GenericType<>() {
            });
            response.close();
            return participant;
        } else {
            response.close();
            throw new RuntimeException("Failed to create participant. Status code: " + response.getStatus());
        }
    }

    public void deleteParticipant(String uuid, int eventId) {
        Response response = ClientBuilder.newClient(new ClientConfig())
                .target(SERVER).path("api/participants/{uuid}/{eventId}")
                .resolveTemplate("uuid", uuid)
                .resolveTemplate("eventId", eventId)
                .request(APPLICATION_JSON)
                .accept(APPLICATION_JSON)
                .delete();
        if (response.getStatus() != Response.Status.OK.getStatusCode()) {
            response.close();
            throw new RuntimeException("Failed to delete participant. Status code: " + response.getStatus());
        }
    }


    // Uuid in this method wouldn't be passed as an argument but rather fetched from the config?
    public Participant updateParticipant(String oldUuid, ParticipantDTO participant) {
        Response response = ClientBuilder.newClient(new ClientConfig())
            .target(SERVER).path("api/participants/{uuid}/{eventId}")
            .resolveTemplate("uuid", oldUuid)
            .resolveTemplate("eventId", participant.getEventId())
            .request(APPLICATION_JSON)
            .accept(APPLICATION_JSON)
            .put(Entity.entity(participant, APPLICATION_JSON));

        if (response.getStatus() == Response.Status.OK.getStatusCode()) {
            Participant updatedParticipant = response.readEntity(new GenericType<>() {
            });
            response.close();
            return updatedParticipant;
        } else {
            response.close();
            throw new RuntimeException(
                "Failed to update participant. Status code: " + response.getStatus());
        }
    }

    public List<Event> getEventsByParticipant(String id) {

        Response response = ClientBuilder.newClient(new ClientConfig())
                .target(SERVER).path("api/participants/{uuid}/events")
                .resolveTemplate("uuid", id)
                .request(APPLICATION_JSON)
                .accept(APPLICATION_JSON)
                .get();

        if (response.getStatus() == Response.Status.OK.getStatusCode()) {
            List<Event> events = response.readEntity(new GenericType<>() {
            });
            response.close();
            return events;
        } else {
            response.close();
            throw new RuntimeException("Failed to retrieve events. Status code: " + response.getStatus());
        }

    }


    public List<Payment> getPaymentsOfEvent(int eventId){
        return ClientBuilder.newClient(new ClientConfig())
            .target(SERVER).path("api/payments/{id}")
            .resolveTemplate("id", eventId)
            .request(APPLICATION_JSON)
            .accept(APPLICATION_JSON)
            .get(new GenericType<List<Payment>>() {
            });
    }

    public Payment savePayment(PaymentDTO paymentDTO) {
        return ClientBuilder.newClient(new ClientConfig())
            .target(SERVER).path("api/payments")
            .request(APPLICATION_JSON)
            .accept(APPLICATION_JSON)
            .post(Entity.entity(paymentDTO, APPLICATION_JSON), Payment.class);
    }

    public Payment updatePayment(PaymentDTO paymentDTO, long paymentId) {
        return ClientBuilder.newClient(new ClientConfig())
            .target(SERVER).path("api/payments/{id}")
            .resolveTemplate("id", paymentId)
            .request(APPLICATION_JSON)
            .accept(APPLICATION_JSON)
            .put(Entity.entity(paymentDTO, APPLICATION_JSON), Payment.class);
    }

    public Payment deletePaymentsOfEvent(int eventId) {
        return ClientBuilder.newClient(new ClientConfig())
            .target(SERVER).path("api/payments/{id}")
            .resolveTemplate("id", eventId)
            .request(APPLICATION_JSON)
            .accept(APPLICATION_JSON)
            .delete(Payment.class);
    }

    public List<Payment> generatePaymentsForEvent(int eventCode) {
        deletePaymentsOfEvent(eventCode);
        List<Participant> participants = getParticipants(eventCode);
        Stack<Pair<String, Double>> banks = new Stack<>();
        Stack<Pair<String, Double>> rest = new Stack<>();
        List<PaymentDTO> payments = new ArrayList<>();
        for (Participant p : participants) {
            if (p.getBalance() > 0) banks.add(new Pair<>(p.getUuid(), p.getBalance()));
            else if (p.getBalance() < 0) rest.add(new Pair<>(p.getUuid(), p.getBalance()));
        }
        generateDTOs(eventCode, rest, banks, payments);
        if (!rest.isEmpty() || !banks.isEmpty())
            throw new RuntimeException("Balances did not add up to 0");
        List<Payment> responseList = new ArrayList<>();
        for (PaymentDTO p : payments) {
            responseList.add(savePayment(p));
        }
        return responseList;
    }

    /**
     * This exists just to overcome checkstyle's cyclomatic complexity warning
     * @param eventCode
     * @param rest
     * @param banks
     * @param payments
     */
    private void generateDTOs(int eventCode, Stack<Pair<String, Double>> rest,
                           Stack<Pair<String, Double>> banks, List<PaymentDTO> payments) {
        while (!rest.isEmpty() && !banks.isEmpty()) {
            Pair<String, Double> payee = banks.pop();
            Pair<String, Double> payer = rest.pop();
            if (compareDouble(payee.getValue(), -payer.getValue(), 0.001) > 0) {
                banks.push(new Pair<>(payee.getKey(), payee.getValue() + payer.getValue()));
                payments.add(
                    new PaymentDTO(payer.getKey(), payee.getKey(), eventCode, -payer.getValue(),
                        false));
            } else if (compareDouble(payee.getValue(), -payer.getValue(), 0.001) == 0) {
                payments.add(
                    new PaymentDTO(payer.getKey(), payee.getKey(), eventCode, -payer.getValue(),
                        false));
            } else {
                rest.push(new Pair<>(payer.getKey(), payer.getValue() + payee.getValue()));
                payments.add(
                    new PaymentDTO(payer.getKey(), payee.getKey(), eventCode, payee.getValue(),
                        false));
            }
        }
    }

    public double[] getStatisticsByEventID(int eventID){
        return ClientBuilder.newClient(new ClientConfig())
                .target(SERVER)
                .path("/api/statistics")
                .queryParam("eventID", eventID)
                .request(APPLICATION_JSON)
                .accept(APPLICATION_JSON)
                .get(double[].class);
    }

    public double getTotalCostEvent(int eventID){
        return ClientBuilder.newClient(new ClientConfig())
                .target(SERVER)
                .path("/api/statistics/totalCost")
                .queryParam("eventID", eventID)
                .request(APPLICATION_JSON)
                .accept(APPLICATION_JSON)
                .get(double.class);
    }


    private int compareDouble(double d1, double d2, double precision) {
        if (Math.abs(d1 - d2) < precision) return 0;
        if (d1 < d2) return -1;
        return 1;
    }
}

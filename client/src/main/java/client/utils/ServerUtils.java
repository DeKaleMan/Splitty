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

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import commons.*;
import commons.dto.*;

import jakarta.ws.rs.client.Client;
import jakarta.ws.rs.client.ClientBuilder;
import jakarta.ws.rs.client.Entity;
import jakarta.ws.rs.core.GenericType;
import jakarta.ws.rs.core.Response;
import javafx.util.Pair;
import org.glassfish.jersey.client.ClientConfig;
import org.springframework.dao.DuplicateKeyException;

import org.springframework.messaging.converter.MappingJackson2MessageConverter;
import org.springframework.messaging.simp.stomp.StompFrameHandler;
import org.springframework.messaging.simp.stomp.StompHeaders;
import org.springframework.messaging.simp.stomp.StompSession;
import org.springframework.messaging.simp.stomp.StompSessionHandlerAdapter;
import org.springframework.web.socket.client.standard.StandardWebSocketClient;
import org.springframework.web.socket.messaging.WebSocketStompClient;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Stack;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.function.Consumer;

import static jakarta.ws.rs.core.MediaType.APPLICATION_JSON;

/**
 * The type server utils.
 */
public class ServerUtils {

    private Client client;
    private StompSession session;
    private Config config;

    /**
     * ONLY USE THIS CONSTRUCTOR FOR TESTING PURPOSES
     * Because the server will not be running during testing websockets won't work if using this constructor
     *
     * @param client You're client mock
     */
    public ServerUtils(Client client) {
        this.client = client;
    }

    /**
     * Constructor to use for the actual program (so not for testing)
     */
    public ServerUtils() {
        this.client = ClientBuilder.newClient(new ClientConfig());
        // This is only called if the serverutils class was constructed from the actual program and not a test
        session = connect("ws://" + serverDomain + "/websocket");
    }

    public static void resetServer() {
        server = "http://" + serverDomain + "/";
    }

    public static String serverDomain = "localhost:8080";
    public static String server = "http://localhost:8080/";

    /**
     * Gets expense.
     *
     * @param eventCode the event code
     * @return the expense
     */
    public List<Expense> getExpense(int eventCode) {
        return client.target(server).path("api/expenses")
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
     * @param uuid      the uuid
     * @return the expense by email
     */
    public List<Expense> getExpenseByUuid(int eventCode, String uuid) {
        return client
            .target(server).path("api/expenses/{uuid}")
            .resolveTemplate("uuid", uuid)
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
        return client
            .target(server).path("api/expenses/addExp")
            .request(APPLICATION_JSON)
            .accept(APPLICATION_JSON)
            .post(Entity.entity(expenseDTO, APPLICATION_JSON), Expense.class);
    }

    public Expense updateExpense(int expenseId, ExpenseDTO expenseDTO) {
        List<Debt> debts = getDebtByExpense(expenseDTO.getEventId(), expenseId);
        for (Debt d : debts) {
            Participant p = d.getParticipant();
            updateParticipant(p.getUuid(), new ParticipantDTO(p.getName(),
                p.getBalance() - d.getBalance(), p.getIBan(), p.getBIC(), p.getEmail(),
                p.getAccountHolder(), p.getEvent().getId(), p.getUuid()));
        }

        deleteDebtsOfExpense(expenseDTO.getEventId(), expenseId);
        return ClientBuilder.newClient(new ClientConfig())
            .target(server).path("api/expenses/{eventId}/{expenseId}")
            .resolveTemplate("eventId", expenseDTO.getEventId())
            .resolveTemplate("expenseId", expenseId)
            .request(APPLICATION_JSON)
            .accept(APPLICATION_JSON)
            .put(Entity.entity(expenseDTO, APPLICATION_JSON), Expense.class);
    }

    /**
     * Gets debt by event code.
     *
     * @param eventCode the event code
     * @return the debt by event code
     */
    public List<Debt> getDebtByEventCode(int eventCode) {
        return client
            .target(server).path("api/debts/{eventId}")
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
        return client
            .target(server).path("api/debts/{eventId}/participant/{email}")
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
        return client
            .target(server).path("api/debts/{eventId}/expense/{expenseId}")
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
        return client
            .target(server).path("api/debts")
            .request(APPLICATION_JSON)
            .accept(APPLICATION_JSON)
            .post(Entity.entity(debtDTO, APPLICATION_JSON), Debt.class);
    }

    public List<Debt> deleteDebtsOfExpense(int eventId, int expenseId) {
        return ClientBuilder.newClient(new ClientConfig())
            .target(server).path("api/debts/{eventId}/{expenseId}")
            .resolveTemplate("eventId", eventId)
            .resolveTemplate("expenseId", expenseId)
            .request(APPLICATION_JSON)
            .accept(APPLICATION_JSON)
            .delete(new GenericType<List<Debt>>() {
            });
    }


    /**
     * Delete expense.
     *
     * @param expense the expense
     */
    public Expense deleteExpense(Expense expense) {
        Response response = client
            .target(server)
            .path("api/expenses")
            .queryParam("eventID", expense.getEvent().id)
            .queryParam("expenseID", expense.getExpenseId())
            .request(APPLICATION_JSON)
            .accept(APPLICATION_JSON)
            .delete();
        if (response.getStatus() == Response.Status.OK.getStatusCode()) {
            Expense e = response.readEntity(Expense.class);
            response.close();
            return e;
        } else {
            response.close();
            throw new RuntimeException("failed to delete an expense " + response.getStatus());
        }
    }


    /**
     * Gets participants.
     *
     * @param eventCode the event code
     * @return the participants
     */

    public List<Participant> getParticipants(int eventCode) {
        return client
            .target(server).path("api/participants")
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
            .target(server).path("api/event/all")
            .request(APPLICATION_JSON)
            .accept(APPLICATION_JSON)
            .get(new GenericType<List<Event>>() {
            });
        return response;
    }

    /**
     * Gets event by id.
     *
     * @param id the id
     * @return the event by id
     */
    public Event getEventById(int id) {
        Response response = client.target(server).path("api/event")
            .queryParam("id", id)
            .request(APPLICATION_JSON)
            .accept(APPLICATION_JSON)
            .get(new GenericType<Response>() {
            });

        if (response.getStatus() == Response.Status.OK.getStatusCode()) {
            Event event = response.readEntity(new GenericType<Event>() {
            });
            response.close();
            return event;
        } else {
            response.close();
            throw new RuntimeException(
                "Failed to retrieve event. Status code: " + response.getStatus());
        }
    }

    public Event updateEvent(Event event, String newName) {
        Response response = client.target(server).path("api/event/updateName")
            .queryParam("newName", newName)
            .queryParam("id", event.getId())
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
        Response response = client
            .target(server).path("api/event")
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
        if (newEvent.getDescription() == null ||
                newEvent.getDescription().isEmpty()) {
            newEvent.setDescription("");
        }
        Response response = client
            .target(server).path("api/event")
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

    public boolean setTags(int eventId) {
        Response response = client
                .target(server).path("api/tag/setup/{eventId}")
                .resolveTemplate("eventId", eventId)
                .request(APPLICATION_JSON)
                .accept(APPLICATION_JSON)
                .get();
        return response.getStatus() == Response.Status.OK.getStatusCode();
    }

    public List<Tag> getTagsByEvent(int eventId) {
        Response response = client.target(server).path("api/tag/getAll/{eventId}")
                .resolveTemplate("eventId", eventId)
                .request(APPLICATION_JSON)
                .accept(APPLICATION_JSON)
                .get();
        if (response.getStatus() == Response.Status.OK.getStatusCode()) {
            List<Tag> list = response.readEntity(new GenericType<>() {
            });
            response.close();
            return list;
        } else {
            response.close();
            throw new RuntimeException(
                    "Failed to retrieve tags. Status code: " + response.getStatus());
        }
    }
    public Tag deleteTag(int eventId, String name) {
        Response response = client.target(server).path("api/tag/{name}/{eventId}")
                .resolveTemplate("name", name)
                .resolveTemplate("eventId", eventId)
                .request(APPLICATION_JSON)
                .accept(APPLICATION_JSON)
                .delete();
        if (response.getStatus() == Response.Status.OK.getStatusCode()) {
            Tag tag = response.readEntity(new GenericType<>() {
            });
            response.close();
            return tag;
        } else {
            response.close();
            throw new RuntimeException(
                    "Failed to delete tag. Status code: " + response.getStatus());
        }
    }

    public Tag getOtherTagById(int eventId) {
        Response response = client.target(server).path("api/tag/other/{eventId}")
                .resolveTemplate("eventId", eventId)
                .request(APPLICATION_JSON)
                .accept(APPLICATION_JSON)
                .get();
        if (response.getStatus() == Response.Status.OK.getStatusCode()) {
            Tag tag = response.readEntity(new GenericType<>() {
            });
            response.close();
            return tag;
        } else {
            response.close();
            throw new RuntimeException(
                    "Failed to retrieve tag. Status code: " + response.getStatus());
        }
    }

    public Tag addTag(TagDTO tagDTO) {
        Response response = client
                .target(server).path("api/tag")
                .request(APPLICATION_JSON)
                .accept(APPLICATION_JSON)
                .post(Entity.entity(tagDTO, APPLICATION_JSON));
        if (response.getStatus() == Response.Status.OK.getStatusCode()) {
            Tag tag = response.readEntity(new GenericType<>() {
            });
            response.close();
            return tag;
        } else {
            response.close();
            throw new RuntimeException(
                    "Failed to save tag. Status code: " + response.getStatus());
        }
    }

    public Tag updateTag(TagDTO tagDTO, String name, int eventId) {
        Response response = client.target(server).path("api/tag/{name}/{eventId}")
                .resolveTemplate("name", name)
                .resolveTemplate("eventId", eventId)
                .request(APPLICATION_JSON)
                .accept(APPLICATION_JSON)
                .put(Entity.entity(tagDTO, APPLICATION_JSON));
        if (response.getStatus() == Response.Status.OK.getStatusCode()) {
            Tag tag = response.readEntity(new GenericType<>() {
            });
            response.close();
            return tag;
        } else {
            response.close();
            List<Tag> tags = getTagsByEvent(eventId);
            if (tags.stream().anyMatch(t ->
                    t.getName().equals(tagDTO.getName()))) {
                throw new DuplicateKeyException("This name is already used for this event");
            } else {
                throw new RuntimeException();
                // either the event is null or the tag with the given name and event does not exit
            }
        }
    }

    // stomp session which means you are connected to your websocket

    public void setSession(StompSession session) {
        this.session = session;
    }

    /**
     * This method creates the websocket connection
     *
     * @param url this is the url you want to connect to
     * @return StompSession
     */
    StompSession connect(String url) {
        var client = new StandardWebSocketClient();
        var stomp = new WebSocketStompClient(client);
        stomp.setMessageConverter(new MappingJackson2MessageConverter());
        try {
            return stomp.connect(url, new StompSessionHandlerAdapter() {
            }).get();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        } catch (ExecutionException e) {
            throw new RuntimeException(e);
        }
        throw new IllegalStateException();
    }

    /**
     * This is the method to register for updates
     * the StompFrameHandler() needs two methods. the getPayloadType
     * which type is the message we receive (in this case an expense)
     * The handleFrame you can cast Object payload to an expense since
     * you have the MappingJackson2MessageConverter() in the connect method
     *
     * @param destination this is for example /topic/expense
     * @param consumer
     */
    public <T> void registerForExpenseWS(String destination, Class<T> type, Consumer<T> consumer) {
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

    public void send(String destination, Object o) {
        session.send(destination, o);
    }

    public Participant createParticipant(ParticipantDTO p) {
        Response response = client
            .target(server).path("api/participants")
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
            throw new RuntimeException(
                "Failed to create participant. Status code: " + response.getStatus());
        }
    }

    public Participant deleteParticipant(String uuid, int eventId) {
        Response response = client
            .target(server).path("api/participants/{uuid}/{eventId}")
            .resolveTemplate("uuid", uuid)
            .resolveTemplate("eventId", eventId)
            .request(APPLICATION_JSON)
            .accept(APPLICATION_JSON)
            .delete();
        if (response.getStatus() != Response.Status.OK.getStatusCode()) {
            response.close();
            throw new RuntimeException(
                "Failed to delete participant. Status code: " + response.getStatus());
        } else {
            Participant p = response.readEntity(new GenericType<>() {
            });
            response.close();
            return p;
        }
    }

    // Uuid in this method wouldn't be passed as an argument but rather fetched from the config?
    public Participant updateParticipant(String uuid, ParticipantDTO participant) {
        Response response = client
            .target(server).path("api/participants/{uuid}/{eventId}")
            .resolveTemplate("uuid", uuid)
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
        Response response = client
            .target(server).path("api/participants/{uuid}/events")
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
            throw new RuntimeException(
                "Failed to retrieve events. Status code: " + response.getStatus());
        }

    }


    public List<Payment> getPaymentsOfEvent(int eventId) {
        return client
            .target(server).path("api/payments/event/{id}")
            .resolveTemplate("id", eventId)
            .request(APPLICATION_JSON)
            .accept(APPLICATION_JSON)
            .get(new GenericType<List<Payment>>() {
            });
    }

    public Payment getPayment(long id) {
        Response response = client
            .target(server).path("api/payments/{id}")
            .resolveTemplate("id", id)
            .request(APPLICATION_JSON)
            .accept(APPLICATION_JSON)
            .get();

        if(response.getStatus() == 404) throw new RuntimeException("Payments have been updated");
        return response.readEntity(Payment.class);
    }

    public Payment savePayment(PaymentDTO paymentDTO) {
        return client
            .target(server).path("api/payments")
            .request(APPLICATION_JSON)
            .accept(APPLICATION_JSON)
            .post(Entity.entity(paymentDTO, APPLICATION_JSON), Payment.class);
    }

    public Payment updatePayment(PaymentDTO paymentDTO, long paymentId) {
        return client
            .target(server).path("api/payments/{id}")
            .resolveTemplate("id", paymentId)
            .request(APPLICATION_JSON)
            .accept(APPLICATION_JSON)
            .put(Entity.entity(paymentDTO, APPLICATION_JSON), Payment.class);
    }


    public List<Payment> deletePaymentsOfEvent(int eventId) {
        return client
            .target(server).path("api/payments/{id}")
            .resolveTemplate("id", eventId)
            .request(APPLICATION_JSON)
            .accept(APPLICATION_JSON)
            .delete(new GenericType<List<Payment>>() {
            });
    }

    public List<Payment> generatePaymentsForEvent(int eventCode) {
        deletePaymentsOfEvent(eventCode);
        List<Participant> participants = getParticipants(eventCode);
        Stack<Pair<String, Double>> banks = new Stack<>();
        Stack<Pair<String, Double>> rest = new Stack<>();
        List<PaymentDTO> payments = new ArrayList<>();
        for (Participant p : participants) {
            if (compareDouble(p.getBalance(), 0, 0.001) > 0)
                banks.add(new Pair<>(p.getUuid(), p.getBalance()));
            else if (compareDouble(p.getBalance(), 0, 0.001) < 0)
                rest.add(new Pair<>(p.getUuid(), p.getBalance()));
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
     *
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

    public Double getSumByTag(int eventID, String tagName) {
        return client
            .target(server)
            .path("/api/statistics/{eventId}/{tagName}")
            .resolveTemplate("eventId", eventID)
            .resolveTemplate("tagName", tagName)
            .request(APPLICATION_JSON)
            .accept(APPLICATION_JSON)
            .get(Double.class);
    }

    public double getTotalCostEvent(int eventID) {
        return client
            .target(server)
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


    public Participant getParticipant(String uuid, int eventId) {
        Response response = client.target(server).path("api/participants/{uuid}/{eventId}")
            .resolveTemplate("uuid", uuid)
            .resolveTemplate("eventId", eventId)
            .request(APPLICATION_JSON)
            .accept(APPLICATION_JSON)
            .get();

        if (response.getStatus() == Response.Status.OK.getStatusCode()) {
            Participant participant = response.readEntity(new GenericType<>() {
            });
            response.close();
            return participant;
        } else {
            response.close();
            throw new RuntimeException(
                "Failed to retrieve participant. Status code: " + response.getStatus());
        }

    }

    //http://localhost:8080/api/currency/?from=USD&to=CHF&date=31-03-2024
    public Conversion getConversion(Currency from, Currency to, String date) {
        Response response = client
            .target(server).path("api/currency")
            .queryParam("from", from.toString())
            .queryParam("to", to.toString())
            .queryParam("date", date)
            .request(APPLICATION_JSON)
            .accept(APPLICATION_JSON)
            .get();

        try {
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode jsonNode = response.readEntity(JsonNode.class);
            Conversion conversion =
                objectMapper.treeToValue(jsonNode.get("conversion"), Conversion.class);
            if (conversion == null) throw new RuntimeException("Failed to retrieve conversion");
            return conversion;
        } catch (JsonProcessingException e) {
            System.out.println("Error while parsing JSON");
            return null;
        } catch (RuntimeException e) {
            System.out.println(e.getMessage());
            return null;
        }
    }


    private static final ExecutorService EXEC = Executors.newFixedThreadPool(2);

    public void registerForParticipantLongPolling(Consumer<Participant> updates,
                                                  Consumer<Participant> deletions) {
        EXEC.submit(() -> {
            while (!Thread.interrupted()) {
                Response res = client.target(server).path("api/participants/updates")
                    .request(APPLICATION_JSON)
                    .accept(APPLICATION_JSON)
                    .get();

                if (res.getStatus() == 200) updates.accept(res.readEntity(Participant.class));
            }
        });
        EXEC.submit(() -> {
            while (!Thread.interrupted()) {
                Response res = client.target(server).path("api/participants/deletions")
                    .request(APPLICATION_JSON)
                    .accept(APPLICATION_JSON)
                    .get();

                if (res.getStatus() == 200) deletions.accept(res.readEntity(Participant.class));
            }
        });
    }

    public String getLanguageJSON(String lang){
        return client.target(server)
                .path("api/translate/json")
                .queryParam("lang", lang)
                .request(APPLICATION_JSON)
                .accept(APPLICATION_JSON)
                .get(String.class);
    }
    public String setNewLang(String jsonObject, String lang){
        return client.target(server)
                .path("api/translate/write")
                .queryParam("lang", lang)
                .request(APPLICATION_JSON)
                .accept(APPLICATION_JSON)
                .put(Entity.entity(jsonObject, APPLICATION_JSON), String.class);
    }

    public String translate(String query, String sourceLang, String targetLang) {
        Response response = ClientBuilder.newClient()
                .target(server)
                .path("api/translate")
                .queryParam("query", query)
                .queryParam("sourceLang", sourceLang)
                .queryParam("targetLang", targetLang)
                .request(APPLICATION_JSON)
                .get();
        if (response.getStatus() != Response.Status.OK.getStatusCode()) {
            response.close();
            //throw new RuntimeException("Failed to retrieve language. Status code: " + response.getStatus());
            return "no language found";
        }
        String res = response.readEntity(String.class);

        return res;

    }


    public void stop() {
        EXEC.shutdownNow();
    }

}

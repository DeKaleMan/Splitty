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

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;


import commons.*;
import commons.dto.DebtDTO;
import commons.dto.ExpenseDTO;
import jakarta.ws.rs.core.Response;

import org.glassfish.jersey.client.ClientConfig;

import jakarta.ws.rs.client.ClientBuilder;
import jakarta.ws.rs.client.Entity;
import jakarta.ws.rs.core.GenericType;

public class ServerUtils {

    private static final String SERVER = "http://localhost:8080/";

    public void getQuotesTheHardWay() throws IOException, URISyntaxException {
        var url = new URI("http://localhost:8080/api/quotes").toURL();
        var is = url.openConnection().getInputStream();
        var br = new BufferedReader(new InputStreamReader(is));
        String line;
        while ((line = br.readLine()) != null) {
            System.out.println(line);
        }
    }

    public List<Quote> getQuotes() {
        return ClientBuilder.newClient(new ClientConfig()) //
                .target(SERVER).path("api/quotes") //
                .request(APPLICATION_JSON) //
                .accept(APPLICATION_JSON) //
                .get(new GenericType<List<Quote>>() {
                });
    }

    public Quote addQuote(Quote quote) {
        return ClientBuilder.newClient(new ClientConfig()) //
                .target(SERVER).path("api/quotes") //
                .request(APPLICATION_JSON) //
                .accept(APPLICATION_JSON) //
                .post(Entity.entity(quote, APPLICATION_JSON), Quote.class);
    }

    public List<Expense> getExpense(int eventCode) {
        return ClientBuilder.newClient(new ClientConfig())
                .target(SERVER).path("api/expenses")
                .queryParam("eventCode", eventCode)
                .request(APPLICATION_JSON)
                .accept(APPLICATION_JSON)
                .get(new GenericType<List<Expense>>() {
                });
    }

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
    public Expense addExpense(ExpenseDTO expenseDTO) {
        return ClientBuilder.newClient(new ClientConfig())
                .target(SERVER).path("api/expenses")
                .request(APPLICATION_JSON)
                .accept(APPLICATION_JSON)
                .post(Entity.entity(expenseDTO, APPLICATION_JSON), Expense.class);
    }

    public List<Debt> getDebtByEventCode(int eventCode) {
        return ClientBuilder.newClient(new ClientConfig())
                .target(SERVER).path("api/debts/{eventId}")
                .resolveTemplate("eventId", eventCode)
                .request(APPLICATION_JSON)
                .accept(APPLICATION_JSON)
                .get(new GenericType<List<Debt>>() {
                });
    }

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

    public Debt saveDebt(DebtDTO debtDTO) {
        return ClientBuilder.newClient(new ClientConfig())
                .target(SERVER).path("api/debts")
                .request(APPLICATION_JSON)
                .accept(APPLICATION_JSON)
                .post(Entity.entity(debtDTO, APPLICATION_JSON), Debt.class);
    }


    public void deleteExpense(Expense expense) {
        ExpenseId expenseId = new ExpenseId(expense.getEvent(), expense.getExpenseId());

        ClientBuilder.newClient(new ClientConfig())
                .target(SERVER)
                .path("api/expenses/{eventID}/{expenseID}")
                .resolveTemplate("eventID", expense.getEvent().id)
                .resolveTemplate("expenseID", expense.getExpenseId())
                .request(APPLICATION_JSON)
                .delete();
    }



    public List<Participant> getParticipants(int eventCode) {
        return ClientBuilder.newClient(new ClientConfig())
                .target(SERVER).path("api/participant")
                .queryParam("eventCode", eventCode)
                .request(APPLICATION_JSON)
                .accept(APPLICATION_JSON)
                .get(new GenericType<List<Participant>>() {
                });
    }
    public List<Event> getAllEvents() {
        Response response = ClientBuilder.newClient(new ClientConfig())
                .target(SERVER).path("api/event/all")
                .request(APPLICATION_JSON)
                .accept(APPLICATION_JSON)
                .get();

        if (response.getStatus() == Response.Status.OK.getStatusCode()) {
            List<Event> events = response.readEntity(new GenericType<>(){});
            response.close();
            return events;
        } else {
            // Handle error response
            response.close();
            throw new RuntimeException("Failed to retrieve events. Status code: " + response.getStatus());
        }
    }

    public Event getEventById(int id) {
        Response response = ClientBuilder.newClient(new ClientConfig())
                .target(SERVER).path("api/event?id=" + id)
                .request(APPLICATION_JSON)
                .accept(APPLICATION_JSON)
                .get();

        if (response.getStatus() == Response.Status.OK.getStatusCode()) {
            Event event = response.readEntity(new GenericType<>(){});
            response.close();
            return event;
        } else {
            response.close();
            throw new RuntimeException("Failed to retrieve event. Status code: " + response.getStatus());
        }
    }

    public Event deleteEventById(int id) {
        Response response = ClientBuilder.newClient(new ClientConfig())
                .target(SERVER).path("api/event")
                .queryParam("id", id)
                .request(APPLICATION_JSON)
                .accept(APPLICATION_JSON)
                .delete();

        if (response.getStatus() == Response.Status.OK.getStatusCode()) {
            Event event = response.readEntity(new GenericType<>(){});
            response.close();
            return event;
        } else {
            response.close();
            throw new RuntimeException("Failed to delete event. Status code: " + response.getStatus());
        }
    }

    public Event addEvent(Event newEvent) {
        Response response = ClientBuilder.newClient(new ClientConfig())
                .target(SERVER).path("api/event")
                .request(APPLICATION_JSON)
                .accept(APPLICATION_JSON)
                .post(Entity.entity(newEvent, APPLICATION_JSON));

        if (response.getStatus() == Response.Status.OK.getStatusCode()) {
            Event event = response.readEntity(new GenericType<>(){});
            response.close();
            return event;
        } else {
            response.close();
            throw new RuntimeException("Failed to add event. Status code: " + response.getStatus());
        }

    }
}
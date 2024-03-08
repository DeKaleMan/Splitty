package client.utils;

import jakarta.ws.rs.client.ClientBuilder;
import jakarta.ws.rs.core.Response;
import org.glassfish.jersey.client.ClientConfig;

import static jakarta.ws.rs.core.MediaType.APPLICATION_JSON;

public class AdminServerUtils {

    private static String server = "";

    public Response validatePassword(String password, String serverUrl) {
        server = "http://" + serverUrl + "/";
        return ClientBuilder.newClient(new ClientConfig())
                .target(server).path("/api/admin/validate_password")
                .queryParam("password", password)
                .request(APPLICATION_JSON)
                .accept(APPLICATION_JSON)
                .get();
    }
}

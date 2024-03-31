package server.api.depinjectionUtils;

import jakarta.ws.rs.client.ClientBuilder;
import jakarta.ws.rs.core.GenericType;
import org.glassfish.jersey.client.ClientConfig;
import org.springframework.stereotype.Component;

import static jakarta.ws.rs.core.MediaType.APPLICATION_JSON;

@Component
public class HttpResponseActual implements HttpResponse {

    private static final String CURRENCY_SERVER = "https://openexchangerates.org";

    @Override
    public String getExchangeRateResponse(String date) {
        System.out.println(date);
        return ClientBuilder.newClient(new ClientConfig())
                .target(CURRENCY_SERVER).path("api/historical/" + date + ".json")
                .queryParam("app_id", "ecc02200bf6d4b95b136ec71eca463d5")
                .queryParam("base", "USD")
                .request(APPLICATION_JSON)
                .accept(APPLICATION_JSON)
                .get(new GenericType<>() {});
    }
}

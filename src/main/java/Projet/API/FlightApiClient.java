package Projet.API;

import org.apache.hc.client5.http.classic.methods.HttpGet;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.CloseableHttpResponse;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.apache.hc.core5.http.ParseException;
import org.apache.hc.core5.http.io.entity.EntityUtils;

import java.io.IOException;

public class FlightApiClient {

        private static final String API_KEY = "a0a73334a6c0e8f63df1bf8fc2ca1c04";
        private static final String BASE_URL = "http://api.aviationstack.com/v1/flights";

        public static String getFlights(String departureIata, String arrivalIata) throws IOException, ParseException {
            String url = BASE_URL + "?access_key=" + API_KEY + "&dep_iata=" + departureIata + "&arr_iata=" + arrivalIata;

            try (CloseableHttpClient httpClient = HttpClients.createDefault()) {
                HttpGet request = new HttpGet(url);
                try (CloseableHttpResponse response = httpClient.execute(request)) {
                    String jsonResponse = EntityUtils.toString(response.getEntity());
                    return jsonResponse;
                }
            }
        }

        public static void main(String[] args) {
            try {
                String jsonResponse = getFlights("TUN", "DXB"); // Example: Tunis to Dubai
                System.out.println(jsonResponse);
            } catch (IOException e) {
                e.printStackTrace();
            } catch (ParseException e) {
                throw new RuntimeException(e);
            }
        }
    }


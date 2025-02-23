package Projet.API;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.hc.core5.http.ParseException;

import java.io.IOException;

public class FlightDataParser {

    public static void parseFlightData(String jsonResponse) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode rootNode = objectMapper.readTree(jsonResponse);

        JsonNode flightsNode = rootNode.path("data");
        for (JsonNode flightNode : flightsNode) {
            String airline = flightNode.path("airline").path("name").asText();
            String flightNumber = flightNode.path("flight").path("number").asText();
            String departureTime = flightNode.path("departure").path("scheduled").asText();
            String arrivalTime = flightNode.path("arrival").path("scheduled").asText();

            System.out.println("Airline: " + airline);
            System.out.println("Flight Number: " + flightNumber);
            System.out.println("Departure Time: " + departureTime);
            System.out.println("Arrival Time: " + arrivalTime);
            System.out.println("-----------------------------");
        }
    }

    public static void main(String[] args) {
        try {
            String jsonResponse = FlightApiClient.getFlights("TUN", "DXB");
            parseFlightData(jsonResponse);
        } catch (IOException | ParseException e) {
            e.printStackTrace();
        }
    }
}

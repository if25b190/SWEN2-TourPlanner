package at.fhtw.tourplanner.service;

import at.fhtw.tourplanner.model.Tour;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.beans.factory.annotation.Value;
import tools.jackson.databind.JsonNode;
import tools.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.LocalTime;
import java.util.List;

@Service
@Transactional
@Slf4j
@RequiredArgsConstructor(onConstructor_ = @Autowired, access = AccessLevel.PROTECTED)
public class OpenRouteService {
    @Value("${openroute.api.key}")
    private String openRouteApiKey;

    public void setGeoInformationOfTour(Tour tour) {
        try(HttpClient httpClient = HttpClient.newHttpClient()) {
            HttpRequest httpRequest = HttpRequest.newBuilder()
                    .uri(URI.create("https://api.openrouteservice.org/v2/directions/"
                            + tour.getTransportType().label
                            + "?api_key="+openRouteApiKey
                            + "&start="+tour.getFrom().getLatitude()+","+tour.getFrom().getLongitude()
                            + "&end="+tour.getTo().getLatitude()+","+tour.getTo().getLongitude()))
                    .GET()
                    .build();

            HttpResponse<String> response = httpClient.send(httpRequest, HttpResponse.BodyHandlers.ofString());
            ObjectMapper mapper = new ObjectMapper();
            JsonNode respJson = mapper.readTree(response.body());
            List<Float[]> wayPoints = new java.util.ArrayList<>(List.of());

            JsonNode coords = respJson.at("/features/0/geometry/coordinates");
            if (coords.isArray()) {
                for (JsonNode point : coords) {
                    float lon = point.get(0).asFloat();
                    float lat = point.get(1).asFloat();

                    wayPoints.add(new Float[]{lon, lat});
                }
            }

            tour.setDistance(respJson.at("/features/0/properties/summary/distance").asFloat());
            LocalTime time = LocalTime.MIN.plusSeconds(respJson.at("/features/0/properties/summary/duration").asLong());
            tour.setEstimatedTime(time);
            tour.setWayPoints(wayPoints);
        } catch (RuntimeException | IOException | InterruptedException e) {
            log.debug("Failed to get geo information of tour.", e);
        }
    }
}

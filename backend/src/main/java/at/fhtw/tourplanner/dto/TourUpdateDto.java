package at.fhtw.tourplanner.dto;

import at.fhtw.tourplanner.model.Tour;
import at.fhtw.tourplanner.model.TransportType;
import lombok.Builder;

import java.sql.Time;

@Builder
public record TourUpdateDto(
        @org.hibernate.validator.constraints.UUID String uuid,
        String name,
        String description,
        Tour.MapPoint from,
        Tour.MapPoint to,
        TransportType transportType,
        Float distance,
        Time estimatedTime
) {
    public static Tour merge(Tour tour, TourUpdateDto tourDto) {
        if (tourDto.name != null && tourDto.name.length() >= 4) {
            tour.setName(tourDto.name);
        }
        if (tourDto.description != null && tourDto.description.length() >= 4) {
            tour.setDescription(tourDto.description);
        }
        if (tourDto.from != null) {
            tour.setFrom(tourDto.from);
        }
        if (tourDto.to != null) {
            tour.setTo(tourDto.to);
        }
        if (tourDto.transportType != null) {
            tour.setTransportType(tourDto.transportType);
        }
        if (tourDto.distance != null) {
            tour.setDistance(tourDto.distance);
        }
        if (tourDto.estimatedTime != null) {
            tour.setEstimatedTime(tourDto.estimatedTime);
        }
        return tour;
    }
}

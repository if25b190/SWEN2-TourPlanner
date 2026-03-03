package at.fhtw.tourplanner.dto;

import at.fhtw.tourplanner.model.Account;
import at.fhtw.tourplanner.model.Tour;
import at.fhtw.tourplanner.model.TransportType;
import lombok.Builder;
import lombok.NonNull;
import org.hibernate.validator.constraints.Length;

import java.sql.Time;
import java.util.Optional;
import java.util.UUID;

@Builder
public record TourDto(
        @org.hibernate.validator.constraints.UUID(allowEmpty = true) String uuid,
        @NonNull @Length(min = 4) String name,
        @NonNull @Length(min = 4) String description,
        @NonNull Tour.MapPoint from,
        @NonNull Tour.MapPoint to,
        @NonNull TransportType transportType,
        @NonNull Float distance,
        @NonNull Time estimatedTime,
        String creator
) {
    public static Tour toEntity(TourDto tourDto) {
        return Tour.builder()
                .uuid(Optional.ofNullable(tourDto.uuid).map(UUID::fromString).orElse(null))
                .name(tourDto.name())
                .description(tourDto.description)
                .from(tourDto.from)
                .to(tourDto.to)
                .transportType(tourDto.transportType)
                .distance(tourDto.distance)
                .estimatedTime(tourDto.estimatedTime)
                .build();
    }

    public static TourDto fromEntity(Tour tour) {
        return new TourDto(
                Optional.ofNullable(tour.getUuid()).map(UUID::toString).orElse(null),
                tour.getName(),
                tour.getDescription(),
                tour.getFrom(),
                tour.getTo(),
                tour.getTransportType(),
                tour.getDistance(),
                tour.getEstimatedTime(),
                tour.getCreator().getUsername()
        );
    }
}
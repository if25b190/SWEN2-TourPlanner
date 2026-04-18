package at.fhtw.tourplanner.dto;

import at.fhtw.tourplanner.model.Tour;
import at.fhtw.tourplanner.model.TransportType;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import org.hibernate.validator.constraints.Length;

import java.util.List;

@Builder
public record TourExportDto(
        @NotNull @Length(min = 4) String name,
        @NotNull @Length(min = 4) String description,
        @NotNull Tour.MapPoint from,
        @NotNull Tour.MapPoint to,
        @NotNull TransportType transportType,
        List<TourLogExportDto> logs
) {
    public static Tour toEntity(TourExportDto tourExportDto) {
        return Tour.builder()
                .name(tourExportDto.name())
                .description(tourExportDto.description)
                .from(tourExportDto.from)
                .to(tourExportDto.to)
                .transportType(tourExportDto.transportType)
                .build();
    }

    public static TourExportDto fromEntity(Tour tour) {

        return new TourExportDto(
                tour.getName(),
                tour.getDescription(),
                tour.getFrom(),
                tour.getTo(),
                tour.getTransportType(),
                tour.getLogs().stream().map(TourLogExportDto::fromEntity).toList()
        );
    }
}

package at.fhtw.tourplanner.dto;

import at.fhtw.tourplanner.model.Difficulty;
import at.fhtw.tourplanner.model.TourLog;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.Builder;
import lombok.NonNull;

import java.time.LocalDateTime;
import java.time.LocalTime;

@Builder
public record TourLogExportDto (
        @NonNull LocalDateTime creationDate,
        @NonNull String comment,
        @NonNull String difficulty,
        @NonNull Float distance,
        @NonNull LocalTime totalTime,
        @NonNull @Min(0) @Max(4) Integer rating
) {
    public static TourLog toEntity(TourLogExportDto tourLogExportDto) {
        return TourLog.builder()
                .creationDate(tourLogExportDto.creationDate)
                .comment(tourLogExportDto.comment)
                .difficulty(Difficulty.valueOf(tourLogExportDto.difficulty))
                .distance(tourLogExportDto.distance)
                .totalTime(tourLogExportDto.totalTime)
                .rating(tourLogExportDto.rating)
                .build();
    }

    public static TourLogExportDto fromEntity(TourLog tourLog) {
        return new  TourLogExportDto(
                tourLog.getCreationDate(),
                tourLog.getComment(),
                tourLog.getDifficulty().name(),
                tourLog.getDistance(),
                tourLog.getTotalTime(),
                tourLog.getRating()
        );
    }
}

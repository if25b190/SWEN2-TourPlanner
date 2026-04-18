package at.fhtw.tourplanner.dto;

import at.fhtw.tourplanner.model.Difficulty;
import at.fhtw.tourplanner.model.TourLog;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.Builder;
import lombok.NonNull;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Optional;
import java.util.UUID;

@Builder
public record TourLogDto (
    @org.hibernate.validator.constraints.UUID(allowEmpty = true) String uuid,
    String creator,
    String tour,
    @NonNull LocalDateTime creationDate,
    @NonNull String comment,
    @NonNull String difficulty,
    @NonNull Float distance,
    @NonNull LocalTime totalTime,
    @NonNull @Min(0) @Max(4) Integer rating
) {
    public static TourLog toEntity(TourLogDto tourLogDto) {
        return TourLog.builder()
                .uuid(Optional.ofNullable(tourLogDto.uuid).map(UUID::fromString).orElse(null))
                .creationDate(tourLogDto.creationDate)
                .comment(tourLogDto.comment)
                .difficulty(Difficulty.valueOf(tourLogDto.difficulty))
                .distance(tourLogDto.distance)
                .totalTime(tourLogDto.totalTime)
                .rating(tourLogDto.rating)
                .build();
    }

    public static TourLogDto fromEntity(TourLog tourLog) {
        return new  TourLogDto(
                Optional.ofNullable(tourLog.getUuid()).map(UUID::toString).orElse(null),
                tourLog.getCreator().getUsername(),
                tourLog.getTour().getUuid().toString(),
                tourLog.getCreationDate(),
                tourLog.getComment(),
                tourLog.getDifficulty().name(),
                tourLog.getDistance(),
                tourLog.getTotalTime(),
                tourLog.getRating()
        );
    }
}

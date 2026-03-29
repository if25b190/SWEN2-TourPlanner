package at.fhtw.tourplanner.dto;

import at.fhtw.tourplanner.model.Difficulty;
import at.fhtw.tourplanner.model.TourLog;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.Builder;
import lombok.NonNull;

import java.sql.Time;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.UUID;

@Builder
public record TourLogUpdateDto (
        @org.hibernate.validator.constraints.UUID(allowEmpty = true) String uuid,
        String creator,
        String tour,
        LocalDateTime creationDate,
        String comment,
        String difficulty,
        Float distance,
        LocalTime totalTime,
        Integer rating
) {
    public static TourLog merge(TourLog tourLog, TourLogUpdateDto tourLogUpdateDto) {
        if(tourLogUpdateDto.comment != null) {
            tourLog.setComment(tourLogUpdateDto.comment);
        }
        if(tourLogUpdateDto.difficulty != null) {
            tourLog.setDifficulty(Difficulty.valueOf(tourLogUpdateDto.difficulty));
        }
        if(tourLogUpdateDto.distance != null) {
            tourLog.setDistance(tourLogUpdateDto.distance);
        }
        if(tourLogUpdateDto.totalTime != null) {
            tourLog.setTotalTime(tourLogUpdateDto.totalTime);
        }
        if(tourLogUpdateDto.rating != null && (tourLogUpdateDto.rating > 0 && tourLogUpdateDto.rating <= 4)) {
            tourLog.setRating(tourLogUpdateDto.rating);
        }
        return tourLog;
    }
}

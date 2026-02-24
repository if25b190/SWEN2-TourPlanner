package at.fhtw.tourplanner.model;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.sql.Time;
import java.time.LocalDateTime;

@Entity
@Data
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@SuperBuilder
@EqualsAndHashCode(callSuper = false)
@ToString
public class TourLog extends GlobalEntity {
    @NonNull
    private String creator;
    @NonNull
    private Integer tourId;
    @NonNull
    private LocalDateTime creationDate;
    @NonNull
    private String comment;
    @NonNull
    @Enumerated(EnumType.STRING)
    private Difficulty difficulty;
    @NonNull
    private Float distance;
    @NonNull
    private Time totalTime;
    @NonNull
    @Min(0)
    @Max(4)
    private Integer rating;
}

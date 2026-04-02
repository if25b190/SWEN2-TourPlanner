package at.fhtw.tourplanner.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.sql.Time;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Entity
@Data
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@SuperBuilder
@EqualsAndHashCode(callSuper = false)
@ToString
public class TourLog extends GlobalEntity {
    @ManyToOne(cascade = CascadeType.REFRESH, optional = false)
    private Account creator;
    @ManyToOne(cascade = CascadeType.REFRESH, optional = false, fetch = FetchType.LAZY)
    private Tour tour;
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
    private LocalTime totalTime;
    @NonNull
    @Min(0)
    @Max(4)
    private Integer rating;
}

package at.fhtw.tourplanner.model;

import jakarta.persistence.*;
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
    @ManyToOne(cascade = CascadeType.REFRESH)
    private Account creator;
    @NonNull
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
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
    private Time totalTime;
    @NonNull
    @Min(0)
    @Max(4)
    private Integer rating;
}

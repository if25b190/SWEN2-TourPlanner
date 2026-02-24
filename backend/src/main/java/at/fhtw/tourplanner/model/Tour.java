package at.fhtw.tourplanner.model;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.hibernate.validator.constraints.Length;

import java.sql.Time;
import java.util.ArrayList;

@Entity
@Data
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@SuperBuilder
@EqualsAndHashCode(callSuper = false)
@ToString
public class Tour extends GlobalEntity {
    @Length(min = 4)
    @NonNull
    private String name;
    @Length(min = 4)
    @NonNull
    private String description;
    //From
    //to
    @NonNull
    @Enumerated(EnumType.STRING)
    private TransportType transportType;
    @NonNull
    private Float distance;
    @NonNull
    private Time estimatedTime;
    private ArrayList<TourLog> logs;
    @NonNull
    private String creator;
}

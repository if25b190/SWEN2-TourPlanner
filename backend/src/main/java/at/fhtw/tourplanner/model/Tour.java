package at.fhtw.tourplanner.model;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.hibernate.validator.constraints.Length;

import java.io.Serializable;
import java.sql.Time;
import java.util.ArrayList;

@Entity
@Data
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@SuperBuilder
@EqualsAndHashCode(callSuper = false)
@ToString
public class Tour extends GlobalEntity implements Serializable {
    @Length(min = 4)
    @NonNull
    private String name;
    @Length(min = 4)
    @NonNull
    private String description;
    @NonNull
    @Embedded
    private MapPoint from;
    @NonNull
    @Embedded
    private MapPoint to;
    @NonNull
    @Enumerated(EnumType.STRING)
    private TransportType transportType;
    @NonNull
    private Float distance;
    @NonNull
    private Time estimatedTime;
    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private ArrayList<TourLog> logs;
    @NonNull
    private String creator;

    @Embeddable
    public static class MapPoint {
        @NonNull
        private Float longitude;
        @NonNull
        private Float latitude;
    }
}

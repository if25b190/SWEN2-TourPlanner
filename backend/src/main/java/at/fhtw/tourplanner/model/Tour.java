package at.fhtw.tourplanner.model;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.hibernate.validator.constraints.Length;

import java.io.Serializable;
import java.sql.Time;
import java.util.List;

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
    @AttributeOverrides({
            @AttributeOverride(name = "longitude", column = @Column(name = "from_longitude")),
            @AttributeOverride(name = "latitude", column = @Column(name = "from_latitude"))
    })
    private MapPoint from;
    @NonNull
    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "longitude", column = @Column(name = "to_longitude")),
            @AttributeOverride(name = "latitude", column = @Column(name = "to_latitude"))
    })
    private MapPoint to;
    @NonNull
    @Enumerated(EnumType.STRING)
    private TransportType transportType;
    @NonNull
    private Float distance;
    @NonNull
    private Time estimatedTime;
    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<TourLog> logs;
    @NonNull
    private String creator;

    @Data
    @NoArgsConstructor
    @SuperBuilder
    @Embeddable
    public static class MapPoint {
        @NonNull
        private Float longitude;
        @NonNull
        private Float latitude;
    }
}

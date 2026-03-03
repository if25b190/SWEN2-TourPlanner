package at.fhtw.tourplanner.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.hibernate.validator.constraints.Length;

import java.sql.Time;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

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
    @OneToMany(cascade = CascadeType.REFRESH, fetch = FetchType.LAZY, mappedBy = "tour")
    @ToString.Exclude
    @JsonIgnore
    @Builder.Default
    private List<TourLog> logs = new ArrayList<>();
    @ManyToOne(cascade = CascadeType.REFRESH, optional = false)
    private Account creator;

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

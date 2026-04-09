package at.fhtw.tourplanner.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.hibernate.validator.constraints.Length;

import java.sql.Time;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

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
    private Float distance;
    private Time estimatedTime;
    @OneToMany(cascade = CascadeType.REFRESH, fetch = FetchType.LAZY, mappedBy = "tour")
    @ToString.Exclude
    @JsonIgnore
    @Builder.Default
    private List<TourLog> logs = new ArrayList<>();
    @ManyToOne(cascade = CascadeType.REFRESH, optional = false)
    private Account creator;
    @Transient
    private Integer popularity;
    @Transient
    private Integer childfriendliness;

    public List<TourLog> getLogs() {return Collections.unmodifiableList(logs);}

    public TourLog addLog(TourLog log) {
        if(log == null || logs.contains(log)) {
            return null;
        }

        log.setTour(this);
        log.setCreator(this.creator);
        logs.add(log);
        return log;
    }

    public Integer getPopularity() {
        return this.logs.size();
    }

    public Integer getChildfriendliness() {
        return this.logs.stream().filter(x -> x.getDifficulty().equals(Difficulty.Easy) &&
                x.getDistance() <= 10 &&
                x.getTotalTime().isBefore(LocalTime.of(5,0)))
                .toList().size();
    }

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

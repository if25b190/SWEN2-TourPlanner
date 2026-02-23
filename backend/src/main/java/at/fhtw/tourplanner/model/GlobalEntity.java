package at.fhtw.tourplanner.model;

import jakarta.persistence.Entity;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.domain.AbstractPersistable;

import java.util.UUID;

@Entity
@Getter
public abstract class GlobalEntity extends AbstractPersistable<Long> {
    /*@NotNull
    private final String uuid = UUID.randomUUID().toString();*/
}

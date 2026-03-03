package at.fhtw.tourplanner.repository;

import at.fhtw.tourplanner.model.TourLog;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface TourLogRepository extends CrudRepository<TourLog, String> {
    Optional<TourLog> getTourLogByUuid(UUID uuid);
}

package at.fhtw.tourplanner.repository;

import at.fhtw.tourplanner.model.TourLog;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface TourLogRepository extends CrudRepository<TourLog, String> {
    Optional<TourLog> getTourLogByUuid(UUID uuid);
    @Query(value = "select l from TourLog l where (trim(:searchTerm) = '' or " +
            "upper(l.comment) like upper(concat('%',:searchTerm,'%')) or "+
            "upper(l.difficulty) like upper(concat('%',:searchTerm,'%')))")
    Page<TourLog> findAllWithSearch(@Param("searchTerm") String searchTerm, Pageable pageable);
}

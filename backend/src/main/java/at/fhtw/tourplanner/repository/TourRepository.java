package at.fhtw.tourplanner.repository;

import at.fhtw.tourplanner.model.Tour;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface TourRepository extends CrudRepository<Tour, String> {
    Optional<Tour> getTourByUuid(UUID uuid);
    @Query(value = "select t from Tour t where (trim(:searchTerm) = '' or " +
            "upper(t.name) like upper(concat('%',:searchTerm,'%')) or " +
            "upper(t.description) like upper(concat('%',:searchTerm,'%')))")
    Page<Tour> findAllWithSearch(@Param("searchTerm") String searchTerm, Pageable pageable);
}

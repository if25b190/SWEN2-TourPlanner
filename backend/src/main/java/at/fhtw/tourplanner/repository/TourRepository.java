package at.fhtw.tourplanner.repository;

import at.fhtw.tourplanner.model.Tour;
import org.hibernate.annotations.processing.SQL;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface TourRepository extends CrudRepository<Tour, String> {
    Optional<ArrayList<Tour>> getAllToursOfUser(String creator);
    Optional<Tour> getTourByUuid(UUID uuid);
    //TODO add own stmt
    @Query()
    Long deleteTourByUuid(UUID uuid);
//    void deleteTourByCreator(String creator);
//    Optional<Tour> updateTourByCreator(String creator, Tour tourToUpdate);
//    Tour createTour(Tour tourToCreate);
}

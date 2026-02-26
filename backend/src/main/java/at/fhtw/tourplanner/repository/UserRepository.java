package at.fhtw.tourplanner.repository;

import at.fhtw.tourplanner.model.Account;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends CrudRepository<Account, String> {
    Optional<Account> findByUsername(String username);
}

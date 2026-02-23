package at.fhtw.tourplanner.repository;

import at.fhtw.tourplanner.model.Account;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface UserRepository extends CrudRepository<Account, String> {
    Optional<Account> findByUsername(String s);
}

package at.fhtw.tourplanner.repository;

import at.fhtw.tourplanner.model.Account;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserRepository extends CrudRepository<Account, String> {
    List<Account> findAll();
    Optional<Account> findByUuid(UUID uuid);
    Optional<Account> findByUsername(String username);
}

package at.fhtw.tourplanner.service;

import at.fhtw.tourplanner.dto.LoginDto;
import at.fhtw.tourplanner.model.Account;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.test.autoconfigure.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@AutoConfigureTestDatabase
public class UserServiceTest {

    private static final Account account = Account.builder()
            .username("username")
            .password("password")
            .build();
    @Autowired
    private UserService userService;

    @Test
    void testRegisterUser() {
        userService.registerUser(new LoginDto(null, account.getUsername(), account.getPassword()));
        var result = userService.loadUserByUsername(account.getUsername());
        assertTrue(result.isEnabled());
        assertNotEquals(result.getPassword(), account.getPassword());
    }

}

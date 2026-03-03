package at.fhtw.tourplanner.util;

import at.fhtw.tourplanner.exeception.UnknownUserException;
import at.fhtw.tourplanner.model.Account;
import org.springframework.security.core.Authentication;

public class PrincipalCheckUtil {
    public static Account getPrincipal(Authentication authentication) {
        if (authentication != null &&
                authentication.isAuthenticated() &&
                authentication.getPrincipal() instanceof Account account &&
                account.getUuid() != null) {
            return account;
        }
        throw new UnknownUserException("Unknown user");
    }
}

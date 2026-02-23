package at.fhtw.tourplanner.service;

import at.fhtw.tourplanner.dto.LoginDto;
import at.fhtw.tourplanner.model.Account;
import at.fhtw.tourplanner.repository.UserRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor(onConstructor_ = @Autowired, access = AccessLevel.PROTECTED)
public class UserService implements UserDetailsService {
    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        var user = userRepository.findByUsername(username);
        if (user.isPresent()) {
            return user.get();
        }
        throw new UsernameNotFoundException("User not found");
    }

    public Optional<Account> registerUser(LoginDto loginDto) {
        if (userRepository.findByUsername(loginDto.username()).isPresent()) {
            return Optional.empty();
        }
        var account = Account.builder()
                .username(loginDto.username())
                .password(loginDto.password())
                .build();
        userRepository.save(account);
        return Optional.of(account);
    }
}

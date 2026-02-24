package at.fhtw.tourplanner.dto;

import at.fhtw.tourplanner.model.Account;
import jakarta.validation.constraints.NotNull;
import org.hibernate.validator.constraints.Length;

public record LoginDto(
        String uuid,
        @NotNull @Length(min = 4) String username,
        @NotNull @Length(min = 8) String password
) {
    public static Account toEntity(LoginDto loginDto) {
        return Account.builder()
                .username(loginDto.username())
                .password(loginDto.password())
                .build();
    }

    public static LoginDto fromEntity(Account account) {
        return new LoginDto(
                account.getUuid().toString(),
                account.getUsername(),
                null
        );
    }
}

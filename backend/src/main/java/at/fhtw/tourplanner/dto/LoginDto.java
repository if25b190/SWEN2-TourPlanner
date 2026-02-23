package at.fhtw.tourplanner.dto;

import jakarta.validation.constraints.NotNull;
import org.hibernate.validator.constraints.Length;

public record LoginDto(
        @NotNull @Length(min = 4) String username,
        @NotNull @Length(min = 8) String password
) {
}

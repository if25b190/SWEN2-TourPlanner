package at.fhtw.tourplanner.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.hibernate.validator.constraints.Length;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

@Entity
@Data
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@SuperBuilder
@EqualsAndHashCode(callSuper = false)
@ToString
public class Account extends GlobalEntity implements UserDetails {
    @NotNull
    @Length(min = 4)
    private String username;
    @NotNull
    @Length(min = 8)
    private String password;
    @NotNull
    @Builder.Default
    @Enumerated(EnumType.STRING)
    private Role role = Role.ROLE_USER;
    @NotNull
    @Builder.Default
    private boolean locked = false;
    @NotNull
    @Builder.Default
    private boolean enabled = true;
    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Tour> tours;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        SimpleGrantedAuthority authority = new SimpleGrantedAuthority(Role.ROLE_USER.name());
        if (role != Role.ROLE_USER) {
            return List.of(authority, new SimpleGrantedAuthority(role.name()));
        }
        return List.of(authority);
    }

    @Override
    public boolean isAccountNonExpired() {
        return UserDetails.super.isAccountNonExpired();
    }

    @Override
    public boolean isAccountNonLocked() {
        return !locked;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return UserDetails.super.isCredentialsNonExpired();
    }

    @Override
    public boolean isEnabled() {
        return enabled;
    }
}

package at.fhtw.tourplanner.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.hibernate.validator.constraints.Length;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

@Entity
@Data
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@SuperBuilder
@EqualsAndHashCode(callSuper = false)
@ToString(onlyExplicitlyIncluded = true)
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
    @OneToMany(cascade = CascadeType.REFRESH, fetch = FetchType.LAZY, mappedBy = "creator")
    @ToString.Exclude
    @JsonIgnore
    @Builder.Default
    private List<Tour> tours = new ArrayList<>();

    public List<Tour> getTours() {
        return Collections.unmodifiableList(tours);
    }

    public Tour addTour(Tour tour) {
        if (tour == null || tours.contains(tour)) {
            return null;
        }
        tour.setCreator(this);
        tours.add(tour);
        return tour;
    }

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

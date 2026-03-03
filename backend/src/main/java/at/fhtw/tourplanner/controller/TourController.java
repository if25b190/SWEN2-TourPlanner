package at.fhtw.tourplanner.controller;

import at.fhtw.tourplanner.dto.TourDto;
import at.fhtw.tourplanner.dto.TourUpdateDto;
import at.fhtw.tourplanner.service.TourService;
import at.fhtw.tourplanner.util.PrincipalCheckUtil;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping(path = "/api/v1/tours")
@RequiredArgsConstructor(onConstructor_ = @Autowired, access = AccessLevel.PROTECTED)
public class TourController {
    private final TourService tourService;

    @GetMapping("")
    public ResponseEntity<List<TourDto>> getAllTours(Authentication authentication) {
        var account = PrincipalCheckUtil.getPrincipal(authentication);
        var result = tourService.getAllToursOfUser(account);

        return ResponseEntity.ok(result);
    }

    @GetMapping("/{uuid}")
    public ResponseEntity<TourDto> getTour(@PathVariable String uuid, Authentication authentication) {
        var account = PrincipalCheckUtil.getPrincipal(authentication);
        var result = tourService.getTourByUuid(UUID.fromString(uuid), account);

        return result.map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    @PostMapping("")
    public ResponseEntity<Optional<TourDto>> addTour(@Valid @RequestBody TourDto tourDto, Authentication authentication) {
        var account = PrincipalCheckUtil.getPrincipal(authentication);
        var result = tourService.addTour(tourDto, account);
        return result.map(_ -> ResponseEntity.created(URI.create("/api/v1/tours")).body(result))
                .orElse(ResponseEntity.badRequest().build());
    }

    @PutMapping("")
    public ResponseEntity<TourDto> updateTour(@Valid @RequestBody TourUpdateDto tourUpdateDto, Authentication authentication) {
        var account = PrincipalCheckUtil.getPrincipal(authentication);
        var result = tourService.updateTour(tourUpdateDto, account);
        return result.map(_ -> ResponseEntity.of(result))
                .orElse(ResponseEntity.badRequest().build());
    }

    @DeleteMapping("/{uuid}")
    public ResponseEntity<String> deleteTour(@PathVariable String uuid, Authentication authentication) {
        var account = PrincipalCheckUtil.getPrincipal(authentication);
        var result = tourService.deleteTour(UUID.fromString(uuid), account);
        if (result.isPresent()) {
            return ResponseEntity.ok(null);
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}

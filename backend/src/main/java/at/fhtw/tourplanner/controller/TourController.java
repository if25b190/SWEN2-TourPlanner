package at.fhtw.tourplanner.controller;

import at.fhtw.tourplanner.dto.TourDto;
import at.fhtw.tourplanner.service.TourService;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.ArrayList;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping(path = "/api/v1/tours")
@RequiredArgsConstructor(onConstructor_ = @Autowired, access = AccessLevel.PROTECTED)
public class TourController {
    private final TourService tourService;

    @GetMapping("/{username}")
    public ResponseEntity<ArrayList<TourDto>> getAllTours(@PathVariable String username) {
        var result = tourService.getAllToursOfUser(username);

        if (result.isEmpty()) {
            ResponseEntity.notFound();
        }

        return ResponseEntity.of(result);
    }

    @GetMapping("/{username}/{uuid}")
    public ResponseEntity<TourDto> getTour(@PathVariable String username, @PathVariable String uuid) {
        var result = tourService.getTourByUuid(UUID.fromString(uuid));

        if (result.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        TourDto dto = result.get();

        if(dto.creator().equals(username)) {
            return ResponseEntity.of(result);
        }

        return ResponseEntity.status(403).body(null);
    }

    @PostMapping("")
    public ResponseEntity<Optional<TourDto>> addTour(@Valid @RequestBody TourDto tourDto) {
        var result = tourService.addTour(tourDto);
        return result.map(_ -> ResponseEntity.created(URI.create("/api/v1/tours")).body(result))
                .orElse(ResponseEntity.badRequest().build());
    }

    //TODO
    @PutMapping("")
    public ResponseEntity<TourDto> updateTour(@Valid @RequestBody TourDto tourDto) {
        var result = tourService.updateTour(tourDto);
        return result.map(_ -> ResponseEntity.of(result))
                .orElse(ResponseEntity.badRequest().build());
    }

    //TODO
    @DeleteMapping("/{uuid}")
    public ResponseEntity<String> deleteTour(@PathVariable String uuid) {
        var result = tourService.deleteTour(UUID.fromString(uuid));
        if(result > 0) {
            return ResponseEntity.ok("Tour deleted");
        } else {
            return ResponseEntity.badRequest().build();
        }
    }
}

package at.fhtw.tourplanner.controller;

import at.fhtw.tourplanner.dto.LoginDto;
import at.fhtw.tourplanner.dto.TourDto;
import at.fhtw.tourplanner.model.Tour;
import at.fhtw.tourplanner.service.TourService;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.UUID;

@RestController
@RequestMapping(path = "/api/v1/tours")
@RequiredArgsConstructor(onConstructor_ = @Autowired, access = AccessLevel.PROTECTED)
public class TourController {
    private final TourService tourService;

    @GetMapping("")
    public ResponseEntity<String> getAllTours() {
        return ResponseEntity.ok("Hallo");
    }

    //TODO
    @PostMapping("")
    public ResponseEntity<TourDto> addTour(@Valid @RequestBody TourDto tourDto) {
        var result = tourService.addTour(tourDto);
        return result.map(dto -> ResponseEntity.created(URI.create("/api/v1/tours")).body(tourDto))
                .orElse(ResponseEntity.badRequest().build());
    }

    //TODO
    @PutMapping("")
    public ResponseEntity<TourDto> updateTour(@Valid @RequestBody TourDto tourDto) {
        var result = tourService.updateTour(tourDto);
        return result.map(dto -> ResponseEntity.created(URI.create("/api/v1/tours")).body(tourDto))
                .orElse(ResponseEntity.badRequest().build());
    }

    //TODO
    @DeleteMapping("/{uuid}")
    public ResponseEntity<String> deleteTour(@PathVariable String uuid) {
        var result = tourService.deleteTour(uuid);
        if(result > 0) {
            return ResponseEntity.ok("Tour deleted");

        }
        else
            return ResponseEntity.badRequest().build();
    }
}

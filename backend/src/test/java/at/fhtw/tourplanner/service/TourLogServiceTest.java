package at.fhtw.tourplanner.service;

import at.fhtw.tourplanner.dto.*;
import at.fhtw.tourplanner.model.Account;
import at.fhtw.tourplanner.model.Difficulty;
import at.fhtw.tourplanner.model.Tour;
import at.fhtw.tourplanner.model.TransportType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.test.autoconfigure.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@AutoConfigureTestDatabase
@Transactional
public class TourLogServiceTest {

    private static final LoginDto loginDto = new LoginDto(null, "username", "password");
    TourLogDto log;
    private static final TourDto tour = TourDto.builder().uuid(null).name("Donau").description("Donau").from(Tour.MapPoint.builder().longitude(2.f).latitude(4.f).build()).to(Tour.MapPoint.builder().longitude(3.f).latitude(4.f).build()).transportType(TransportType.Hiking).distance(2f).estimatedTime(LocalTime.now()).creator("").popularity(null).childfriendliness(null).wayPoints(null).build();

    @Autowired
    private UserService userService;
    @Autowired
    private TourLogService tourLogService;
    @Autowired
    private TourService tourService;

    @BeforeEach
    void setup() {
        userService.registerUser(loginDto);
        var result = userService.loadUserByUsername(loginDto.username());
        assertTrue(result.isEnabled());


        var res = tourService.addTour(tour, (Account) userService.loadUserByUsername(loginDto.username()));
        res.ifPresent(tourDto -> log = TourLogDto.builder().uuid(null).creator("").tour(tourDto.uuid()).creationDate(LocalDateTime.now()).comment("Cool").difficulty(Difficulty.Hard.name()).distance(10.5F).totalTime(LocalTime.now()).rating(3).build());
    }

    @Test
    void testAddTourLog() {

        var result = tourLogService.addTourLog(log, (Account) userService.loadUserByUsername(loginDto.username()));
        assertTrue(result.isPresent());
        assertNotNull(result.get().uuid());
    }

    @Test
    void testDeleteTourLog() {
        var result = tourLogService.addTourLog(log, (Account) userService.loadUserByUsername(loginDto.username()));
        assertTrue(result.isPresent());
        assertNotNull(result.get().uuid());
        tourLogService.deleteTourLog(UUID.fromString(result.get().uuid()), (Account) userService.loadUserByUsername(loginDto.username()));
        var searchForDeletedTour = tourLogService.getTourLogByUuid(UUID.fromString(result.get().uuid()), (Account) userService.loadUserByUsername(loginDto.username()));
        assertFalse(searchForDeletedTour.isPresent());
    }

    @Test
    void testUpdateTourLog() {
        var result = tourLogService.addTourLog(log, (Account) userService.loadUserByUsername(loginDto.username()));
        assertTrue(result.isPresent());
        assertNotNull(result.get().uuid());
        TourLogUpdateDto updateLogDto = TourLogUpdateDto.builder()
                .uuid(result.get().uuid())
                .creator("")
                .tour("1234")
                .creationDate(LocalDateTime.now())
                .comment("Cool2")
                .difficulty(Difficulty.Hard.name())
                .distance(11.5F)
                .totalTime(LocalTime.now())
                .rating(4)
                .build();
        var updateResult = tourLogService.updateTourLog(updateLogDto, (Account) userService.loadUserByUsername(loginDto.username()));
        assertTrue(updateResult.isPresent());
        assertEquals(updateLogDto.comment(), updateResult.get().comment());
        assertEquals(updateLogDto.distance(), updateResult.get().distance());
        assertEquals(updateLogDto.difficulty(), updateResult.get().difficulty());
    }

    @Test
    void testUpdateTour_TourLogDoesNotExist() {
        assertThrows(IllegalArgumentException.class, () -> {
            TourLogUpdateDto updateLogDto = TourLogUpdateDto.builder()
                    .uuid("1234")
                    .creator("")
                    .tour("1234")
                    .creationDate(LocalDateTime.now())
                    .comment("Cool2")
                    .difficulty(Difficulty.Hard.name())
                    .distance(11.5F)
                    .totalTime(LocalTime.now())
                    .rating(4)
                    .build();
            tourLogService.updateTourLog(updateLogDto, (Account) userService.loadUserByUsername(loginDto.username()));
        });
    }
}
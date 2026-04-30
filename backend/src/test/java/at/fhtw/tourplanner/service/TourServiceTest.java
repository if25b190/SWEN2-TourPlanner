package at.fhtw.tourplanner.service;

import at.fhtw.tourplanner.dto.LoginDto;
import at.fhtw.tourplanner.dto.TourDto;
import at.fhtw.tourplanner.dto.TourUpdateDto;
import at.fhtw.tourplanner.model.Account;
import at.fhtw.tourplanner.model.Tour;
import at.fhtw.tourplanner.model.TransportType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.test.autoconfigure.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalTime;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@AutoConfigureTestDatabase
@Transactional
public class TourServiceTest {

    private static final LoginDto loginDto = new LoginDto(null, "username", "password");
    private static final TourDto tour = TourDto.builder().uuid(null).name("Donau").description("Donau").from(Tour.MapPoint.builder().longitude(2.f).latitude(4.f).build()).to(Tour.MapPoint.builder().longitude(3.f).latitude(4.f).build()).transportType(TransportType.Hiking).distance(2f).estimatedTime(LocalTime.now()).creator("").popularity(null).childfriendliness(null).wayPoints(null).build();
    private static final TourDto tour2 = TourDto.builder().uuid(null).name("Test").description("1234").from(Tour.MapPoint.builder().longitude(2.f).latitude(4.f).build()).to(Tour.MapPoint.builder().longitude(3.f).latitude(4.f).build()).transportType(TransportType.Hiking).distance(2f).estimatedTime(LocalTime.now()).creator("").popularity(null).childfriendliness(null).wayPoints(null).build();

    @Autowired
    private UserService userService;
    @Autowired
    private TourService tourService;

    @BeforeEach
    void setup() {
        userService.registerUser(loginDto);
        var result = userService.loadUserByUsername(loginDto.username());
        assertTrue(result.isEnabled());
    }

    @Test
    void testAddTour() {
        var result = tourService.addTour(tour, (Account) userService.loadUserByUsername(loginDto.username()));
        assertTrue(result.isPresent());
        assertNotNull(result.get().uuid());
    }

    @Test
    void testAddAnotherTour() {
        var result = tourService.addTour(tour2, (Account) userService.loadUserByUsername(loginDto.username()));
        assertTrue(result.isPresent());
        assertNotNull(result.get().uuid());
        System.out.println(userService.findAll().size());
    }

    @Test
    void testDeleteTour() {
        var result = tourService.addTour(tour2, (Account) userService.loadUserByUsername(loginDto.username()));
        assertTrue(result.isPresent());
        assertNotNull(result.get().uuid());
        tourService.deleteTour(UUID.fromString(result.get().uuid()), (Account) userService.loadUserByUsername(loginDto.username()));
        var searchForDeletedTour = tourService.getTourByUuid(UUID.fromString(result.get().uuid()), (Account) userService.loadUserByUsername(loginDto.username()));
        assertFalse(searchForDeletedTour.isPresent());
    }

    @Test
    void testUpdateTour() {
        var result = tourService.addTour(tour2, (Account) userService.loadUserByUsername(loginDto.username()));
        assertTrue(result.isPresent());
        assertNotNull(result.get().uuid());
        TourUpdateDto tourUpdate = TourUpdateDto.builder()
                .uuid(result.get().uuid())
                .name("Test2")
                .description("1234")
                .from(Tour.MapPoint.builder().longitude(2.f).latitude(4.f).build())
                .to(Tour.MapPoint.builder().longitude(3.f).latitude(4.f).build())
                .transportType(TransportType.Hiking)
                .build();
        var updateResult = tourService.updateTour(tourUpdate, (Account) userService.loadUserByUsername(loginDto.username()));
        assertTrue(updateResult.isPresent());
        assertEquals(tourUpdate.name(), updateResult.get().name());
    }

    @Test
    void testUpdateTour_TourDoesNotExist() {
        assertThrows(IllegalArgumentException.class, () -> {
            TourUpdateDto tourUpdate = TourUpdateDto.builder()
                    .uuid("12334553")
                    .name("Test2")
                    .description("1234")
                    .from(Tour.MapPoint.builder().longitude(2.f).latitude(4.f).build())
                    .to(Tour.MapPoint.builder().longitude(3.f).latitude(4.f).build())
                    .transportType(TransportType.Hiking)
                    .build();
            tourService.updateTour(tourUpdate, (Account) userService.loadUserByUsername(loginDto.username()));
        });
    }
}
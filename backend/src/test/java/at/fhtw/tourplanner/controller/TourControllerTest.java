package at.fhtw.tourplanner.controller;

import at.fhtw.tourplanner.TourplannerApplicationTests;
import at.fhtw.tourplanner.dto.TourDto;
import at.fhtw.tourplanner.dto.TourUpdateDto;
import at.fhtw.tourplanner.model.Account;
import at.fhtw.tourplanner.model.Tour;
import at.fhtw.tourplanner.model.TransportType;
import at.fhtw.tourplanner.service.TourService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import tools.jackson.databind.ObjectMapper;

import java.time.LocalTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@SpringBootTest(
        webEnvironment = SpringBootTest.WebEnvironment.MOCK,
        classes = TourplannerApplicationTests.UserMockConfig.class
)
@AutoConfigureMockMvc
public class TourControllerTest {
    private static final String API_URL = "http://localhost:8080/api/v1/tours";
    private static final TourDto TOUR_DTO = TourDto.builder()
            .uuid(null)
            .name("Donau")
            .description("Donau")
            .from(Tour.MapPoint.builder().longitude(2.f).latitude(4.f).build())
            .to(Tour.MapPoint.builder().longitude(3.f).latitude(4.f).build())
            .transportType(TransportType.Hiking)
            .distance(2f)
            .estimatedTime(LocalTime.now())
            .creator("")
            .popularity(null)
            .childfriendliness(null)
            .wayPoints(null)
            .build();

    @MockitoBean
    private TourService tourService;
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void testGetAllTours_whenNotLoggedIn() throws Exception {
        when(tourService.getAllToursOfUser(ArgumentCaptor.forClass(Account.class).capture())).thenReturn(List.of());
        mockMvc.perform(
                        MockMvcRequestBuilders
                                .get(API_URL)
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isUnauthorized());
        verify(tourService, never()).getAllToursOfUser(ArgumentCaptor.forClass(Account.class).capture());
    }

    @Test
    @WithUserDetails("User")
    void testGetAllTours_whenLoggedIn() throws Exception {
        when(tourService.getAllToursOfUser(ArgumentCaptor.forClass(Account.class).capture())).thenReturn(List.of());
        mockMvc.perform(
                        MockMvcRequestBuilders
                                .get(API_URL)
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andExpect(content().json("[]"));
        verify(tourService, atLeastOnce()).getAllToursOfUser(ArgumentCaptor.forClass(Account.class).capture());
    }

    @Test
    void testAddTour_whenNotLoggedIn() throws Exception {
        when(tourService.addTour(ArgumentCaptor.forClass(TourDto.class).capture(), ArgumentCaptor.forClass(Account.class).capture()))
                .thenReturn(Optional.of(TOUR_DTO));
        mockMvc.perform(
                        MockMvcRequestBuilders
                                .post(API_URL)
                                .content(objectMapper.writeValueAsString(TOUR_DTO))
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isUnauthorized());
        verify(tourService, never())
                .addTour(ArgumentCaptor.forClass(TourDto.class).capture(), ArgumentCaptor.forClass(Account.class).capture());
    }

    @Test
    @WithUserDetails("User")
    void testAddTour_whenLoggedIn() throws Exception {
        when(tourService.addTour(ArgumentCaptor.forClass(TourDto.class).capture(), ArgumentCaptor.forClass(Account.class).capture()))
                .thenReturn(Optional.of(TOUR_DTO));
        mockMvc.perform(
                        MockMvcRequestBuilders
                                .post(API_URL)
                                .content(objectMapper.writeValueAsString(TOUR_DTO))
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isCreated());
        verify(tourService, atLeastOnce())
                .addTour(ArgumentCaptor.forClass(TourDto.class).capture(), ArgumentCaptor.forClass(Account.class).capture());
    }

    @Test
    void testUpdateTour_whenNotLoggedIn() throws Exception {
        when(tourService.updateTour(ArgumentCaptor.forClass(TourUpdateDto.class).capture(), ArgumentCaptor.forClass(Account.class).capture()))
                .thenReturn(Optional.of(TOUR_DTO));
        mockMvc.perform(
                        MockMvcRequestBuilders
                                .put(API_URL)
                                .content(objectMapper.writeValueAsString(TOUR_DTO))
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isUnauthorized());
        verify(tourService, never())
                .updateTour(ArgumentCaptor.forClass(TourUpdateDto.class).capture(), ArgumentCaptor.forClass(Account.class).capture());
    }

    @Test
    @WithUserDetails("User")
    void testUpdateTour_whenLoggedIn() throws Exception {
        when(tourService.updateTour(ArgumentCaptor.forClass(TourUpdateDto.class).capture(), ArgumentCaptor.forClass(Account.class).capture()))
                .thenReturn(Optional.of(TOUR_DTO));
        mockMvc.perform(
                        MockMvcRequestBuilders
                                .put(API_URL)
                                .content(objectMapper.writeValueAsString(TOUR_DTO))
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk());
        verify(tourService, atLeastOnce())
                .updateTour(ArgumentCaptor.forClass(TourUpdateDto.class).capture(), ArgumentCaptor.forClass(Account.class).capture());
    }

    @Test
    void testDeleteTour_whenNotLoggedIn() throws Exception {
        when(tourService.deleteTour(ArgumentCaptor.forClass(UUID.class).capture(), ArgumentCaptor.forClass(Account.class).capture()))
                .thenReturn(Optional.of(1));
        mockMvc.perform(
                        MockMvcRequestBuilders
                                .delete(API_URL + "/" + UUID.randomUUID())
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isUnauthorized());
        verify(tourService, never())
                .deleteTour(ArgumentCaptor.forClass(UUID.class).capture(), ArgumentCaptor.forClass(Account.class).capture());
    }

    @Test
    @WithUserDetails("User")
    void testDeleteTour_whenLoggedIn() throws Exception {
        when(tourService.deleteTour(ArgumentCaptor.forClass(UUID.class).capture(), ArgumentCaptor.forClass(Account.class).capture()))
                .thenReturn(Optional.of(1));
        mockMvc.perform(
                        MockMvcRequestBuilders
                                .delete(API_URL + "/" + UUID.randomUUID())
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isNoContent());
        verify(tourService, atLeastOnce())
                .deleteTour(ArgumentCaptor.forClass(UUID.class).capture(), ArgumentCaptor.forClass(Account.class).capture());
    }
}

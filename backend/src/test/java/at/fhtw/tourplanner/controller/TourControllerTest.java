package at.fhtw.tourplanner.controller;

import at.fhtw.tourplanner.TourplannerApplicationTests;
import at.fhtw.tourplanner.service.TourLogService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@SpringBootTest(
        webEnvironment = SpringBootTest.WebEnvironment.MOCK,
        classes = TourplannerApplicationTests.UserMockConfig.class
)
@AutoConfigureMockMvc
public class TourControllerTest {
    private static final String API_URL = "http://localhost:8080/api/v1";

    @MockitoBean
    private TourLogService tourLogService;
    @Autowired
    private MockMvc mockMvc;

    @Test
    void testGetTours_whenNotLoggedIn() throws Exception {
        mockMvc.perform(
                        MockMvcRequestBuilders
                                .get(API_URL + "/logs/xyz")
                )
                .andExpect(status().isUnauthorized());
    }
}

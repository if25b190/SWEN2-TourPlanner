package at.fhtw.tourplanner.controller;

import at.fhtw.tourplanner.TourplannerApplicationTests;
import at.fhtw.tourplanner.dto.LoginDto;
import at.fhtw.tourplanner.service.UserService;
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
public class UserControllerTest {
    private static final String API_URL = "http://localhost:8080/api/v1";
    private static final LoginDto LOGIN_DTO = new LoginDto(UUID.randomUUID().toString(), "TestUser", "testtest");

    @MockitoBean
    private UserService userService;
    @Autowired
    private MockMvc mockMvc;

    @Test
    void testRegister_whenNotLoggedIn() throws Exception {
        when(userService.registerUser(ArgumentCaptor.forClass(LoginDto.class).capture())).thenReturn(Optional.of(LOGIN_DTO));
        mockMvc.perform(
                MockMvcRequestBuilders
                        .post(API_URL + "/register")
                        .content("""
                                {
                                    "username": "TestUser",
                                    "password": "testtest"
                                }
                                """)
                        .contentType(MediaType.APPLICATION_JSON)
        )
                .andExpect(status().isCreated());
        verify(userService, atLeastOnce()).registerUser(ArgumentCaptor.forClass(LoginDto.class).capture());
    }

    @Test
    void testRegisterWithInvalidBody_whenNotLoggedIn() throws Exception {
        mockMvc.perform(
                MockMvcRequestBuilders
                        .post(API_URL + "/register")
                        .content("""
                                {
                                    "username": "TestUser",
                                    "password": "test"
                                }
                                """)
                        .contentType(MediaType.APPLICATION_JSON)
        )
                .andExpect(status().isBadRequest());
        verify(userService, never()).registerUser(ArgumentCaptor.forClass(LoginDto.class).capture());
    }

    @Test
    void testGetProfile_whenNotLoggedIn() throws Exception {
        mockMvc.perform(
                MockMvcRequestBuilders
                        .get(API_URL + "/profile")
        )
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithUserDetails("User")
    void testGetProfile_whenLoggedIn() throws Exception {
        mockMvc.perform(
                MockMvcRequestBuilders
                        .get(API_URL + "/profile")
        )
                .andExpect(status().isOk())
                .andExpect(content().string("TestUser"));
    }

}

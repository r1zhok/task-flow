package org.r1zhok.app.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.keycloak.userprofile.UserProfile;
import org.mockito.Mockito;
import org.r1zhok.app.controller.payload.UserLoginPayload;
import org.r1zhok.app.controller.payload.UserRegisterPayload;
import org.r1zhok.app.controller.response.AllUsersResponse;
import org.r1zhok.app.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.restdocs.RestDocumentationExtension;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.springframework.test.web.client.match.MockRestRequestMatchers.jsonPath;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WithMockUser
@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureRestDocs
@ExtendWith(RestDocumentationExtension.class)
class UserRestControllerTest {

    @Autowired
    MockMvc mockMvc;

    @MockBean
    private UserService userService;

    /*@Test
    void testGetAllUsers() throws Exception {
        List<AllUsersResponse> users = List.of(new AllUsersResponse("user1", "ROLE_USER"),
                new AllUsersResponse("admin", "ROLE_ADMIN"));
        Mockito.when(userService.getAllUsers()).thenReturn(users);

        mockMvc.perform(get("/api/users"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].username", is("user1")))
                .andExpect(jsonPath("$[1].username", is("admin")));

        Mockito.verify(userService).getAllUsers();
    }

    @Test
    void testRegister() throws Exception {
        UserRegisterPayload payload = new UserRegisterPayload("user", "password", "user@example.com");

        mockMvc.perform(post("/api/users/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(payload)))
                .andExpect(status().isCreated())
                .andExpect(header().string("Location", "/api/users/login"));

        Mockito.verify(userService).registerUser(Mockito.eq(payload));
    }

    @Test
    void testLogin() throws Exception {
        UserLoginPayload payload = new UserLoginPayload("user", "password");
        String token = "mockToken";

        Mockito.when(userService.loginUser(Mockito.eq(payload))).thenReturn(token);

        mockMvc.perform(post("/api/users/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(payload)))
                .andExpect(status().isOk())
                .andExpect(content().string(token));

        Mockito.verify(userService).loginUser(Mockito.eq(payload));
    }

    @Test
    void testGetProfile() throws Exception {
        String username = "user";
        UserProfile profile = new UserProfile(username, "user@example.com");

        Mockito.when(userService.getProfile(username)).thenReturn(profile);

        mockMvc.perform(get("/api/users/profile").principal(() -> username))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username", is(username)));

        Mockito.verify(userService).getProfile(username);
    }

    @Test
    void testUpdateProfile() throws Exception {
        String username = "user";
        UserRegisterPayload payload = new UserRegisterPayload("user", "newPassword", "new@example.com");

        mockMvc.perform(put("/api/users/profile")
                        .contentType(MediaType.APPLICATION_JSON)
                        .principal(() -> username)
                        .content(new ObjectMapper().writeValueAsString(payload)))
                .andExpect(status().isNoContent());

        Mockito.verify(userService).updateProfile(Mockito.eq(payload), Mockito.eq(username));
    }

    @Test
    void testAssignRole() throws Exception {
        String userId = "123";
        List<String> roles = List.of("ROLE_ADMIN", "ROLE_USER");

        mockMvc.perform(put("/api/users/assign-role/" + userId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(roles)))
                .andExpect(status().isNoContent());

        Mockito.verify(userService).setRole(userId, roles);
    }

    @Test
    void testDeleteProfile() throws Exception {
        String username = "user";

        mockMvc.perform(delete("/api/users/profile").principal(() -> username))
                .andExpect(status().isNoContent());

        Mockito.verify(userService).deleteUser(username);
    }

    @Test
    void testAdminDeleteUserProfile() throws Exception {
        String userId = "123";

        mockMvc.perform(delete("/api/users/profile/" + userId))
                .andExpect(status().isNoContent());

        Mockito.verify(userService).deleteUser(userId);
    }*/
}
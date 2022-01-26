package ch.bbcag.controllers;

import ch.bbcag.models.ApplicationUser;
import ch.bbcag.security.UserDetailsServiceImpl;
import ch.bbcag.services.ApplicationUserService;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;

import java.util.NoSuchElementException;

import static ch.bbcag.utils.TestDataUtil.getTestUser;
import static ch.bbcag.utils.TestDataUtil.getTestUsers;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.doThrow;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = UserController.class)
@AutoConfigureMockMvc(addFilters = false)
public class UserControllerTest {

    private static final String JSON_ALL_USERS = """
            [
              {
                "id": 1,
                "username": "User1",
                "items": []
              },
              {
                "id": 2,
                "username": "User2",
                "items": []
              },
              {
                "id": 3,
                "username": "User3",
                "items": []
              },
              {
                "id": 4,
                "username": "User4",
                "items": []
              }
            ]
            """;

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ApplicationUserService applicationUserService;

    @MockBean
    private UserDetailsServiceImpl userDetailsService;

    @MockBean
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    private ArgumentCaptor<ApplicationUser> argumentCaptor = ArgumentCaptor.forClass(ApplicationUser.class);

    @Test
    public void checkGet_whenNoParam_thenAllUsersAreReturned() throws Exception {
        doReturn(getTestUsers()).when(applicationUserService).findAll();

        mockMvc.perform(get("/users")
                        .contentType("application/json"))
                .andExpect(status().isOk())
                .andExpect(content().json(JSON_ALL_USERS));
    }

    @Test
    public void checkGetById_whenValidId_thenUserIsReturned() throws Exception {
        ApplicationUser expected = getTestUser();
        doReturn(expected).when(applicationUserService).findById(1);

        mockMvc.perform(get("/users/" + 1)
                        .contentType("application/json"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username", is("User1")));
    }

    @Test
    public void checkGetById_whenInvalidId_thenIsNotFound() throws Exception {
        doThrow(NoSuchElementException.class).when(applicationUserService).findById(0);

        mockMvc.perform(get("/users/" + 0)
                        .contentType("application/json"))
                .andExpect(status().isNotFound());
    }

    @Test
    public void checkPost_whenNewUser_thenIsOk() throws Exception {
        mockMvc.perform(post("/users/sign-up")
                        .contentType("application/json")
                        .content("{\"username\":\"NewUser\", \"password\":\"newPassword\"}"))
                .andExpect(status().isCreated());


        Mockito.verify(applicationUserService).signUp(argumentCaptor.capture());

        ApplicationUser verify = argumentCaptor.getValue();
        Assertions.assertThat(verify.getUsername()).isEqualTo("NewUser");
        Assertions.assertThat(verify.getPassword()).isEqualTo("newPassword");
    }

    @Test
    public void checkPut_whenValidUser_thenIsOk() throws Exception {
        mockMvc.perform(put("/users")
                        .contentType("application/json")
                        .content("{\"username\":\"NewUser\", \"password\":\"newPassword\"}"))
                .andExpect(status().isOk());
        Mockito.verify(applicationUserService).update(argumentCaptor.capture());

        ApplicationUser verify = argumentCaptor.getValue();
        Assertions.assertThat(verify.getUsername()).isEqualTo("NewUser");
        Assertions.assertThat(verify.getPassword()).isEqualTo("newPassword");
    }


    @Test
    public void checkDelete_whenValidId_thenIsOk() throws Exception {
        mockMvc.perform(delete("/users/1")
                        .contentType("application/json"))
                .andExpect(status().isOk());

        Mockito.verify(applicationUserService).deleteById(eq(1));
    }

}

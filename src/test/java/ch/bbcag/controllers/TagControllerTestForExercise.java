package ch.bbcag.controllers;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import ch.bbcag.repositories.TagRepository;
import ch.bbcag.security.UserDetailsServiceImpl;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(controllers = TagController.class)
@AutoConfigureMockMvc(addFilters = false)
public class TagControllerTestForExercise {

    private static final String TEST_REQUEST_INPUT_DATA = """
            {
              "name": "Tag"
            }
            """;

    @Autowired
    private MockMvc fakeController;

    @MockBean
    private TagRepository tagRepository;

    @MockBean
    private UserDetailsServiceImpl userDetailsService;

    @MockBean
    private BCryptPasswordEncoder bCryptPasswordEncoder;


    @Test
    public void checkCreateNewTags_whenValidItem_thenIsOk() throws Exception {
        fakeController.perform(post("/tags")
                .contentType("application/json")
                .content(TEST_REQUEST_INPUT_DATA))
            .andExpect(status().isCreated());
    }

    @Test
    public void checkCreateNewTas_whenInvalidItem_thenIsBadRequest() throws Exception {
        fakeController.perform(post("/tags")
                .contentType("application/json")
                .content("{\"Invalid Name\":\"Invalid Data\"}"))
            .andExpect(status().isBadRequest());
    }

}

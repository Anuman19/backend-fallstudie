package ch.bbcag.controllers;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import ch.bbcag.repositories.ItemRepository;
import ch.bbcag.security.UserDetailsServiceImpl;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(controllers = ItemController.class)
@AutoConfigureMockMvc(addFilters = false)
public class ItemControllerTestForExercise {

    private static final String TEST_REQUEST_INPUT_DATA = """
            {
                 "id": 4,
                 "name": "Item4",
                 "description": "Description4",
                 "createdAt": "2020-01-01T00:00:00.000+00:00",
                 "doneAt": "2020-01-01T05:00:00.000+00:00",
                 "deletedAt": "2020-01-01T10:00:00.000+00:00",
                 "linkedTags": [
                   {
                     "id": 1,
                     "name": "Tag"
                   }
                 ]
               }
            """;

    @Autowired
    private MockMvc fakeController;

    @MockBean
    private ItemRepository itemRepository;                  // Wird vom SpringBoot Testing Framework benötigt

    @MockBean
    private UserDetailsServiceImpl userDetailsService;      // Wird vom SpringBoot Testing Framework benötigt

    @MockBean
    private BCryptPasswordEncoder bCryptPasswordEncoder;    // Wird vom SpringBoot Testing Framework benötigt


    @Test
    public void checkCreateNewItem_whenValidItem_thenIsOk() throws Exception {
        fakeController.perform(post("/items")
                        .contentType("application/json")
                        .content(TEST_REQUEST_INPUT_DATA))
                .andExpect(status().isCreated());
    }

    @Test
    public void checkCreateNewItem_whenInvalidItem_thenIsBadRequest() throws Exception {
        fakeController.perform(post("/items")
                        .contentType("application/json")
                        .content("{\"Invalid Name\":\"Invalid Data\"}"))
                .andExpect(status().isBadRequest());
    }

}

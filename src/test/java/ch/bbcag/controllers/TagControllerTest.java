package ch.bbcag.controllers;

import static ch.bbcag.utils.TestDataUtil.getTestTag;
import static ch.bbcag.utils.TestDataUtil.getTestTags;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doReturn;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import ch.bbcag.models.Tag;
import ch.bbcag.repositories.TagRepository;
import ch.bbcag.security.UserDetailsServiceImpl;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(controllers = TagController.class)
@AutoConfigureMockMvc(addFilters = false)
public class TagControllerTest {

    private static final String JSON_ALL_TAGS = """
            [
              {
                "id": 1,
                "name": "Tag1"
              },
              {
                "id": 2,
                "name": "Tag2"
              },
              {
                "id": 3,
                "name": "Tag3"
              },
              {
                "id": 4,
                "name": "Tag4"
              }
            ]
            """;

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private TagRepository tagRepository;

    @MockBean
    private UserDetailsServiceImpl userDetailsService;

    @MockBean
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Test
    public void checkGet_whenNoParam_thenAllTagsAreReturned() throws Exception {
        doReturn(getTestTags()).when(tagRepository).findAll();

        mockMvc.perform(get("/tags")
                        .contentType("application/json"))
                .andExpect(status().isOk())
                .andExpect(content().json(JSON_ALL_TAGS));
    }

    @Test
    public void checkGet_whenValidName_thenTagIsReturned() throws Exception {
        String tagName = "Tag4";
        doReturn(getTestTags().subList(3, 4)).when(tagRepository).findByName(tagName);

        mockMvc.perform(get("/tags")
                        .contentType("application/json")
                        .queryParam("name", tagName))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value(tagName));
    }

    @Test
    public void checkGet_whenNotExistingName_thenAllTagsAreReturned() throws Exception {
        String tagName = "NotExistingTag";

        mockMvc.perform(get("/tags")
                        .contentType("application/json")
                        .queryParam("name", tagName))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(0)));
    }

    @Test
    public void checkGetById_whenValidId_thenTagIsReturned() throws Exception {
        Tag expected = getTestTag();
        doReturn(Optional.of(expected)).when(tagRepository).findById(1);

        mockMvc.perform(get("/tags/" + 1)
                        .contentType("application/json"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is("Tag1")));
    }

    @Test
    public void checkGetById_whenInvalidId_thenIsNotFound() throws Exception {
        doReturn(Optional.empty()).when(tagRepository).findById(0);
        mockMvc.perform(get("/tags/" + 0)
                        .contentType("application/json"))
                .andExpect(status().isNotFound());
    }

    @Test
    public void checkPost_whenNewTag_thenIsOk() throws Exception {
        mockMvc.perform(post("/tags")
                        .contentType("application/json")
                        .content("{\"name\":\"NewTag\"}"))
                .andExpect(status().isCreated());
    }

    @Test
    public void checkPut_whenValidTag_thenIsOk() throws Exception {
        mockMvc.perform(put("/tags")
                        .contentType("application/json")
                        .content("{\"name\":\"NewTag\"}"))
                .andExpect(status().isOk());
    }

    @Test
    public void checkDelete_whenValidId_thenIsOk() throws Exception {
        mockMvc.perform(delete("/tags/1")
                        .contentType("application/json"))
                .andExpect(status().isOk());

        Mockito.verify(tagRepository).deleteById(eq(1));
    }


}

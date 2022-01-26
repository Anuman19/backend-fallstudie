package ch.bbcag.controllers;

import static ch.bbcag.utils.TestDataUtil.getTestItem;
import static ch.bbcag.utils.TestDataUtil.getTestItems;
import static ch.bbcag.utils.TestDataUtil;
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

import ch.bbcag.models.Item;
import ch.bbcag.repositories.ItemRepository;
import ch.bbcag.security.UserDetailsServiceImpl;
import java.util.Optional;
import java.util.TimeZone;
import org.hibernate.exception.ConstraintViolationException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(controllers = ItemController.class)
@AutoConfigureMockMvc(addFilters = false)
public class ItemControllerTest {

    private static final String JSON_ALL_ITEMS = """
            [
              {
                "id": 1,
                "name": "Item1",
                "description": "Description1",
                "createdAt": "2020-01-01T00:00:00.000+00:00",
                "doneAt": "2020-01-01T05:00:00.000+00:00",
                "deletedAt": "2020-01-01T10:00:00.000+00:00",
                "linkedTags": [
                  {
                    "id": 1,
                    "name": "Tag"
                  }
                ]
              },
              {
                "id": 2,
                "name": "Item2",
                "description": "Description2",
                "createdAt": "2020-01-01T00:00:00.000+00:00",
                "doneAt": "2020-01-01T05:00:00.000+00:00",
                "deletedAt": "2020-01-01T10:00:00.000+00:00",
                "linkedTags": [
                  {
                    "id": 1,
                    "name": "Tag"
                  }
                ]
              },
              {
                "id": 3,
                "name": "Item3",
                "description": "Description3",
                "createdAt": "2020-01-01T00:00:00.000+00:00",
                "doneAt": "2020-01-01T05:00:00.000+00:00",
                "deletedAt": "2020-01-01T10:00:00.000+00:00",
                "linkedTags": [
                  {
                    "id": 1,
                    "name": "Tag"
                  }
                ]
              },
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
            ]
            """;

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ItemRepository itemRepository;

    @MockBean
    private UserDetailsServiceImpl userDetailsService;

    @MockBean
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @BeforeEach
    public void prepare() {
        TimeZone.setDefault(TimeZone.getTimeZone("UTC"));
    }

    @Test
    public void checkGet_whenNoParam_thenAllItemsAreReturned() throws Exception {

        doReturn(getTestItems()).when(itemRepository).findAll();

        mockMvc.perform(get("/items")
                        .contentType("application/json"))
                .andExpect(status().isOk())
                .andExpect(content().json(JSON_ALL_ITEMS));
    }

    @Test
    public void checkGet_whenValidName_thenItemIsReturned() throws Exception {
        String itemName = "Item4";
        doReturn(getTestItems().subList(3, 4)).when(itemRepository).findByName(itemName);

        mockMvc.perform(get("/items")
                        .contentType("application/json")
                        .queryParam("name", itemName))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value(itemName));
    }

    @Test
    public void checkGetById_whenValidId_thenItemIsReturned() throws Exception {
        Item expected = getTestItem();
        doReturn(Optional.of(expected)).when(itemRepository).findById(1);

        mockMvc.perform(get("/items/" + 1)
                        .contentType("application/json"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is("Item1")));
    }

    @Test
    public void checkGetById_whenInvalidId_thenIsNotFound() throws Exception {
        doReturn(Optional.empty()).when(itemRepository).findById(0);
        mockMvc.perform(get("/items/" + 0)
                        .contentType("application/json"))
                .andExpect(status().isNotFound());
    }

    @Test
    public void checkPost_whenNewItem_thenIsOk() throws Exception {
        mockMvc.perform(post("/items")
                        .contentType("application/json")
                        .content("{\"name\":\"NewItem\", \"userId\":\"1\"}"))
                .andExpect(status().isCreated());
    }

    @Test
    public void checkPost_whenInvalidItem_thenIsConflict() throws Exception {
        doThrow(ConstraintViolationException.class).when(itemRepository).save(new Item());

        mockMvc.perform(post("/items")
                        .contentType("application/json")
                        .content("{\"wrongFieldName\":\"Item1\"}"))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void checkPut_whenValidItem_thenIsOk() throws Exception {
        mockMvc.perform(put("/items")
                        .contentType("application/json")
                        .content("{\"name\":\"NewItem\", \"userId\":\"1\"}"))
                .andExpect(status().isOk());
    }

    @Test
    public void checkPut_whenInvalidItem_thenIsConflict() throws Exception {
        doThrow(ConstraintViolationException.class).when(itemRepository).save(new Item());

        mockMvc.perform(put("/items")
                        .contentType("application/json")
                        .content("{\"wrongFieldName\":\"Item1\"}"))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void checkDelete_whenValidId_thenIsOk() throws Exception {
        mockMvc.perform(delete("/items/1")
                        .contentType("application/json"))
                .andExpect(status().isOk());

        Mockito.verify(itemRepository).deleteById(eq(1));
    }

    @Test
    public void checkDelete_whenInvalidId_thenIsNotFound() throws Exception {
        doThrow(EmptyResultDataAccessException.class).when(itemRepository).deleteById(99999);
        mockMvc.perform(delete("/items/99999")
                        .contentType("application/json"))
                .andExpect(status().isNotFound());

        Mockito.verify(itemRepository).deleteById(eq(99999));
    }


}

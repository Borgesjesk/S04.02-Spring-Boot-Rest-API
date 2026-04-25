package cat.itacademy.s04.t02.n01.fruit_api_h2;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class FruitControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    @DisplayName("Should return 201 Created and the persisted fruit when a valid POST request is made")
    void createFruit_withValidData_returns201WithFruit() throws Exception {

        String fruitJson = "{\"name\": \"Apple\", \"weightInKilos\": 3}";

        mockMvc.perform(post("/fruits")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(fruitJson))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.name").value("Apple"))
                .andExpect(jsonPath("$.weightInKilos").value(3));
    }

    @Test
    @DisplayName("Should return 400 when name is blank")
    void createFruit_withInvalidName_returns400() throws Exception {

        String fruitJson = "{\"name\": \"\", \"weightInKilos\": 10}";

        mockMvc.perform(post("/fruits")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(fruitJson))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("Should return 400 when weight is zero")
    void createFruit_withInvalidWeight_returns400() throws Exception {

        String fruitJson = "{\"name\": \"Apple\", \"weightInKilos\": 0}";

        mockMvc.perform(post("/fruits")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(fruitJson))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("Should return 400 when weight is a negative")
    void createFruit_withNegativeWeight_returns400() throws Exception {
        String fruitJson = "{\"name\": \"Apple\", \"weightInKilos\": -1}";

        mockMvc.perform(post("/fruits")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(fruitJson))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("Should return a list of fruits and 200 OK")
    void getAllFruits_withValidData_returnsListOfFruits() throws Exception {

        String fruit1 = "{\"name\": \"Banana\", \"weightInKilos\": 3}";
        String fruit2 = "{\"name\": \"Orange\", \"weightInKilos\": 4}";

        mockMvc.perform(post("/fruits")
                .contentType(MediaType.APPLICATION_JSON)
                .content(fruit1));

        mockMvc.perform(post("/fruits")
                .contentType(MediaType.APPLICATION_JSON)
                .content(fruit2));

        mockMvc.perform(get("/fruits")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].name").value("Banana"))
                .andExpect(jsonPath("$[1].name").value("Orange"));
    }

    @Test
    @DisplayName("Should return an empty list and 200 OK when no Fruits exists")
    void getAllFruits_whenEmpty_returnsEmptyListAnd200() throws Exception {

        mockMvc.perform(get("/fruits")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(0));
    }

    @Test
    @DisplayName("Should return 200 and the fruit when ID exists")
    void getFruitById_whenValidId_returns200AndFruit() throws Exception {
        String fruitJson = "{\"name\": \"Mango\", \"weightInKilos\": 2}";

        String response = mockMvc.perform(post("/fruits")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(fruitJson))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString();

        Long id = ((Number) com.jayway.jsonpath.JsonPath.read(response, "$.id")).longValue();

        mockMvc.perform(get("/fruits/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Mango"))
                .andExpect(jsonPath("$.weightInKilos").value(2));
    }

    @Test
    @DisplayName("Should return 404 when fruit ID does not exist")
    void getFruitById_whenInvalidId_returns404() throws Exception {

        mockMvc.perform(get("/fruits/{id}", 999L)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("Should return 200 and updated fruit when ID exists and data is valid")
    void updateFruit_withValidData_returns200AndUpdatedFruit() throws Exception {
        String originalJson = "{\"name\": \"Melon\", \"weightInKilos\": 5}";
        String postResponse = mockMvc.perform(post("/fruits")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(originalJson))
                .andReturn()
                .getResponse()
                .getContentAsString();

        Long id = ((Number) com.jayway.jsonpath.JsonPath.read(postResponse, "$.id")).longValue();

        String updatedJson = "{\"name\": \"Green Melon\", \"weightInKilos\": 6}";

        mockMvc.perform(put("/fruits/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(updatedJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Green Melon"))
                .andExpect(jsonPath("$.weightInKilos").value(6));
    }

    @Test
    @DisplayName("Should return 404 when updating a non-existing fruit ID")
    void updateFruit_whenInvalidId_returns404() throws Exception {
        String updatedJson = "{\"name\": \"Kiwi\", \"weightInKilos\": 1}";


        mockMvc.perform(put("/fruits/{id}", 999L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(updatedJson))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("Should return 400 when updating with invalid data (blank name)")
    void updateFruit_withInvalidName_returns400() throws Exception {
        String originalJson = "{\"name\": \"Watermelon\", \"weightInKilos\": 1}";
        String postResponse = mockMvc.perform(post("/fruits")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(originalJson))
                .andReturn()
                .getResponse()
                .getContentAsString();

        Long id = ((Number) com.jayway.jsonpath.JsonPath.read(postResponse, "$.id")).longValue();

        String badJson = "{\"name\": \"\", \"weightInKilos\": 2}";

        mockMvc.perform(put("/fruits/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(badJson))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("Should return 204 when fruits is successfully deleted")
    void deleteFruit_withValidId_returns204() throws Exception {

        String fruitJson = "{\"name\": \"Strawberry\", \"weightInKilos\": 3}";
        String response = mockMvc.perform(post("/fruits")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(fruitJson))
                .andReturn()
                .getResponse()
                .getContentAsString();

        Long id = ((Number) com.jayway.jsonpath.JsonPath.read(response, "$.id")).longValue();

        mockMvc.perform(delete("/fruits/{id}", id))
                .andExpect(status().isNoContent());

        mockMvc.perform(get("/fruits/{id}", id))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("Should return 404 when deleting a non-existing fruit")
    void deleteFruit_withInvalidId_returns404() throws Exception {
        mockMvc.perform(delete("/fruits/{id}", 999L))
                .andExpect(status().isNotFound());
    }
}
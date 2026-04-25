package cat.itacademy.s04.t02.n01.fruit_api_h2;


import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
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
}
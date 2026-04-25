package cat.itacademy.s04.t02.n01.fruit_api_h2;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@SpringBootTest
@AutoConfigureMockMvc
class FruitControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    @DisplayName("Should return 201 Created and the persisted fruit when a valid POST request is made")
    public void createFruit_withValidData_returns201WithFruit() throws Exception {

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
    public void createFruit_withInvalidName_returns400() throws Exception {

        String fruitJson = "{\"name\": \"\", \"weightInKilos\": 10}";

        mockMvc.perform(post("/fruits")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(fruitJson))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("Should return 400 when weight is zero")
    public void createFruit_withInvalidWeight_returns400() throws Exception {

        String fruitJson = "{\"name\": \"Apple\", \"weightInKilos\": 0}";

        mockMvc.perform(post("/fruits")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(fruitJson))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("Should return 400 when weight is a negative")
    public void createFruit_withNegativeWeight_returns400() throws Exception {
        String fruitJson = "{\"name\": \"Apple\", \"weightInKilos\": -1}";

        mockMvc.perform(post("/fruits")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(fruitJson))
                .andExpect(status().isBadRequest());
    }
}
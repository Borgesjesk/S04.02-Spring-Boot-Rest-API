package cat.itacademy.s04.t02.n01.fruit_api_h2.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class FruitRequestDto {

    @NotBlank(message = "Fruit name cannot be empty")
    private String name;

    @NotNull(message = "Weight is required")
    @Positive(message = "Weight must be greater than zero")
    private Integer weightInKilos;
}
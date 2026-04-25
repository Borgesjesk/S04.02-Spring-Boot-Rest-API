package cat.itacademy.s04.t02.n01.fruit_api_h2.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class FruitResponseDto {
    private Long id;
    private String name;
    private Integer weightInKilos;
}
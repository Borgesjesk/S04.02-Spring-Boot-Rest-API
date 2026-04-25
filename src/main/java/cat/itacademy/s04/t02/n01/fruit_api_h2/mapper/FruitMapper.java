package cat.itacademy.s04.t02.n01.fruit_api_h2.mapper;

import cat.itacademy.s04.t02.n01.fruit_api_h2.dto.FruitRequestDto;
import cat.itacademy.s04.t02.n01.fruit_api_h2.dto.FruitResponseDto;
import cat.itacademy.s04.t02.n01.fruit_api_h2.model.Fruit;

public class FruitMapper {

    private FruitMapper() {
        throw new UnsupportedOperationException("Utility class should not be instantiated.");
    }

    public static Fruit toEntity(FruitRequestDto dto) {
        if (dto == null) {
            return null;
        }
        return new Fruit(dto.getName(), dto.getWeightInKilos());
    }

    public static FruitResponseDto toResponseDto(Fruit fruit) {
        if (fruit == null) {
            return null;
        }
        return new FruitResponseDto(fruit.getId(), fruit.getName(), fruit.getWeightInKilos());
    }
}
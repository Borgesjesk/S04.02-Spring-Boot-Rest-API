package cat.itacademy.s04.t02.n01.fruit_api_h2.service;

import cat.itacademy.s04.t02.n01.fruit_api_h2.dto.FruitRequestDto;
import cat.itacademy.s04.t02.n01.fruit_api_h2.dto.FruitResponseDto;

import java.util.List;

public interface FruitService {

    FruitResponseDto createFruit(FruitRequestDto dto);

    List<FruitResponseDto> findAllFruits();
}
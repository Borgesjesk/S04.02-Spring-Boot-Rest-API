package cat.itacademy.s04.t02.n01.fruit_api_h2.service;

import cat.itacademy.s04.t02.n01.fruit_api_h2.dto.FruitRequestDto;
import cat.itacademy.s04.t02.n01.fruit_api_h2.dto.FruitResponseDto;
import cat.itacademy.s04.t02.n01.fruit_api_h2.mapper.FruitMapper;
import cat.itacademy.s04.t02.n01.fruit_api_h2.model.Fruit;
import cat.itacademy.s04.t02.n01.fruit_api_h2.repository.FruitRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class FruitServiceImpl implements FruitService {

    private final FruitRepository fruitRepository;

    @Override
    public FruitResponseDto createFruit(FruitRequestDto fruitRequestDto) {
        Fruit fruit = FruitMapper.toEntity(fruitRequestDto);

        Fruit savedFruit = fruitRepository.save(fruit);

        return FruitMapper.toResponseDto(savedFruit);
    }

    @Override
    public List<FruitResponseDto> findAllFruits() {
        return fruitRepository.findAll()
                .stream()
                .map(FruitMapper::toResponseDto)
                .toList();
    }
}
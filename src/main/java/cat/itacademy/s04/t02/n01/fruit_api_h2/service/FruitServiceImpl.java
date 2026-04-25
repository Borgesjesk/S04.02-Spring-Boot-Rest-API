package cat.itacademy.s04.t02.n01.fruit_api_h2.service;

import cat.itacademy.s04.t02.n01.fruit_api_h2.dto.FruitRequestDto;
import cat.itacademy.s04.t02.n01.fruit_api_h2.dto.FruitResponseDto;
import cat.itacademy.s04.t02.n01.fruit_api_h2.exception.FruitNotFoundException;
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

    @Override
    public FruitResponseDto getFruitById(Long id) {
        return fruitRepository.findById(id)
                .map(FruitMapper::toResponseDto)
                .orElseThrow(() -> new FruitNotFoundException("Fruit with ID: %d not found".formatted(id)));
    }

    @Override
    public FruitResponseDto updateFruit(Long id, FruitRequestDto dto) {
        Fruit existingFruit = fruitRepository.findById(id)
                .orElseThrow(() -> new FruitNotFoundException("Fruit with ID: %d not found".formatted(id)));

        existingFruit.setName(dto.getName());
        existingFruit.setWeightInKilos(dto.getWeightInKilos());

        Fruit updatedFruit = fruitRepository.save(existingFruit);

        return FruitMapper.toResponseDto(updatedFruit);
    }

    @Override
    public void deleteFruit(Long id) {
        if (!fruitRepository.existsById(id)) {
            throw new FruitNotFoundException("Fruit with ID: %d not found".formatted(id));
        }
        fruitRepository.deleteById(id);
    }
}
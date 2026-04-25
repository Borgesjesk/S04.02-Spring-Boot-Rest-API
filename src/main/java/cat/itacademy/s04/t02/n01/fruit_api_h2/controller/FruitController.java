package cat.itacademy.s04.t02.n01.fruit_api_h2.controller;

import cat.itacademy.s04.t02.n01.fruit_api_h2.dto.FruitRequestDto;
import cat.itacademy.s04.t02.n01.fruit_api_h2.dto.FruitResponseDto;
import cat.itacademy.s04.t02.n01.fruit_api_h2.service.FruitService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/fruits")
@RequiredArgsConstructor
public class FruitController {

    private final FruitService fruitService;

    @PostMapping
    public ResponseEntity<FruitResponseDto> createFruit(@Valid @RequestBody FruitRequestDto dto) {
        FruitResponseDto response = fruitService.createFruit(dto);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<FruitResponseDto>> findAllFruits() {
        List<FruitResponseDto> fruits = fruitService.findAllFruits();
        return ResponseEntity.ok(fruits);
    }
}
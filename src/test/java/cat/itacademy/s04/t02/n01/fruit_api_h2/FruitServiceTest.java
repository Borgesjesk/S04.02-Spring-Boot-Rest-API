package cat.itacademy.s04.t02.n01.fruit_api_h2;

import cat.itacademy.s04.t02.n01.fruit_api_h2.dto.FruitRequestDto;
import cat.itacademy.s04.t02.n01.fruit_api_h2.dto.FruitResponseDto;
import cat.itacademy.s04.t02.n01.fruit_api_h2.exception.FruitNotFoundException;
import cat.itacademy.s04.t02.n01.fruit_api_h2.model.Fruit;
import cat.itacademy.s04.t02.n01.fruit_api_h2.repository.FruitRepository;
import cat.itacademy.s04.t02.n01.fruit_api_h2.service.FruitServiceImpl;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class FruitServiceTest {

    @Mock
    private FruitRepository fruitRepository;

    @InjectMocks
    private FruitServiceImpl fruitService;

    @Test
    @DisplayName("Should successfully create a fruit and return the saved entity")
    void createFruit_withValidDto_returnsSavedEntity() {
        FruitRequestDto requestDto = new FruitRequestDto("Tangerine", 2);

        Fruit savedFruit = new Fruit("Tangerine", 2);
        savedFruit.setId(1L);

        when(fruitRepository.save(any(Fruit.class))).thenReturn(savedFruit);

        FruitResponseDto response = fruitService.createFruit(requestDto);

        assertNotNull(response);
        assertEquals(1L, response.getId());
        assertEquals("Tangerine", response.getName());
        assertEquals(2, response.getWeightInKilos());

        verify(fruitRepository).save(any(Fruit.class));
    }

    @Test
    @DisplayName("Should return FruitResponseDto when valid ID is provided")
    void getFruitById_whenValidId_returnsFruit() {
        Fruit savedFruit = new Fruit("Grape", 2);
        Long id = 1L;
        savedFruit.setId(id);

        when(fruitRepository.findById(id)).thenReturn(Optional.of(savedFruit));

        FruitResponseDto response = fruitService.getFruitById(id);

        assertNotNull(response);
        assertEquals(1L, response.getId());
        assertEquals("Grape", response.getName());
        assertEquals(2, response.getWeightInKilos());
        verify(fruitRepository).findById(id);
    }

    @Test
    @DisplayName("Should throw FruitNotFoundException when ID does not exist")
    void getFruitById_whenIdDoesNotExist_throwsFruitNotFoundException() {
        Long id = 999L;

        when(fruitRepository.findById(id)).thenReturn(Optional.empty());

        assertThrows(FruitNotFoundException.class, () -> fruitService.getFruitById(id));
    }

    @Test
    @DisplayName("Should throw Exception when attempting to delete non-existing ID")
    void deleteFruitById_whenIdDoesNotExist_throwsFruitNotFoundException() {
        Long id = 55L;
        when(fruitRepository.existsById(id)).thenReturn(false);

        assertThrows(FruitNotFoundException.class, () -> fruitService.deleteFruit(id));

        verify(fruitRepository).existsById(id);
        verify(fruitRepository, never()).deleteById(anyLong());
    }
}
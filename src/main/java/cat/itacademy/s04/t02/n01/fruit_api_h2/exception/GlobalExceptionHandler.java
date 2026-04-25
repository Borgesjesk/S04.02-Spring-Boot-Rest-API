package cat.itacademy.s04.t02.n01.fruit_api_h2.exception;

import cat.itacademy.s04.t02.n01.fruit_api_h2.dto.FruitResponseDto;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(FruitNotFoundException.class)
    public ResponseEntity<Map<String, Object>> handleFruitNotFoundException(FruitNotFoundException ex) {
        Map<String, Object> response = new HashMap<>();
        response.put("message", ex.getMessage());
        response.put("status", 404);
        response.put("error", "Not Found"); //or ("error", "Not Found"
        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }
}

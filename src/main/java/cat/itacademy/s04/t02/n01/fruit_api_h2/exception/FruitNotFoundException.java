package cat.itacademy.s04.t02.n01.fruit_api_h2.exception;

public class FruitNotFoundException extends RuntimeException {
    public FruitNotFoundException(String message) {
        super(message);
    }
}
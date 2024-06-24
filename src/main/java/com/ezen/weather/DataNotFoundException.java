package com.ezen.weather;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND, reason = "entity not found")
public class DataNotFoundException extends RuntimeException{
    // RuntimeException: 실행시 발생하는 예외
    private static final long serialVersionUID = 1L;
    public DataNotFoundException(String message) {
        super(message);
    }
}

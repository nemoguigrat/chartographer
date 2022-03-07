package ru.ivanov.intern.chartographer.exeption;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND)
public class ChartNotFoundException extends Exception {
    public ChartNotFoundException(String message) {
        super(message);
    }
}

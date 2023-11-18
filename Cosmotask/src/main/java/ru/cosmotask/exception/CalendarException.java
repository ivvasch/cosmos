package ru.cosmotask.exception;

public class CalendarException extends RuntimeException {
    public CalendarException() {
        super();
    }

    public CalendarException(String message) {
        super(message);
    }
}

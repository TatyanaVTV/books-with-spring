package ru.vtvhw.exceptions;

public class EntityNotFoundException extends RuntimeException {
    public EntityNotFoundException(Class clazz, long entityId) {
        super(String.format("%s with ID = %d not found!", clazz.getSimpleName(), entityId));
    }
}
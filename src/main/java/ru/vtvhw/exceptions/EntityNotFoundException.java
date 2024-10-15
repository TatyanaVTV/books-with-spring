package ru.vtvhw.exceptions;

public class EntityNotFoundException extends RuntimeException {
    private Class clazz;

    public EntityNotFoundException(Class clazz, long entityId) {
        super(String.format("Entity not found! Id: %d (%s)", entityId, clazz.getSimpleName()));
    }
}
package com.uab.taller.store.exception;

public class EntityDeletionException extends RuntimeException {
    public EntityDeletionException(String message) {
        super(message);
    }
    
    public EntityDeletionException(String entityType, Long id, String reason) {
        super(String.format("Cannot delete %s with ID %d: %s", entityType, id, reason));
    }
}

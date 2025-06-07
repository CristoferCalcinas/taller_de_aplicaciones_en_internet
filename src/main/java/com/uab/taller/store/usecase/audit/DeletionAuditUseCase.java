package com.uab.taller.store.usecase.audit;

import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.logging.Logger;

@Service
public class DeletionAuditUseCase {
    
    private static final Logger logger = Logger.getLogger(DeletionAuditUseCase.class.getName());
    
    public void logDeletion(String entityType, Long entityId, String operationType, String userId) {
        String logMessage = String.format(
            "DELETION_AUDIT: [%s] %s ID:%d by User:%s at %s",
            operationType,
            entityType,
            entityId,
            userId != null ? userId : "SYSTEM",
            LocalDateTime.now()
        );
        
        logger.info(logMessage);
        
        // En una implementación real, aquí podrías:
        // 1. Guardar en una tabla de auditoría en la base de datos
        // 2. Enviar a un sistema de logging externo
        // 3. Notificar a administradores sobre eliminaciones críticas
    }
    
    public void logDeletionAttempt(String entityType, Long entityId, String reason, String userId) {
        String logMessage = String.format(
            "DELETION_ATTEMPT_FAILED: %s ID:%d - Reason: %s - User:%s at %s",
            entityType,
            entityId,
            reason,
            userId != null ? userId : "SYSTEM",
            LocalDateTime.now()
        );
        
        logger.warning(logMessage);
    }
    
    public void logForceDelete(String entityType, Long entityId, String userId) {
        String logMessage = String.format(
            "FORCE_DELETE: %s ID:%d by User:%s at %s - WARNING: Physical deletion performed",
            entityType,
            entityId,
            userId != null ? userId : "SYSTEM",
            LocalDateTime.now()
        );
        
        logger.severe(logMessage);
    }
}

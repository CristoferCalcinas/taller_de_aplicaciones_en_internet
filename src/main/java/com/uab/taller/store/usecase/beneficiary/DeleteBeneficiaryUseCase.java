package com.uab.taller.store.usecase.beneficiary;

import com.uab.taller.store.domain.Beneficiary;
import com.uab.taller.store.domain.dto.response.DeleteResponse;
import com.uab.taller.store.exception.EntityDeletionException;
import com.uab.taller.store.exception.EntityNotFoundException;
import com.uab.taller.store.service.interfaces.IBeneficiaryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class DeleteBeneficiaryUseCase {
    @Autowired
    IBeneficiaryService service;

    @Transactional
    public DeleteResponse deleteById(Long id) {
        // Validar que el ID no sea nulo
        if (id == null) {
            throw new IllegalArgumentException("El ID del beneficiario no puede ser nulo");
        }

        // Verificar que el beneficiario existe
        Beneficiary beneficiary = service.findById(id);
        if (beneficiary == null) {
            throw new EntityNotFoundException("Beneficiario", id);
        }

        // Validar que el beneficiario no esté siendo usado en transacciones activas
        // En una implementación real, aquí verificarías si hay transacciones pendientes
        // que involucren a este beneficiario

        try {
            // Realizar soft delete
            beneficiary.setDeleted(true);
            beneficiary.setChangeUser("SYSTEM_DELETE");
            service.update(beneficiary);

            return DeleteResponse.success(id, "Beneficiario");
        } catch (Exception e) {
            throw new EntityDeletionException("Beneficiario", id,
                    "Error interno durante la eliminación: " + e.getMessage());
        }
    }

    // Método para eliminación física
    @Transactional
    public DeleteResponse forceDeleteById(Long id) {
        if (id == null) {
            throw new IllegalArgumentException("El ID del beneficiario no puede ser nulo");
        }

        Beneficiary beneficiary = service.findById(id);
        if (beneficiary == null) {
            throw new EntityNotFoundException("Beneficiario", id);
        }

        try {
            service.deleteById(id);
            return DeleteResponse.success(id, "Beneficiario");
        } catch (Exception e) {
            throw new EntityDeletionException("Beneficiario", id,
                    "Error durante la eliminación física: " + e.getMessage());
        }
    }
}

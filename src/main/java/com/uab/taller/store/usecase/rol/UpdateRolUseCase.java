package com.uab.taller.store.usecase.rol;

import com.uab.taller.store.domain.Rol;
import com.uab.taller.store.domain.dto.request.UpdateRolRequest;
import com.uab.taller.store.domain.dto.response.RolResponse;
import com.uab.taller.store.exception.EntityNotFoundException;
import com.uab.taller.store.service.interfaces.IRolService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UpdateRolUseCase {

    @Autowired
    private IRolService rolService;

    @Autowired
    private RolValidationUseCase validationUseCase;

    @Autowired
    private RolMappingUseCase mappingUseCase;

    @Transactional
    public RolResponse update(Long id, UpdateRolRequest updateRequest) {
        // Validar la actualización del rol
        RolValidationUseCase.ValidationResult validation = validationUseCase.validateRolUpdate(id,
                updateRequest.getName());

        if (!validation.isValid()) {
            throw new IllegalArgumentException("Error de validación: " + validation.getErrorMessage());
        }

        // Obtener el rol existente
        Rol existingRol = rolService.findById(id);
        if (existingRol == null) {
            throw new EntityNotFoundException("Rol", id);
        }

        // Actualizar los campos
        Rol updatedRol = mappingUseCase.updateEntity(existingRol, updateRequest);

        // Guardar el rol actualizado
        Rol savedRol = rolService.update(updatedRol);

        // Convertir a DTO de respuesta
        return mappingUseCase.toResponse(savedRol);
    }

    // Método legacy para mantener compatibilidad
    public Rol update(Rol rol) {
        if (rol == null || rol.getId() == null) {
            throw new IllegalArgumentException("El rol y su ID son obligatorios");
        }

        // Validar usando el sistema de validación
        RolValidationUseCase.ValidationResult validation = validationUseCase.validateRolUpdate(rol.getId(),
                rol.getName());

        if (!validation.isValid()) {
            throw new IllegalArgumentException("Error de validación: " + validation.getErrorMessage());
        }

        rol.setChangeUser("SYSTEM");
        return rolService.update(rol);
    }
}

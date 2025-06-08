package com.uab.taller.store.usecase.rol;

import com.uab.taller.store.domain.Rol;
import com.uab.taller.store.domain.dto.request.RolRequest;
import com.uab.taller.store.domain.dto.response.RolResponse;
import com.uab.taller.store.service.interfaces.IRolService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CreateRolUseCase {

    @Autowired
    private IRolService rolService;

    @Autowired
    private RolValidationUseCase validationUseCase;

    @Autowired
    private RolMappingUseCase mappingUseCase;

    @Transactional
    public RolResponse save(RolRequest rolRequest) {
        // Validar la creación del rol
        RolValidationUseCase.ValidationResult validation = validationUseCase.validateRolCreation(rolRequest.getName());

        if (!validation.isValid()) {
            throw new IllegalArgumentException("Error de validación: " + validation.getErrorMessage());
        }

        // Convertir DTO a entidad
        Rol rol = mappingUseCase.toEntity(rolRequest);

        // Guardar el rol
        Rol savedRol = rolService.save(rol);

        // Convertir a DTO de respuesta
        return mappingUseCase.toResponse(savedRol);
    }

    // Método legacy para mantener compatibilidad
    public Rol save(Rol rol) {
        if (rol == null) {
            throw new IllegalArgumentException("El rol no puede ser nulo");
        }

        // Validar usando el sistema de validación
        RolValidationUseCase.ValidationResult validation = validationUseCase.validateRolCreation(rol.getName());

        if (!validation.isValid()) {
            throw new IllegalArgumentException("Error de validación: " + validation.getErrorMessage());
        }

        rol.setAddUser("SYSTEM");
        return rolService.save(rol);
    }
}

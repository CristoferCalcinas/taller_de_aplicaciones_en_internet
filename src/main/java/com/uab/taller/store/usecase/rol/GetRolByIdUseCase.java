package com.uab.taller.store.usecase.rol;

import com.uab.taller.store.domain.Rol;
import com.uab.taller.store.domain.dto.response.RolResponse;
import com.uab.taller.store.service.interfaces.IRolService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class GetRolByIdUseCase {

    @Autowired
    private IRolService rolService;

    @Autowired
    private RolMappingUseCase rolMappingUseCase;

    /**
     * Obtiene un rol por su ID.
     * 
     * @param id ID del rol a buscar
     * @return Rol encontrado en formato DTO
     * @throws RuntimeException si el rol no existe
     */
    public RolResponse getById(Long id) {
        Rol rol = rolService.findById(id);
        if (rol == null) {
            throw new RuntimeException("Rol no encontrado con ID: " + id);
        }
        return rolMappingUseCase.toResponse(rol);
    }
}

package com.uab.taller.store.usecase.rol;

import com.uab.taller.store.domain.Rol;
import com.uab.taller.store.domain.dto.response.RolResponse;
import com.uab.taller.store.repository.RolRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class GetRolByNameUseCase {

    @Autowired
    private RolRepository rolRepository;

    @Autowired
    private RolMappingUseCase rolMappingUseCase;

    /**
     * Obtiene un rol por su nombre.
     * 
     * @param name Nombre del rol a buscar (insensible a mayúsculas/minúsculas)
     * @return Rol encontrado en formato DTO
     * @throws RuntimeException si el rol no existe
     */
    public RolResponse getByName(String name) {
        Rol rol = rolRepository.findByNameIgnoreCase(name)
                .orElseThrow(() -> new RuntimeException("Rol no encontrado con nombre: " + name));
        return rolMappingUseCase.toResponse(rol);
    }
}

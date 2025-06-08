package com.uab.taller.store.usecase.rol;

import com.uab.taller.store.domain.dto.response.RolSummaryResponse;
import com.uab.taller.store.service.interfaces.IRolService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class GetAllRolesUseCase {

    @Autowired
    private IRolService rolService;

    @Autowired
    private RolMappingUseCase rolMappingUseCase;

    /**
     * Obtiene todos los roles activos del sistema.
     * 
     * @return Lista de roles en formato DTO resumido
     */
    public List<RolSummaryResponse> getAllRoles() {
        return rolService.findAll()
                .stream()
                .filter(rol -> !rol.isDeleted()) // Solo roles activos
                .map(rolMappingUseCase::toSummaryResponse)
                .collect(Collectors.toList());
    }
}

package com.uab.taller.store.usecase.rol;

import com.uab.taller.store.domain.Rol;
import com.uab.taller.store.domain.dto.request.RolRequest;
import com.uab.taller.store.domain.dto.request.UpdateRolRequest;
import com.uab.taller.store.domain.dto.response.RolResponse;
import com.uab.taller.store.domain.dto.response.RolSummaryResponse;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Caso de uso para mapear entidades Rol hacia y desde DTOs
 */
@Service
public class RolMappingUseCase {

    /**
     * Convierte un RolRequest a una entidad Rol
     */
    public Rol toEntity(RolRequest request) {
        if (request == null) {
            return null;
        }

        Rol rol = new Rol();
        rol.setName(request.getName());
        rol.setDescription(request.getDescription());
        rol.setAddUser("SYSTEM");

        return rol;
    }

    /**
     * Convierte un UpdateRolRequest a una entidad Rol (solo para actualización)
     */
    public Rol toEntity(UpdateRolRequest request) {
        if (request == null) {
            return null;
        }

        Rol rol = new Rol();
        rol.setName(request.getName());
        rol.setDescription(request.getDescription());
        rol.setChangeUser("SYSTEM");

        return rol;
    }

    /**
     * Actualiza una entidad Rol existente con los datos de UpdateRolRequest
     */
    public Rol updateEntity(Rol existing, UpdateRolRequest request) {
        if (existing == null || request == null) {
            return existing;
        }

        if (request.getName() != null) {
            existing.setName(request.getName());
        }
        if (request.getDescription() != null) {
            existing.setDescription(request.getDescription());
        }
        existing.setChangeUser("SYSTEM");

        return existing;
    }

    /**
     * Convierte una entidad Rol a RolResponse
     */
    public RolResponse toResponse(Rol rol) {
        return RolResponse.fromRol(rol);
    }

    /**
     * Convierte una entidad Rol a RolSummaryResponse
     */
    public RolSummaryResponse toSummaryResponse(Rol rol) {
        return RolSummaryResponse.fromRol(rol);
    }

    /**
     * Convierte una lista de entidades Rol a lista de RolResponse
     */
    public List<RolResponse> toResponseList(List<Rol> roles) {
        if (roles == null) {
            return null;
        }

        return roles.stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    /**
     * Convierte una lista de entidades Rol a lista de RolSummaryResponse
     */
    public List<RolSummaryResponse> toSummaryResponseList(List<Rol> roles) {
        if (roles == null) {
            return null;
        }

        return roles.stream()
                .map(this::toSummaryResponse)
                .collect(Collectors.toList());
    }
}

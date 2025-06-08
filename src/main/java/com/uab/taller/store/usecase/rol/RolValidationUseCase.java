package com.uab.taller.store.usecase.rol;

import com.uab.taller.store.domain.Rol;
import com.uab.taller.store.repository.RolRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Service
public class RolValidationUseCase {

    @Autowired
    private RolRepository rolRepository;

    // Roles del sistema que no se pueden eliminar
    private static final List<String> SYSTEM_ROLES = Arrays.asList("ADMIN", "USER", "MODERATOR");

    /**
     * Clase para encapsular el resultado de validación
     */
    public static class ValidationResult {
        private final boolean valid;
        private final List<String> errors;

        public ValidationResult(boolean valid, List<String> errors) {
            this.valid = valid;
            this.errors = errors != null ? errors : new ArrayList<>();
        }

        public boolean isValid() {
            return valid;
        }

        public List<String> getErrors() {
            return errors;
        }

        public String getErrorMessage() {
            return String.join("; ", errors);
        }
    }

    /**
     * Valida si se puede crear un rol con el nombre especificado
     */
    public ValidationResult validateRolCreation(String name) {
        List<String> errors = new ArrayList<>();

        if (name == null || name.trim().isEmpty()) {
            errors.add("El nombre del rol es obligatorio");
            return new ValidationResult(false, errors);
        }

        // Validar formato del nombre
        if (!name.matches("^[A-Z][A-Z_]*$")) {
            errors.add("El nombre del rol debe estar en mayúsculas y puede contener guiones bajos");
        }

        // Validar longitud
        if (name.length() < 2 || name.length() > 50) {
            errors.add("El nombre del rol debe tener entre 2 y 50 caracteres");
        } // Verificar que no existe un rol con el mismo nombre
        try {
            Optional<Rol> existingRol = rolRepository.findByNameIgnoreCase(name);
            if (existingRol.isPresent()) {
                errors.add("Ya existe un rol con el nombre: " + name);
            }
        } catch (Exception e) {
            // Si no existe, está bien
        }

        return new ValidationResult(errors.isEmpty(), errors);
    }

    /**
     * Valida si se puede actualizar un rol
     */
    public ValidationResult validateRolUpdate(Long rolId, String newName) {
        List<String> errors = new ArrayList<>();

        if (rolId == null) {
            errors.add("El ID del rol es obligatorio");
            return new ValidationResult(false, errors);
        } // Verificar que el rol existe
        try {
            Optional<Rol> existingRolOpt = rolRepository.findById(rolId);
            if (!existingRolOpt.isPresent()) {
                errors.add("El rol no existe");
                return new ValidationResult(false, errors);
            }

            Rol existingRol = existingRolOpt.get();
            if (existingRol.isDeleted()) {
                errors.add("No se puede actualizar un rol eliminado");
            }

            // Si se está cambiando el nombre
            if (newName != null && !newName.equals(existingRol.getName())) {
                // Validar que el nuevo nombre no existe
                try {
                    Optional<Rol> rolWithNewNameOpt = rolRepository.findByNameIgnoreCase(newName);
                    if (rolWithNewNameOpt.isPresent() && !rolWithNewNameOpt.get().getId().equals(rolId)) {
                        errors.add("Ya existe un rol con el nombre: " + newName);
                    }
                } catch (Exception e) {
                    // Si no existe, está bien
                }

                // Validar formato del nuevo nombre
                if (!newName.matches("^[A-Z][A-Z_]*$")) {
                    errors.add("El nombre del rol debe estar en mayúsculas y puede contener guiones bajos");
                }
            }

        } catch (Exception e) {
            errors.add("Error al verificar el rol: " + e.getMessage());
        }

        return new ValidationResult(errors.isEmpty(), errors);
    }

    /**
     * Valida si se puede eliminar un rol
     */
    public ValidationResult validateRolDeletion(Long rolId) {
        List<String> errors = new ArrayList<>();

        if (rolId == null) {
            errors.add("El ID del rol es obligatorio");
            return new ValidationResult(false, errors);
        }

        try {
            Optional<Rol> rolOpt = rolRepository.findById(rolId);
            if (!rolOpt.isPresent()) {
                errors.add("El rol no existe");
                return new ValidationResult(false, errors);
            }

            Rol rol = rolOpt.get();

            if (rol.isDeleted()) {
                errors.add("El rol ya está eliminado");
            }

            // Verificar si es un rol del sistema
            if (SYSTEM_ROLES.contains(rol.getName())) {
                errors.add("No se puede eliminar el rol del sistema: " + rol.getName());
            }

            // TODO: Verificar si hay usuarios con este rol
            // Esta validación se puede implementar cuando se tenga acceso al servicio de
            // usuarios

        } catch (Exception e) {
            errors.add("Error al verificar el rol: " + e.getMessage());
        }

        return new ValidationResult(errors.isEmpty(), errors);
    }

    /**
     * Valida si se puede hacer eliminación forzada de un rol
     */
    public ValidationResult validateRolForceDeletion(Long rolId) {
        List<String> errors = new ArrayList<>();

        if (rolId == null) {
            errors.add("El ID del rol es obligatorio");
            return new ValidationResult(false, errors);
        }

        try {
            Optional<Rol> rolOpt = rolRepository.findById(rolId);
            if (!rolOpt.isPresent()) {
                errors.add("El rol no existe");
                return new ValidationResult(false, errors);
            }

            Rol rol = rolOpt.get();

            // Los roles del sistema no se pueden eliminar físicamente nunca
            if (SYSTEM_ROLES.contains(rol.getName())) {
                errors.add("No se puede eliminar físicamente el rol del sistema: " + rol.getName());
            }

        } catch (Exception e) {
            errors.add("Error al verificar el rol: " + e.getMessage());
        }

        return new ValidationResult(errors.isEmpty(), errors);
    }
}

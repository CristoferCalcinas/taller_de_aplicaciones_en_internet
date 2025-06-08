package com.uab.taller.store.controller;

import com.uab.taller.store.domain.dto.request.RolRequest;
import com.uab.taller.store.domain.dto.request.UpdateRolRequest;
import com.uab.taller.store.domain.dto.response.DeleteResponse;
import com.uab.taller.store.domain.dto.response.RolResponse;
import com.uab.taller.store.domain.dto.response.RolSummaryResponse;
import com.uab.taller.store.usecase.rol.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Gestión de Roles", description = "API para la gestión completa de roles del sistema")
@RestController
@RequestMapping("/api/v1/rol")
@Validated
@CrossOrigin(origins = "*", maxAge = 3600)
public class RolController {

    @Autowired
    private CreateRolUseCase createRolUseCase;

    @Autowired
    private GetAllRolesUseCase getAllRolesUseCase;

    @Autowired
    private GetRolByIdUseCase getRolByIdUseCase;

    @Autowired
    private GetRolByNameUseCase getRolByNameUseCase;

    @Autowired
    private DeleteRolUseCase deleteRolUseCase;

    @Autowired
    private UpdateRolUseCase updateRolUseCase;

    /**
     * Crea un nuevo rol en el sistema.
     * 
     * @param rolRequest Datos del rol a crear
     * @return ResponseEntity con el rol creado
     */
    @Operation(summary = "Crear un nuevo rol", description = "Crea un nuevo rol en el sistema con validaciones de negocio. "
            +
            "El nombre del rol debe ser único y seguir el formato en mayúsculas.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Rol creado exitosamente", content = @Content(mediaType = "application/json", schema = @Schema(implementation = RolResponse.class))),
            @ApiResponse(responseCode = "400", description = "Datos de entrada inválidos o rol ya existe", content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "422", description = "Error de validación de negocio", content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor", content = @Content(mediaType = "application/json"))
    })
    @PostMapping
    public ResponseEntity<RolResponse> createRol(
            @Parameter(description = "Datos del rol a crear", required = true) @Valid @RequestBody RolRequest rolRequest) {

        RolResponse createdRol = createRolUseCase.save(rolRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdRol);
    }

    /**
     * Obtiene todos los roles del sistema.
     * 
     * @return ResponseEntity con la lista de roles
     */
    @Operation(summary = "Obtener todos los roles", description = "Recupera una lista de todos los roles activos del sistema con información resumida.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista de roles obtenida exitosamente", content = @Content(mediaType = "application/json", schema = @Schema(implementation = RolSummaryResponse.class))),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor", content = @Content(mediaType = "application/json"))
    })
    @GetMapping
    public ResponseEntity<List<RolSummaryResponse>> getAllRoles() {
        List<RolSummaryResponse> roles = getAllRolesUseCase.getAllRoles();
        return ResponseEntity.ok(roles);
    }

    /**
     * Obtiene un rol específico por su ID.
     * 
     * @param id ID del rol a buscar
     * @return ResponseEntity con el rol encontrado
     */
    @Operation(summary = "Obtener rol por ID", description = "Recupera un rol específico utilizando su identificador único.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Rol encontrado exitosamente", content = @Content(mediaType = "application/json", schema = @Schema(implementation = RolResponse.class))),
            @ApiResponse(responseCode = "400", description = "ID de rol inválido", content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "404", description = "Rol no encontrado", content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor", content = @Content(mediaType = "application/json"))
    })
    @GetMapping("/{id}")
    public ResponseEntity<RolResponse> getRolById(
            @Parameter(description = "ID único del rol", required = true, example = "1") @PathVariable @NotNull @Positive Long id) {

        RolResponse rol = getRolByIdUseCase.getById(id);
        return ResponseEntity.ok(rol);
    }

    /**
     * Obtiene un rol específico por su nombre.
     * 
     * @param name Nombre del rol a buscar
     * @return ResponseEntity con el rol encontrado
     */
    @Operation(summary = "Obtener rol por nombre", description = "Recupera un rol específico utilizando su nombre. La búsqueda no es sensible a mayúsculas y minúsculas.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Rol encontrado exitosamente", content = @Content(mediaType = "application/json", schema = @Schema(implementation = RolResponse.class))),
            @ApiResponse(responseCode = "400", description = "Nombre de rol inválido", content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "404", description = "Rol no encontrado", content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor", content = @Content(mediaType = "application/json"))
    })
    @GetMapping("/name/{name}")
    public ResponseEntity<RolResponse> getRolByName(
            @Parameter(description = "Nombre del rol a buscar", required = true, example = "ADMIN") @PathVariable @NotBlank String name) {

        RolResponse rol = getRolByNameUseCase.getByName(name);
        return ResponseEntity.ok(rol);
    }

    /**
     * Actualiza un rol existente.
     * 
     * @param id            ID del rol a actualizar
     * @param updateRequest Datos a actualizar del rol
     * @return ResponseEntity con el rol actualizado
     */
    @Operation(summary = "Actualizar un rol existente", description = "Actualiza los datos de un rol existente. Los roles del sistema (ADMIN, USER, MODERATOR) "
            +
            "tienen restricciones especiales de actualización.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Rol actualizado exitosamente", content = @Content(mediaType = "application/json", schema = @Schema(implementation = RolResponse.class))),
            @ApiResponse(responseCode = "400", description = "Datos de entrada inválidos", content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "404", description = "Rol no encontrado", content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "422", description = "Error de validación de negocio", content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor", content = @Content(mediaType = "application/json"))
    })
    @PutMapping("/{id}")
    public ResponseEntity<RolResponse> updateRol(
            @Parameter(description = "ID único del rol a actualizar", required = true, example = "1") @PathVariable @NotNull @Positive Long id,
            @Parameter(description = "Datos actualizados del rol", required = true) @Valid @RequestBody UpdateRolRequest updateRequest) {

        RolResponse updatedRol = updateRolUseCase.update(id, updateRequest);
        return ResponseEntity.ok(updatedRol);
    }

    /**
     * Elimina un rol de forma lógica (soft delete).
     * 
     * @param id ID del rol a eliminar
     * @return ResponseEntity con el resultado de la eliminación
     */
    @Operation(summary = "Eliminar rol (eliminación lógica)", description = "Realiza una eliminación lógica del rol, marcándolo como eliminado pero conservando "
            +
            "el registro en la base de datos. Los roles del sistema no pueden ser eliminados.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Rol eliminado exitosamente", content = @Content(mediaType = "application/json", schema = @Schema(implementation = DeleteResponse.class))),
            @ApiResponse(responseCode = "400", description = "ID de rol inválido", content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "404", description = "Rol no encontrado", content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "422", description = "No se puede eliminar el rol (rol del sistema o tiene dependencias)", content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor", content = @Content(mediaType = "application/json"))
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<DeleteResponse> deleteRol(
            @Parameter(description = "ID único del rol a eliminar", required = true, example = "1") @PathVariable @NotNull @Positive Long id) {

        DeleteResponse response = deleteRolUseCase.deleteById(id);
        return ResponseEntity.ok(response);
    }

    /**
     * Elimina un rol de forma física (eliminación completa).
     * 
     * @param id ID del rol a eliminar
     * @return ResponseEntity con el resultado de la eliminación
     */
    @Operation(summary = "Eliminar rol físicamente (eliminación completa)", description = "Realiza una eliminación física del rol, removiendo completamente el registro "
            +
            "de la base de datos. Esta operación es irreversible y los roles del sistema nunca pueden ser eliminados físicamente.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Rol eliminado físicamente exitosamente", content = @Content(mediaType = "application/json", schema = @Schema(implementation = DeleteResponse.class))),
            @ApiResponse(responseCode = "400", description = "ID de rol inválido", content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "404", description = "Rol no encontrado", content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "422", description = "No se puede eliminar físicamente el rol (rol del sistema)", content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor", content = @Content(mediaType = "application/json"))
    })
    @DeleteMapping("/{id}/force")
    public ResponseEntity<DeleteResponse> forceDeleteRol(
            @Parameter(description = "ID único del rol a eliminar físicamente", required = true, example = "1") @PathVariable @NotNull @Positive Long id) {

        DeleteResponse response = deleteRolUseCase.forceDeleteById(id);
        return ResponseEntity.ok(response);
    }
}

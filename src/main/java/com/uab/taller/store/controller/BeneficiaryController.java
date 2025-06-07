package com.uab.taller.store.controller;

import com.uab.taller.store.domain.Beneficiary;
import com.uab.taller.store.domain.dto.request.BeneficiaryRequest;
import com.uab.taller.store.domain.dto.request.UpdateBeneficiaryRequest;
import com.uab.taller.store.domain.dto.response.BeneficiaryResponse;
import com.uab.taller.store.domain.dto.response.DeleteResponse;
import com.uab.taller.store.usecase.beneficiary.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin
@RestController
@RequestMapping("/api/v1/beneficiary")
@Tag(name = "Beneficiary", description = "API para gestión completa de beneficiarios bancarios")
public class BeneficiaryController {

    @Autowired
    private CreateBeneficiaryUseCase createBeneficiaryUseCase;

    @Autowired
    private GetAllBeneficiaryUseCase getAllBeneficiaryUseCase;

    @Autowired
    private GetBeneficiaryByIdUseCase getBeneficiaryByIdUseCase;

    @Autowired
    private DeleteBeneficiaryUseCase deleteBeneficiaryUseCase;

    @Autowired
    private UpdateBeneficiaryUseCase updateBeneficiaryUseCase;

    @Autowired
    private GetBeneficiariesByUserUseCase getBeneficiariesByUserUseCase;

    @Autowired
    private BeneficiaryMappingUseCase mappingUseCase;

    @Autowired
    private BeneficiaryValidationUseCase validationUseCase;

    @Operation(summary = "Crear beneficiario", description = "Crea un nuevo beneficiario asociando un usuario con una cuenta bancaria. "
            +
            "Permite especificar un alias y descripción opcional para facilitar la identificación.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Beneficiario creado exitosamente"),
            @ApiResponse(responseCode = "400", description = "Datos de entrada inválidos o error de validación"),
            @ApiResponse(responseCode = "404", description = "Usuario o cuenta no encontrados"),
            @ApiResponse(responseCode = "409", description = "Ya existe un beneficiario activo para esta combinación usuario-cuenta")
    })
    @PostMapping
    public ResponseEntity<BeneficiaryResponse> createBeneficiary(
            @Parameter(description = "Datos del beneficiario a crear", required = true) @Valid @RequestBody BeneficiaryRequest beneficiaryRequest) {

        Beneficiary beneficiary = createBeneficiaryUseCase.save(beneficiaryRequest);
        BeneficiaryResponse response = mappingUseCase.toResponse(beneficiary);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @Operation(summary = "Listar todos los beneficiarios", description = "Obtiene una lista completa de todos los beneficiarios en el sistema, "
            +
            "incluyendo información básica del usuario y cuenta asociados.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista de beneficiarios obtenida exitosamente"),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @GetMapping
    public ResponseEntity<List<BeneficiaryResponse>> getAllBeneficiaries() {
        List<Beneficiary> beneficiaries = getAllBeneficiaryUseCase.getAll();
        List<BeneficiaryResponse> responses = mappingUseCase.toResponseList(beneficiaries);
        return ResponseEntity.ok(responses);
    }

    @Operation(summary = "Obtener beneficiario por ID", description = "Busca y retorna un beneficiario específico usando su identificador único, "
            +
            "incluyendo toda la información del usuario y cuenta asociados.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Beneficiario encontrado exitosamente"),
            @ApiResponse(responseCode = "404", description = "Beneficiario no encontrado"),
            @ApiResponse(responseCode = "400", description = "ID inválido")
    })
    @GetMapping("/{id}")
    public ResponseEntity<BeneficiaryResponse> getBeneficiaryById(
            @Parameter(description = "ID único del beneficiario", required = true, example = "1") @PathVariable Long id) {

        if (id == null || id <= 0) {
            return ResponseEntity.badRequest().build();
        }

        Beneficiary beneficiary = getBeneficiaryByIdUseCase.getById(id);
        if (beneficiary == null) {
            return ResponseEntity.notFound().build();
        }

        BeneficiaryResponse response = mappingUseCase.toResponse(beneficiary);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Actualizar beneficiario", description = "Actualiza la información de un beneficiario existente. "
            +
            "Permite cambiar el usuario, cuenta, alias y descripción. " +
            "Incluye validaciones de negocio para asegurar la integridad de los datos.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Beneficiario actualizado exitosamente"),
            @ApiResponse(responseCode = "400", description = "Datos de entrada inválidos o error de validación"),
            @ApiResponse(responseCode = "404", description = "Beneficiario, usuario o cuenta no encontrados"),
            @ApiResponse(responseCode = "409", description = "Conflicto con las reglas de negocio")
    })
    @PutMapping
    public ResponseEntity<BeneficiaryResponse> updateBeneficiary(
            @Parameter(description = "Datos actualizados del beneficiario", required = true) @Valid @RequestBody UpdateBeneficiaryRequest updateRequest) {

        Beneficiary beneficiary = updateBeneficiaryUseCase.update(updateRequest);
        BeneficiaryResponse response = mappingUseCase.toResponse(beneficiary);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Eliminar beneficiario (soft delete)", description = "Realiza una eliminación lógica del beneficiario, marcándolo como eliminado "
            +
            "pero conservando el registro en la base de datos para auditoría. " +
            "El beneficiario no aparecerá en consultas normales pero se mantiene para trazabilidad.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Beneficiario eliminado exitosamente"),
            @ApiResponse(responseCode = "404", description = "Beneficiario no encontrado"),
            @ApiResponse(responseCode = "400", description = "ID inválido"),
            @ApiResponse(responseCode = "409", description = "No se puede eliminar debido a restricciones de negocio")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<DeleteResponse> deleteBeneficiaryById(
            @Parameter(description = "ID único del beneficiario a eliminar", required = true, example = "1") @PathVariable Long id) {
        if (id == null || id <= 0) {
            return ResponseEntity.badRequest()
                    .body(DeleteResponse.failure("ID inválido"));
        }

        DeleteResponse response = deleteBeneficiaryUseCase.deleteById(id);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Eliminar beneficiario permanentemente", description = "Realiza una eliminación física del beneficiario, removiendo completamente "
            +
            "el registro de la base de datos. Esta operación es irreversible y debe usarse " +
            "con extrema precaución.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Beneficiario eliminado permanentemente"),
            @ApiResponse(responseCode = "404", description = "Beneficiario no encontrado"),
            @ApiResponse(responseCode = "400", description = "ID inválido"),
            @ApiResponse(responseCode = "409", description = "No se puede eliminar debido a restricciones de negocio")
    })
    @DeleteMapping("/{id}/force")
    public ResponseEntity<DeleteResponse> forceDeleteBeneficiaryById(
            @Parameter(description = "ID único del beneficiario a eliminar permanentemente", required = true, example = "1") @PathVariable Long id) {
        if (id == null || id <= 0) {
            return ResponseEntity.badRequest()
                    .body(DeleteResponse.failure("ID inválido"));
        }

        DeleteResponse response = deleteBeneficiaryUseCase.forceDeleteById(id);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Obtener beneficiarios por usuario", description = "Busca y retorna todos los beneficiarios (activos e inactivos) asociados "
            +
            "a un usuario específico. Útil para ver el historial completo de beneficiarios " +
            "de un usuario.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista de beneficiarios obtenida exitosamente"),
            @ApiResponse(responseCode = "400", description = "ID de usuario inválido"),
            @ApiResponse(responseCode = "404", description = "Usuario no encontrado")
    })
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<BeneficiaryResponse>> getBeneficiariesByUser(
            @Parameter(description = "ID único del usuario", required = true, example = "1") @PathVariable Long userId) {

        if (userId == null || userId <= 0) {
            return ResponseEntity.badRequest().build();
        }

        List<Beneficiary> beneficiaries = getBeneficiariesByUserUseCase.getBeneficiariesByUser(userId);
        List<BeneficiaryResponse> responses = mappingUseCase.toResponseList(beneficiaries);
        return ResponseEntity.ok(responses);
    }

    @Operation(summary = "Obtener beneficiarios activos por usuario", description = "Busca y retorna únicamente los beneficiarios activos (no eliminados) "
            +
            "asociados a un usuario específico. Esta es la consulta más común para " +
            "operaciones transaccionales.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista de beneficiarios activos obtenida exitosamente"),
            @ApiResponse(responseCode = "400", description = "ID de usuario inválido"),
            @ApiResponse(responseCode = "404", description = "Usuario no encontrado")
    })
    @GetMapping("/user/{userId}/active")
    public ResponseEntity<List<BeneficiaryResponse>> getActiveBeneficiariesByUser(
            @Parameter(description = "ID único del usuario", required = true, example = "1") @PathVariable Long userId) {

        if (userId == null || userId <= 0) {
            return ResponseEntity.badRequest().build();
        }

        List<Beneficiary> beneficiaries = getBeneficiariesByUserUseCase.getActiveBeneficiariesByUser(userId);
        List<BeneficiaryResponse> responses = mappingUseCase.toResponseList(beneficiaries);
        return ResponseEntity.ok(responses);
    }

    @Operation(summary = "Validar creación de beneficiario", description = "Valida si se puede crear un beneficiario para una combinación específica "
            +
            "de usuario y cuenta. Verifica reglas de negocio sin crear el registro.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Validación completada"),
            @ApiResponse(responseCode = "400", description = "Parámetros inválidos")
    })
    @PostMapping("/validate-creation")
    public ResponseEntity<BeneficiaryValidationUseCase.ValidationResult> validateBeneficiaryCreation(
            @Parameter(description = "ID del usuario", required = true, example = "1") @RequestParam Long userId,
            @Parameter(description = "ID de la cuenta", required = true, example = "1") @RequestParam Long accountId) {

        BeneficiaryValidationUseCase.ValidationResult result = validationUseCase.validateBeneficiaryCreation(userId,
                accountId);
        return ResponseEntity.ok(result);
    }

    @Operation(summary = "Validar actualización de beneficiario", description = "Valida si se puede actualizar un beneficiario específico con nuevos valores "
            +
            "de usuario y cuenta. Verifica reglas de negocio sin realizar la actualización.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Validación completada"),
            @ApiResponse(responseCode = "400", description = "Parámetros inválidos")
    })
    @PostMapping("/{id}/validate-update")
    public ResponseEntity<BeneficiaryValidationUseCase.ValidationResult> validateBeneficiaryUpdate(
            @Parameter(description = "ID del beneficiario", required = true, example = "1") @PathVariable Long id,
            @Parameter(description = "Nuevo ID del usuario", example = "1") @RequestParam(required = false) Long userId,
            @Parameter(description = "Nuevo ID de la cuenta", example = "1") @RequestParam(required = false) Long accountId) {

        BeneficiaryValidationUseCase.ValidationResult result = validationUseCase.validateBeneficiaryUpdate(id, userId,
                accountId);
        return ResponseEntity.ok(result);
    }

    @Operation(summary = "Validar eliminación de beneficiario", description = "Valida si se puede eliminar un beneficiario específico. "
            +
            "Verifica reglas de negocio sin realizar la eliminación.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Validación completada"),
            @ApiResponse(responseCode = "400", description = "ID inválido")
    })
    @PostMapping("/{id}/validate-deletion")
    public ResponseEntity<BeneficiaryValidationUseCase.ValidationResult> validateBeneficiaryDeletion(
            @Parameter(description = "ID del beneficiario", required = true, example = "1") @PathVariable Long id) {

        BeneficiaryValidationUseCase.ValidationResult result = validationUseCase.validateBeneficiaryDeletion(id);
        return ResponseEntity.ok(result);
    }
}

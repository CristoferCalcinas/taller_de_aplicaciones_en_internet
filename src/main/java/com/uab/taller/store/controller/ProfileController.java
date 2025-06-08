package com.uab.taller.store.controller;

import com.uab.taller.store.domain.Profile;
import com.uab.taller.store.domain.dto.request.ProfileRequest;
import com.uab.taller.store.domain.dto.request.UpdateProfileRequest;
import com.uab.taller.store.domain.dto.response.ProfileResponse;
import com.uab.taller.store.domain.dto.response.ProfileSummaryResponse;
import com.uab.taller.store.domain.dto.response.DeleteResponse;
import com.uab.taller.store.usecase.profile.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin
@RestController
@RequestMapping("/api/v1/profile")
@Tag(name = "Profile", description = "API para gestión completa de perfiles de usuario")
@Validated
public class ProfileController {

    @Autowired
    private CreateProfileUseCase createProfileUseCase;

    @Autowired
    private GetAllProfilesUseCase getAllProfilesUseCase;

    @Autowired
    private GetProfileByIdUseCase getProfileByIdUseCase;

    @Autowired
    private DeleteProfileUseCase deleteProfileUseCase;

    @Autowired
    private UpdateProfileUseCase updateProfileUseCase;

    @Autowired
    private ProfileMappingUseCase profileMappingUseCase;

    @Operation(summary = "Crear un nuevo perfil", description = "Crea un nuevo perfil de usuario con toda la información requerida")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Perfil creado exitosamente", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ProfileResponse.class))),
            @ApiResponse(responseCode = "400", description = "Datos de entrada inválidos"),
            @ApiResponse(responseCode = "409", description = "El perfil ya existe (CI duplicado)")
    })
    @PostMapping
    public ResponseEntity<ProfileResponse> createProfile(
            @Valid @RequestBody ProfileRequest profileRequest) {
        Profile createdProfile = createProfileUseCase.save(profileRequest);
        ProfileResponse response = profileMappingUseCase.toProfileResponse(createdProfile);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @Operation(summary = "Obtener todos los perfiles", description = "Obtiene una lista resumida de todos los perfiles activos del sistema")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista de perfiles obtenida exitosamente", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ProfileSummaryResponse.class)))
    })
    @GetMapping
    public ResponseEntity<List<ProfileSummaryResponse>> getAllProfiles() {
        List<Profile> profiles = getAllProfilesUseCase.getAll();
        List<ProfileSummaryResponse> response = profileMappingUseCase.toProfileSummaryResponseList(profiles);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Obtener perfil por ID", description = "Obtiene la información completa de un perfil específico por su ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Perfil encontrado exitosamente", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ProfileResponse.class))),
            @ApiResponse(responseCode = "404", description = "Perfil no encontrado")
    })
    @GetMapping("/{id}")
    public ResponseEntity<ProfileResponse> getProfileById(
            @Parameter(description = "ID único del perfil", required = true, example = "1") @PathVariable Long id) {
        Profile profile = getProfileByIdUseCase.getById(id);
        ProfileResponse response = profileMappingUseCase.toProfileResponse(profile);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Actualizar perfil", description = "Actualiza la información de un perfil existente")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Perfil actualizado exitosamente", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ProfileResponse.class))),
            @ApiResponse(responseCode = "400", description = "Datos de entrada inválidos"),
            @ApiResponse(responseCode = "404", description = "Perfil no encontrado")
    })
    @PutMapping("/{id}")
    public ResponseEntity<ProfileResponse> updateProfile(
            @Parameter(description = "ID único del perfil", required = true, example = "1") @PathVariable Long id,
            @Valid @RequestBody UpdateProfileRequest updateRequest) {
        // Convertir el DTO a entidad incluyendo el ID
        Profile profileToUpdate = profileMappingUseCase.toProfile(updateRequest);
        profileToUpdate.setId(id);

        Profile updatedProfile = updateProfileUseCase.update(profileToUpdate);
        ProfileResponse response = profileMappingUseCase.toProfileResponse(updatedProfile);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Eliminar perfil (soft delete)", description = "Realiza una eliminación lógica del perfil, marcándolo como inactivo")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Perfil eliminado exitosamente", content = @Content(mediaType = "application/json", schema = @Schema(implementation = DeleteResponse.class))),
            @ApiResponse(responseCode = "404", description = "Perfil no encontrado")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<DeleteResponse> deleteProfile(
            @Parameter(description = "ID único del perfil", required = true, example = "1") @PathVariable Long id) {
        DeleteResponse response = deleteProfileUseCase.deleteById(id);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Eliminar perfil permanentemente", description = "Realiza una eliminación física del perfil de la base de datos (irreversible)")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Perfil eliminado permanentemente", content = @Content(mediaType = "application/json", schema = @Schema(implementation = DeleteResponse.class))),
            @ApiResponse(responseCode = "404", description = "Perfil no encontrado")
    })
    @DeleteMapping("/{id}/force")
    public ResponseEntity<DeleteResponse> forceDeleteProfile(
            @Parameter(description = "ID único del perfil", required = true, example = "1") @PathVariable Long id) {
        DeleteResponse response = deleteProfileUseCase.forceDeleteById(id);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Obtener resumen de perfil", description = "Obtiene información básica y resumida de un perfil específico")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Resumen del perfil obtenido exitosamente", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ProfileSummaryResponse.class))),
            @ApiResponse(responseCode = "404", description = "Perfil no encontrado")
    })
    @GetMapping("/{id}/summary")
    public ResponseEntity<ProfileSummaryResponse> getProfileSummary(
            @Parameter(description = "ID único del perfil", required = true, example = "1") @PathVariable Long id) {
        Profile profile = getProfileByIdUseCase.getById(id);
        ProfileSummaryResponse response = profileMappingUseCase.toProfileSummaryResponse(profile);
        return ResponseEntity.ok(response);
    }
}

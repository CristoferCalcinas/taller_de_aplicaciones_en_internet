package com.uab.taller.store.controller;

import com.uab.taller.store.domain.dto.request.CreateUserRequest;
import com.uab.taller.store.domain.dto.request.GetUserByEmailRequest;
import com.uab.taller.store.domain.dto.request.UpdateUserRequest;
import com.uab.taller.store.domain.dto.request.UserLoginRequest;
import com.uab.taller.store.domain.dto.response.DeleteResponse;
import com.uab.taller.store.domain.dto.response.UserLoginResponse;
import com.uab.taller.store.domain.dto.response.UserResponse;
import com.uab.taller.store.domain.dto.response.UserSummaryResponse;
import com.uab.taller.store.usecase.user.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin
@RestController
@RequestMapping(value = "/api/v1/users")
@Validated
@Tag(name = "Gestión de Usuarios", description = "API para la gestión completa de usuarios del sistema bancario")
public class UserController {

    @Autowired
    private GetUsersUseCase getUsersUseCase;

    @Autowired
    private GetUserUseCase getUserUseCase;

    @Autowired
    private DeleteUserUseCase deleteUserUseCase;

    @Autowired
    private CreateUserUseCase createUserUseCase;

    @Autowired
    private UpdateUserUseCase updateUserUseCase;

    @Autowired
    private GetUserByEmailUseCase getUserByEmailUseCase;

    @Autowired
    private UserLoginUseCase userLoginUseCase;

    @Operation(summary = "Obtener todos los usuarios", description = "Recupera una lista de todos los usuarios activos del sistema con información resumida. "
            +
            "No incluye usuarios eliminados ni información sensible como contraseñas.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista de usuarios obtenida exitosamente", content = @Content(schema = @Schema(implementation = UserSummaryResponse.class))),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @GetMapping
    public ResponseEntity<List<UserSummaryResponse>> getAllUsers() {
        List<UserSummaryResponse> users = getUsersUseCase.getAllUsers();
        return ResponseEntity.ok(users);
    }

    @Operation(summary = "Obtener usuario por ID", description = "Recupera la información completa de un usuario específico mediante su identificador único. "
            +
            "Incluye datos del perfil, rol y resumen de cuentas asociadas.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Usuario encontrado exitosamente", content = @Content(schema = @Schema(implementation = UserResponse.class))),
            @ApiResponse(responseCode = "404", description = "Usuario no encontrado"),
            @ApiResponse(responseCode = "400", description = "ID de usuario inválido")
    })
    @GetMapping("/{id}")
    public ResponseEntity<UserResponse> getUserById(
            @Parameter(description = "ID único del usuario", example = "1", required = true) @PathVariable @Min(value = 1, message = "El ID debe ser mayor que 0") Long id) {

        UserResponse user = getUserUseCase.getByUserId(id);
        return ResponseEntity.ok(user);
    }

    @Operation(summary = "Crear nuevo usuario", description = "Registra un nuevo usuario en el sistema. Opcionalmente puede crear una cuenta bancaria "
            +
            "automáticamente si se proporcionan los datos de saldo inicial y tipo de cuenta. " +
            "El email debe ser único en el sistema.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Usuario creado exitosamente", content = @Content(schema = @Schema(implementation = UserResponse.class))),
            @ApiResponse(responseCode = "400", description = "Datos de entrada inválidos o email ya existe"),
            @ApiResponse(responseCode = "500", description = "Error interno durante la creación")
    })
    @PostMapping
    public ResponseEntity<UserResponse> createUser(
            @Parameter(description = "Datos del nuevo usuario", required = true) @Valid @RequestBody CreateUserRequest createUserRequest) {

        UserResponse newUser = createUserUseCase.save(createUserRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(newUser);
    }

    @Operation(summary = "Actualizar usuario existente", description = "Modifica los datos de un usuario existente. Solo se actualizarán los campos proporcionados "
            +
            "en la petición (actualización parcial). Los campos nulos o vacíos serán ignorados.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Usuario actualizado exitosamente", content = @Content(schema = @Schema(implementation = UserResponse.class))),
            @ApiResponse(responseCode = "404", description = "Usuario no encontrado"),
            @ApiResponse(responseCode = "400", description = "Datos de entrada inválidos"),
            @ApiResponse(responseCode = "409", description = "Email ya está en uso por otro usuario")
    })
    @PutMapping("/{id}")
    public ResponseEntity<UserResponse> updateUser(
            @Parameter(description = "ID único del usuario a actualizar", example = "1", required = true) @PathVariable @Min(value = 1, message = "El ID debe ser mayor que 0") Long id,
            @Parameter(description = "Datos a actualizar del usuario", required = true) @Valid @RequestBody UpdateUserRequest updateUserRequest) {

        UserResponse updatedUser = updateUserUseCase.updateUser(id, updateUserRequest);
        return ResponseEntity.ok(updatedUser);
    }

    @Operation(summary = "Eliminar usuario (eliminación lógica)", description = "Realiza una eliminación lógica del usuario, marcándolo como eliminado pero conservando "
            +
            "sus datos en la base de datos. El usuario no podrá iniciar sesión y no aparecerá " +
            "en las consultas normales. Valida que no tenga cuentas con saldo positivo.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Usuario eliminado exitosamente", content = @Content(schema = @Schema(implementation = DeleteResponse.class))),
            @ApiResponse(responseCode = "404", description = "Usuario no encontrado"),
            @ApiResponse(responseCode = "409", description = "No se puede eliminar el usuario debido a restricciones"),
            @ApiResponse(responseCode = "400", description = "ID de usuario inválido")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<DeleteResponse> deleteUser(
            @Parameter(description = "ID único del usuario a eliminar", example = "1", required = true) @PathVariable @Min(value = 1, message = "El ID debe ser mayor que 0") Long id) {

        DeleteResponse response = deleteUserUseCase.deleteUserById(id);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Eliminar usuario permanentemente (solo administradores)", description = "Realiza una eliminación física permanente del usuario y todos sus datos relacionados. "
            +
            "Esta operación es irreversible y solo debe ser utilizada por administradores del sistema " +
            "en casos excepcionales. PRECAUCIÓN: Se perderán todos los datos asociados.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Usuario eliminado permanentemente", content = @Content(schema = @Schema(implementation = DeleteResponse.class))),
            @ApiResponse(responseCode = "404", description = "Usuario no encontrado"),
            @ApiResponse(responseCode = "403", description = "No autorizado para esta operación"),
            @ApiResponse(responseCode = "400", description = "ID de usuario inválido")
    })
    @DeleteMapping("/{id}/force")
    public ResponseEntity<DeleteResponse> forceDeleteUser(
            @Parameter(description = "ID único del usuario a eliminar permanentemente", example = "1", required = true) @PathVariable @Min(value = 1, message = "El ID debe ser mayor que 0") Long id) {

        DeleteResponse response = deleteUserUseCase.forceDeleteUserById(id);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Buscar usuario por email", description = "Busca y retorna la información completa de un usuario utilizando su dirección de correo "
            +
            "electrónico como criterio de búsqueda. Útil para verificar la existencia de cuentas " +
            "o recuperar información de usuario.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Usuario encontrado exitosamente", content = @Content(schema = @Schema(implementation = UserResponse.class))),
            @ApiResponse(responseCode = "404", description = "Usuario no encontrado con el email proporcionado"),
            @ApiResponse(responseCode = "400", description = "Email inválido o malformado")
    })
    @PostMapping("/search/email")
    public ResponseEntity<UserResponse> getUserByEmail(
            @Parameter(description = "Solicitud con el email del usuario a buscar", required = true) @Valid @RequestBody GetUserByEmailRequest getUserByEmailRequest) {

        UserResponse user = getUserByEmailUseCase.execute(getUserByEmailRequest.getEmail());
        return ResponseEntity.ok(user);
    }

    @Operation(summary = "Autenticación de usuario", description = "Autentica a un usuario mediante email y contraseña. Valida las credenciales, "
            +
            "verifica el estado de la cuenta y retorna información de la sesión. " +
            "También actualiza la fecha de último acceso del usuario.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Autenticación exitosa", content = @Content(schema = @Schema(implementation = UserLoginResponse.class))),
            @ApiResponse(responseCode = "401", description = "Credenciales inválidas"),
            @ApiResponse(responseCode = "403", description = "Cuenta suspendida o inactiva"),
            @ApiResponse(responseCode = "400", description = "Datos de login inválidos"),
            @ApiResponse(responseCode = "404", description = "Usuario no encontrado")
    })
    @PostMapping("/login")
    public ResponseEntity<UserLoginResponse> authenticateUser(
            @Parameter(description = "Credenciales de autenticación del usuario", required = true) @Valid @RequestBody UserLoginRequest userLoginRequest) {

        UserLoginResponse loginResponse = userLoginUseCase.getUserLogin(userLoginRequest);
        return ResponseEntity.ok(loginResponse);
    }

    @Operation(summary = "Obtener todos los usuarios incluyendo eliminados", description = "Recupera una lista completa de todos los usuarios del sistema, incluyendo aquellos "
            +
            "que han sido eliminados lógicamente. Esta operación está restringida a administradores " +
            "y es útil para auditorías y reportes administrativos.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista completa de usuarios obtenida exitosamente", content = @Content(schema = @Schema(implementation = UserSummaryResponse.class))),
            @ApiResponse(responseCode = "403", description = "No autorizado para esta operación"),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @GetMapping("/admin/all")
    public ResponseEntity<List<UserSummaryResponse>> getAllUsersIncludingDeleted() {
        List<UserSummaryResponse> users = getUsersUseCase.getAllUsersIncludingDeleted();
        return ResponseEntity.ok(users);
    }
}

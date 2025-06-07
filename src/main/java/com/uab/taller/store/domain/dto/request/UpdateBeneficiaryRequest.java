package com.uab.taller.store.domain.dto.request;

import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UpdateBeneficiaryRequest {

    @NotNull(message = "El ID del beneficiario es obligatorio")
    private Long id;

    @NotNull(message = "El ID del usuario es obligatorio")
    private Long userId;

    @NotNull(message = "El ID de la cuenta es obligatorio")
    private Long accountId;

    @Size(max = 100, message = "El alias no puede exceder 100 caracteres")
    private String alias;

    @Size(max = 255, message = "La descripción no puede exceder 255 caracteres")
    private String description;
}

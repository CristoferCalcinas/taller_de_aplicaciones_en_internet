package com.uab.taller.store.domain.dto.request;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UpdateTransactionRequest {

    @NotNull(message = "El ID de la transacción es obligatorio")
    private Long id;

    @Size(max = 255, message = "La descripción no puede exceder 255 caracteres")
    private String description;

    @Size(max = 100, message = "La referencia no puede exceder 100 caracteres")
    private String reference;

    @Size(max = 500, message = "Las notas no pueden exceder 500 caracteres")
    private String notes;
}

package com.uab.taller.store.domain.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class DeleteResponse {
    private boolean success;
    private String message;
    private Long deletedId;
    private String entityType;

    public static DeleteResponse success(Long id, String entityType) {
        DeleteResponse response = new DeleteResponse();
        response.setSuccess(true);
        response.setMessage(String.format("%s with ID %d deleted successfully", entityType, id));
        response.setDeletedId(id);
        response.setEntityType(entityType);
        return response;
    }

    public static DeleteResponse failure(String message) {
        DeleteResponse response = new DeleteResponse();
        response.setSuccess(false);
        response.setMessage(message);
        response.setDeletedId(null);
        response.setEntityType(null);
        return response;
    }
}

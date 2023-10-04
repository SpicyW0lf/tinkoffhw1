package com.example.fintechspring.DTO;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "Ответ")
public class ResponseDTO {
    @Schema(description = "Сообщение")
    String message;

    public ResponseDTO(String message) {
        this.message = message;
    }
}

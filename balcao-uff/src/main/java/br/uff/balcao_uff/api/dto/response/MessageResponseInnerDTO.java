package br.uff.balcao_uff.api.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
public class MessageResponseInnerDTO {
    private Long id;
    private Long senderId;
    private String senderName;
    private String conteudo;
    private LocalDateTime dataEnvio;
    private boolean isRead;
}

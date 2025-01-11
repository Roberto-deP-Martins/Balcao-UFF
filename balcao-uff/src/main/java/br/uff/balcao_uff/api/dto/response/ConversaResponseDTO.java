package br.uff.balcao_uff.api.dto.response;

import java.time.LocalDateTime;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ConversaResponseDTO {
    private Long id;
    private LocalDateTime dataCriacao;
    private boolean interessadoFecharNegocio;
    private List<MessageResponseInnerDTO> mensagens;
}

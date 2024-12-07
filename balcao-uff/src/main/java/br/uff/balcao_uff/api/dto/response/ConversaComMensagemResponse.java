package br.uff.balcao_uff.api.dto.response;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class ConversaComMensagemResponse {
    private Long conversaId;
    private Long usuarioId;
    private Long outroUsuarioId;
    private String mensagem;
    private LocalDateTime dataHoraMensagem;
}

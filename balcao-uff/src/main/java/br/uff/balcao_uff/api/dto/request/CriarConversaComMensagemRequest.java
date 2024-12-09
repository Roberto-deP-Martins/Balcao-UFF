package br.uff.balcao_uff.api.dto.request;

import lombok.Data;

@Data
public class CriarConversaComMensagemRequest {
    private Long anuncioId;
    private String mensagem;
}

package br.uff.balcao_uff.api.dto.request;

import lombok.Builder;

@Builder
public record TransacaoRequestDTO(
		Long anuncioId, 
		Long anuncianteId, 
		Long interessadoId
		) {

}

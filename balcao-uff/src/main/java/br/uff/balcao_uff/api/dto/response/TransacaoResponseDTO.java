package br.uff.balcao_uff.api.dto.response;

import java.util.Date;

import lombok.Builder;

@Builder
public record TransacaoResponseDTO(
		Long anuncioId, 
		Long anuncianteId, 
		Long interessadoId, 
		Date dtConclusao,
		boolean anuncianteReview,
		boolean interessadoReview
		) {

}

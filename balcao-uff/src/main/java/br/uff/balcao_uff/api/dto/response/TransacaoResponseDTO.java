package br.uff.balcao_uff.api.dto.response;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Builder;

@Builder
public record TransacaoResponseDTO(
        Long anuncioId, 
        Long anuncianteId, 
        Long interessadoId,
        @JsonProperty
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy HH:mm", timezone = "America/Sao_Paulo")
        Date dtConclusao,
        boolean anuncianteReview,
        boolean interessadoReview
) {}

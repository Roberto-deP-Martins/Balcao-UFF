package br.uff.balcao_uff.api.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AnuncioPesquisaAvancadaRequestDTO {

	private String nome;
	private String category;
	private String ordemPreco;
	private String ordemData;
	
}
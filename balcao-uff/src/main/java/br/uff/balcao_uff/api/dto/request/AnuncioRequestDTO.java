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
public class AnuncioRequestDTO {

	private String title;
	private String description;
	private String category;
	private double price;
	private String contactInfo;
	private String location;
	private Long userId;
}

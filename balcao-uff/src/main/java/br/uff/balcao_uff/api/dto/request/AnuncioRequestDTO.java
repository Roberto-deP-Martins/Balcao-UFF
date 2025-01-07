package br.uff.balcao_uff.api.dto.request;

import jakarta.validation.constraints.NotEmpty;
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

	private Long id;

	@NotEmpty(message = "O título não pode ser vazio")
	private String title;

	@NotEmpty(message = "A descrição não pode ser vazia")
	private String description;

	@NotEmpty(message = "A categoria não pode ser vazia")
	private String category;

	private double price;

	private String contactInfo;

	private double latitude;
	private double longitude;
	private String address;

	private Long userId;
}

package br.uff.balcao_uff.api.dto.response;

import java.util.Date;
import java.util.List;

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
public class AnuncioResponseDTO {

	private Long id;
	private String title;
	private String description;
	private String category;
	private double price;
	private String contactInfo;
	private String location;
	private Long userId;
	private Date dtCriacao;
	private List<String> imagePaths;

	public AnuncioResponseDTO(Long id, String title, String description, String category, double price,
			String contactInfo, String location, Long userId, String imagePaths, Date dtCriacao) {
		this.id = id;
		this.title = title;
		this.description = description;
		this.category = category;
		this.price = price;
		this.contactInfo = contactInfo;
		this.location = location;
		this.userId = userId;
		this.dtCriacao = dtCriacao;
		this.imagePaths = List.of(imagePaths.split(","));
		
	};
}

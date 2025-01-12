package br.uff.balcao_uff.api.dto.response;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class AnuncioResponsePerfilDTO {
    private String title;
    private String category;
    private String location;
    private double price;
}

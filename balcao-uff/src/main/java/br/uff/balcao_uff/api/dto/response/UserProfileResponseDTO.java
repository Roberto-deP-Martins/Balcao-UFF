package br.uff.balcao_uff.api.dto.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserProfileResponseDTO {
    private Long id;
    private String name;
    private String email;
    private Double reputation;
}

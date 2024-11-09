package br.uff.balcao_uff.api.dto.response;

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
public class MessageResponseDTO {
	 private Long id;
	    private String content;
	    private boolean isRead;
	    private Long senderId;
	    private Long receiverId;
	    private Long anuncioId;
}

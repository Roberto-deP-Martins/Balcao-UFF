package br.uff.balcao_uff.api.resource.swagger;

import java.util.List;

import org.springframework.http.ResponseEntity;

import br.uff.balcao_uff.api.dto.request.MessageRequestDTO;
import br.uff.balcao_uff.api.dto.response.MessageResponseDTO;
import io.swagger.v3.oas.annotations.Operation;

public interface MessageResourceApi {

	@Operation(summary = "Send a message")
	public ResponseEntity<MessageResponseDTO> sendMessage(MessageRequestDTO messageRequestDTO);

	@Operation(summary = "Get all messages for a user")
	public ResponseEntity<List<MessageResponseDTO>> getMessages(Long userId);

	@Operation(summary = "Get a specific message by ID")
	public ResponseEntity<MessageResponseDTO> getMessage(Long messageId);
}

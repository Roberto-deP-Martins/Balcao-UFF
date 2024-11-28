package br.uff.balcao_uff.api.resource.swagger;

import java.util.List;

import org.springframework.http.ResponseEntity;

import br.uff.balcao_uff.api.dto.request.MessageRequestDTO;
import br.uff.balcao_uff.api.dto.response.MessageResponseDTO;
import io.swagger.v3.oas.annotations.Operation;

public interface MessageResourceApi {

    @Operation(summary = "Create a new message")
    public ResponseEntity<MessageResponseDTO> save(MessageRequestDTO messageRequestDTO);

    @Operation(summary = "Get all messages in a conversation")
    public ResponseEntity<List<MessageResponseDTO>> findByConversaId(Long conversaId);

    @Operation(summary = "Get message by ID")
    public ResponseEntity<MessageResponseDTO> findById(Long id);

    @Operation(summary = "Delete message by ID")
    public ResponseEntity<String> delete(Long id);
}

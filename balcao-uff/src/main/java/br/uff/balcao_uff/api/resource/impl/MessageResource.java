package br.uff.balcao_uff.api.resource.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import br.uff.balcao_uff.api.dto.request.MessageRequestDTO;
import br.uff.balcao_uff.api.dto.response.MessageResponseDTO;
import br.uff.balcao_uff.api.resource.swagger.MessageResourceApi;
import br.uff.balcao_uff.service.MessageService;
import br.uff.balcao_uff.service.ConversaService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/messages")
@CrossOrigin(origins = "*")
@RequiredArgsConstructor
@Tag(name = "Messages", description = "Gerenciamento de Mensagens")
public class MessageResource implements MessageResourceApi {

    private final MessageService messageService;
    private final ConversaService conversaService;

    @PostMapping("/save")
    public ResponseEntity<MessageResponseDTO> save(@RequestBody MessageRequestDTO messageRequestDTO) {
        try {
            // Chama o serviço para criar a mensagem
            MessageResponseDTO savedMessage = messageService.save(messageRequestDTO);
            return ResponseEntity.status(HttpStatus.CREATED).body(savedMessage);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
    }

	@Override
	public ResponseEntity<List<MessageResponseDTO>> findByConversaId(Long conversaId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ResponseEntity<MessageResponseDTO> findById(Long id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ResponseEntity<String> delete(Long id) {
		// TODO Auto-generated method stub
		return null;
	}

}

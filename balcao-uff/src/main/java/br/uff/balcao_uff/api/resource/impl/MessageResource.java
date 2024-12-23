package br.uff.balcao_uff.api.resource.impl;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.uff.balcao_uff.api.dto.request.MessageRequestDTO;
import br.uff.balcao_uff.api.dto.response.MessageResponseDTO;
import br.uff.balcao_uff.api.resource.swagger.MessageResourceApi;
import br.uff.balcao_uff.service.MessageService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/messages")
@CrossOrigin(origins = "*")
@RequiredArgsConstructor
@Tag(name = "Messages", description = "Gerenciamento de Mensagens")
public class MessageResource implements MessageResourceApi {

    private final MessageService messageService;

    /**
     * Chama o serviço para salvar a mensagem na conversa correspondente
     */
    @PostMapping("/sendmessage")
    public ResponseEntity<MessageResponseDTO> sendMessage(@RequestBody MessageRequestDTO messageRequestDTO) {
        MessageResponseDTO savedMessage = messageService.sendMessage(messageRequestDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedMessage);
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

package br.uff.balcao_uff.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.uff.balcao_uff.api.dto.request.MessageRequestDTO;
import br.uff.balcao_uff.api.dto.response.MessageResponseDTO;
import br.uff.balcao_uff.entity.ConversaEntity;
import br.uff.balcao_uff.entity.MessageEntity;
import br.uff.balcao_uff.entity.UserEntity;
import br.uff.balcao_uff.repository.ConversaRepository;
import br.uff.balcao_uff.repository.MessageRepository;
import br.uff.balcao_uff.repository.UserRepository;
import lombok.AllArgsConstructor;

@AllArgsConstructor
@Service
public class MessageService {

    private final MessageRepository messageRepository;
    private final ConversaRepository conversaRepository;
    private final UserRepository userRepository;

    @Transactional
    public MessageEntity criarMensagem(Long conversaId, Long senderId, String conteudo) {
        // Verificar se a conversa existe
        ConversaEntity conversa = conversaRepository.findById(conversaId)
                .orElseThrow(() -> new IllegalArgumentException("Conversa não encontrada com ID: " + conversaId));

        // Verificar se o remetente existe
        UserEntity sender = userRepository.findById(senderId)
                .orElseThrow(() -> new IllegalArgumentException("Remetente não encontrado com ID: " + senderId));

        // Criar a mensagem
        MessageEntity novaMensagem = MessageEntity.builder()
                .content(conteudo)
                .isRead(false) // Mensagem inicialmente marcada como não lida
                .sender(sender)
                .conversa(conversa)
                .build();

        // Salvar a mensagem no banco
        return messageRepository.save(novaMensagem);
    }
    
    public MessageResponseDTO save(MessageRequestDTO messageRequestDTO) {
        // Verifica se a conversa existe
        ConversaEntity conversa = conversaRepository.findById(messageRequestDTO.getConversaId())
                .orElseThrow(() -> new IllegalArgumentException("Conversa não encontrada"));

        // Busca o usuário a partir do ID do sender
        UserEntity sender = userRepository.findById(messageRequestDTO.getSenderId())
                .orElseThrow(() -> new IllegalArgumentException("Usuário não encontrado"));

        // Cria a nova mensagem
        MessageEntity messageEntity = MessageEntity.builder()
                .content(messageRequestDTO.getContent())
                .sender(sender)  // Corrigido para receber o objeto UserEntity
                .conversa(conversa)
                .build();

        messageEntity = messageRepository.save(messageEntity);

        // Retorna a resposta da mensagem criada
        return new MessageResponseDTO(messageEntity);
    }
    
    public List<MessageResponseDTO> findByConversaId(Long conversaId) {
        List<MessageEntity> messages = messageRepository.findByConversaId(conversaId);
        return MessageResponseDTO.fromEntityList(messages);
    }
    
 // Busca a mensagem por ID
    public MessageResponseDTO findById(Long id) {
        MessageEntity message = messageRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Mensagem não encontrada"));
        return new MessageResponseDTO(message);
    }
    
    public void delete(Long id) {
        messageRepository.deleteById(id);
    }


}

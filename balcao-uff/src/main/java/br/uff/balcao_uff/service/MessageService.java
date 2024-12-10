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
    private final AuthorizationService authorizationService;

    /**
     * Cria uma nova mensagem dentro de uma conversa.
     * 
     * Este método cria uma mensagem associada a uma conversa existente, com um remetente específico e conteúdo fornecido.
     * A mensagem é marcada inicialmente como não lida e é salva no banco de dados.
     * 
     * @param conversaId ID da conversa onde a mensagem será inserida.
     * @param senderId ID do usuário que está enviando a mensagem.
     * @param conteudo O conteúdo da mensagem a ser enviada.
     * @return A nova mensagem criada e salva.
     * @throws IllegalArgumentException Caso a conversa ou o remetente não existam.
     */
    @Transactional
    public MessageEntity criarMensagem(Long conversaId, Long senderId, String conteudo) {
        ConversaEntity conversa = conversaRepository.findById(conversaId)
                .orElseThrow(() -> new IllegalArgumentException("Conversa não encontrada com ID: " + conversaId));

        UserEntity sender = userRepository.findById(senderId)
                .orElseThrow(() -> new IllegalArgumentException("Remetente não encontrado com ID: " + senderId));

        MessageEntity novaMensagem = MessageEntity.builder()
                .content(conteudo)
                .isRead(false)
                .sender(sender)
                .conversa(conversa)
                .build();

        return messageRepository.save(novaMensagem);
    }

    /**
     * Retorna uma lista de mensagens associadas a uma conversa.
     * 
     * Este método consulta o banco de dados e recupera todas as mensagens de uma conversa específica,
     * retornando uma lista de DTOs com as informações de cada mensagem.
     * 
     * @param conversaId ID da conversa cujas mensagens serão retornadas.
     * @return Lista de mensagens associadas à conversa.
     */
    public List<MessageResponseDTO> findByConversaId(Long conversaId) {
        List<MessageEntity> messages = messageRepository.findByConversaId(conversaId);
        return MessageResponseDTO.fromEntityList(messages);
    }

    /**
     * Retorna uma mensagem específica pelo seu ID.
     * 
     * Este método consulta o banco de dados para encontrar uma mensagem com o ID fornecido e retorna
     * a representação dessa mensagem no formato DTO.
     * 
     * @param id ID da mensagem que será retornada.
     * @return A mensagem correspondente ao ID fornecido.
     * @throws IllegalArgumentException Caso a mensagem não seja encontrada.
     */
    public MessageResponseDTO findById(Long id) {
        MessageEntity message = messageRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Mensagem não encontrada"));
        return new MessageResponseDTO(message);
    }

    /**
     * Exclui uma mensagem pelo seu ID.
     * 
     * Este método deleta a mensagem correspondente ao ID fornecido do banco de dados.
     * 
     * @param id ID da mensagem a ser deletada.
     */
    public void delete(Long id) {
        messageRepository.deleteById(id);
    }

    /**
     * Envia uma nova mensagem em uma conversa existente.
     * 
     * Este método verifica se a conversa existe e se o usuário autenticado é um participante válido da conversa.
     * Caso o usuário não seja um participante, uma exceção é lançada. Se o usuário for válido, a mensagem é criada e salva.
     * 
     * @param messageRequestDTO DTO contendo os dados necessários para enviar a mensagem.
     * @return A mensagem enviada e salva.
     * @throws IllegalArgumentException Caso a conversa não exista.
     * @throws SecurityException Caso o usuário não seja um participante da conversa.
     */
    public MessageResponseDTO sendMessage(MessageRequestDTO messageRequestDTO) {
        UserEntity currentUser = authorizationService.getAuthenticatedUser();

        ConversaEntity conversa = conversaRepository.findById(messageRequestDTO.getConversaId())
                .orElseThrow(() -> new IllegalArgumentException("Conversa não encontrada"));

        if (!isUserParticipantOfConversa(currentUser, conversa)) {
            throw new SecurityException("Usuário não autorizado a enviar mensagens nesta conversa.");
        }

        MessageEntity messageEntity = MessageEntity.builder()
                .content(messageRequestDTO.getContent())
                .sender(currentUser)
                .conversa(conversa)
                .build();

        messageEntity = messageRepository.save(messageEntity);

        return new MessageResponseDTO(messageEntity);
    }

    /**
     * Verifica se um usuário é participante de uma conversa.
     * 
     * Este método verifica se o usuário informado está participando da conversa, sendo ele o anunciante
     * ou o interessado. A verificação é feita comparando os IDs dos participantes.
     * 
     * @param user O usuário a ser verificado.
     * @param conversa A conversa na qual a verificação será realizada.
     * @return true se o usuário é participante da conversa, false caso contrário.
     */
    private boolean isUserParticipantOfConversa(UserEntity user, ConversaEntity conversa) {
        return conversa.getAnunciante().getId().equals(user.getId()) || 
               conversa.getInteressado().getId().equals(user.getId());
    }
}

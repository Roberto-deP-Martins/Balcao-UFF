package br.uff.balcao_uff.service;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.uff.balcao_uff.api.dto.request.CriarConversaComMensagemRequest;
import br.uff.balcao_uff.api.dto.response.ConversaComMensagemResponse;
import br.uff.balcao_uff.api.dto.response.ConversaResponseDTO;
import br.uff.balcao_uff.api.dto.response.MessageResponseInnerDTO;
import br.uff.balcao_uff.commons.util.exceptions.ConversaNotFoundException;
import br.uff.balcao_uff.commons.util.exceptions.UsuarioNaoInteressadoException;
import br.uff.balcao_uff.commons.util.response.ResponseFormatter;
import br.uff.balcao_uff.entity.AnuncioEntity;
import br.uff.balcao_uff.entity.ConversaEntity;
import br.uff.balcao_uff.entity.MessageEntity;
import br.uff.balcao_uff.entity.UserEntity;
import br.uff.balcao_uff.repository.AnuncioRepository;
import br.uff.balcao_uff.repository.ConversaRepository;
import br.uff.balcao_uff.repository.MessageRepository;
import br.uff.balcao_uff.repository.UserRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ConversaService {

    private final ConversaRepository conversaRepository;
    private final MessageRepository messageRepository;
    private final UserRepository userRepository;
    private final AnuncioRepository anuncioRepository;

    /**
     * Cria uma nova conversa com uma mensagem inicial.
     *
     * @param request Objeto com os dados para criação da conversa e mensagem inicial.
     * @param userId ID do usuário autenticado.
     * @return Resposta com os detalhes da conversa e mensagem criada.
     */
    @Transactional
    public ConversaComMensagemResponse criarConversaComMensagem(CriarConversaComMensagemRequest request, Long userId) {
        boolean conversaExistente = conversaRepository.existsByAnuncioIdAndInteressadoId(request.getAnuncioId(), userId);
        if (conversaExistente) {
            throw new IllegalArgumentException("Já existe uma conversa para este anúncio e interessado.");
        }

        AnuncioEntity anuncio = anuncioRepository.findById(request.getAnuncioId())
                .orElseThrow(() -> new IllegalArgumentException("Anúncio não encontrado."));

        UserEntity anunciante = anuncio.getUser();

        if (anunciante.getId().equals(userId)) {
            throw new IllegalArgumentException("O anunciante não pode iniciar uma conversa com ele mesmo.");
        }

        UserEntity interessado = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("Usuário autenticado não encontrado."));

        ConversaEntity conversa = ConversaEntity.builder()
                .anuncio(anuncio)
                .interessado(interessado)
                .anunciante(anunciante)
                .data_criacao(LocalDateTime.now())
                .build();
        conversa = conversaRepository.save(conversa);

        MessageEntity mensagem = MessageEntity.builder()
                .conversa(conversa)
                .sender(interessado)
                .content(request.getMensagem())
                .dt_Envio(new Date())
                .isRead(false)
                .build();
        mensagem = messageRepository.save(mensagem);

        return ConversaComMensagemResponse.builder()
                .conversaId(conversa.getId())
                .usuarioId(interessado.getId())
                .outroUsuarioId(anunciante.getId())
                .mensagem(mensagem.getContent())
                .dataHoraMensagem(mensagem.getDt_Envio().toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime())
                .build();
    }

    /**
     * Busca conversas relacionadas a um anúncio.
     *
     * @param anuncioId    ID do anúncio.
     * @param usuarioAtual Usuário autenticado.
     * @return Lista de conversas como DTO.
     */
    public List<ConversaResponseDTO> buscarConversasPorAnuncio(Long anuncioId, UserEntity usuarioAtual) {
        List<ConversaEntity> conversas;

        if (usuarioAtual.getId().equals(anuncioRepository.findById(anuncioId).get().getUser().getId())) {
            conversas = conversaRepository.findByAnuncioId(anuncioId);
        } else {
            conversas = conversaRepository.findByAnuncioIdAndAnuncianteIdOrAnuncioIdAndInteressadoId(
                    anuncioId, 
                    usuarioAtual.getId(),
                    anuncioId,
                    usuarioAtual.getId()
            );
        }

        if (conversas.isEmpty()) {
            return new ArrayList<>();
        }

        return conversas.stream()
                .map(this::mapearConversaParaDTO)
                .collect(Collectors.toList());
    }

    /**
     * Mapeia uma entidade de conversa para um DTO.
     *
     * @param conversa A entidade da conversa a ser mapeada.
     * @return O DTO da conversa.
     */
    private ConversaResponseDTO mapearConversaParaDTO(ConversaEntity conversa) {
        List<MessageResponseInnerDTO> mensagens = conversa.getMessages().stream()
                .map(this::mapearMensagemParaDTO)
                .collect(Collectors.toList());

        return new ConversaResponseDTO(
                conversa.getId(),
                conversa.getData_criacao(),
                conversa.isInteressadoFecharNegocio(),
                mensagens
        );
    }

    /**
     * Mapeia uma entidade de mensagem para um DTO.
     *
     * @param mensagem A entidade da mensagem a ser mapeada.
     * @return O DTO da mensagem.
     */
    private MessageResponseInnerDTO mapearMensagemParaDTO(MessageEntity mensagem) {
        LocalDateTime dataEnvio = mensagem.getDt_Envio().toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();

        return MessageResponseInnerDTO.builder()
                .id(mensagem.getId())
                .senderId(mensagem.getSender().getId())
                .senderName(mensagem.getSender().getName())
                .conteudo(mensagem.getContent())
                .dataEnvio(dataEnvio)
                .isRead(mensagem.isRead())
                .build();
    }

    /**
     * Atualiza o campo interessado_fechar_negocio para true.
     *
     * @param conversaId O ID da conversa que será atualizada.
     * @param userId O ID do usuário autenticado.
     * @return Map contendo a resposta formatada.
     */
    public Map<String, Object> atualizarInteressadoFecharNegocio(Long conversaId, Long userId) {
        ConversaEntity conversa = conversaRepository.findById(conversaId)
                .orElseThrow(() -> new ConversaNotFoundException("Conversa não encontrada."));

        if (!conversa.getInteressado().getId().equals(userId)) {
            throw new UsuarioNaoInteressadoException("Usuário não é o interessado desta conversa.");
        }

        conversa.setInteressadoFecharNegocio(true);
        conversaRepository.save(conversa);

        return ResponseFormatter.createSuccessResponse("O campo interessado_fechar_negocio foi atualizado com sucesso.");
    }
}

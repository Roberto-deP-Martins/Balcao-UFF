package br.uff.balcao_uff.api.resource.swagger;

import java.util.List;
import java.util.Map;

import org.springframework.http.ResponseEntity;

import br.uff.balcao_uff.api.dto.request.CriarConversaComMensagemRequest;
import br.uff.balcao_uff.api.dto.response.ConversaComMensagemResponse;
import br.uff.balcao_uff.api.dto.response.ConversaResponseDTO;
import br.uff.balcao_uff.api.resource.impl.ConversaResource;
import io.swagger.v3.oas.annotations.Operation;

/**
 * Interface que define os endpoints de gerenciamento de conversas.
 * A interface é implementada pela classe {@link ConversaResource} para definir os métodos que irão ser expostos na API.
 */
public interface ConversaResourceApi {

    /**
     * Cria uma nova conversa com uma mensagem inicial entre um interessado e o anunciante de um anúncio.
     * 
     * Este método recebe os dados necessários para criar a conversa e a mensagem, validando o usuário autenticado
     * e verificando se ele pode iniciar a conversa. O interessado envia uma mensagem inicial ao iniciar a conversa.
     * 
     * @param request O objeto contendo os dados necessários para criar a conversa e a mensagem.
     * @return ResponseEntity com a resposta da criação da conversa e da mensagem.
     * @throws UnauthorizedException Se o usuário não estiver autenticado.
     */
    @Operation(summary = "Criar conversa com mensagem inicial", description = "Cria uma nova conversa com uma mensagem inicial entre o interessado e o anunciante de um anúncio.")
    ResponseEntity<ConversaComMensagemResponse> criarConversaComMensagem(CriarConversaComMensagemRequest request);

    /**
     * Busca todas as conversas associadas a um anúncio, dependendo do papel do usuário (anunciante ou interessado).
     * 
     * Este método retorna uma lista de conversas associadas a um anúncio. Ele verifica se o usuário é o anunciante
     * ou o interessado e filtra as conversas com base no papel do usuário.
     * 
     * @param anuncioId O ID do anúncio para o qual as conversas devem ser buscadas.
     * @return ResponseEntity com a lista de conversas relacionadas ao anúncio.
     * @throws UnauthorizedException Se o usuário não estiver autenticado.
     */
    @Operation(summary = "Buscar conversas por anúncio", description = "Retorna todas as conversas relacionadas a um anúncio, filtradas pelo papel do usuário (anunciante ou interessado).")
    ResponseEntity<List<ConversaResponseDTO>> buscarConversasPorAnuncio(Long anuncioId);
    
    /**
     * Atualiza o campo interessado_fechar_negocio para true.
     * 
     * Este método permite que o usuário interessado marque a conversa como fechada, indicando que ele deseja
     * fechar o negócio. A atualização é feita apenas se o usuário for o interessado da conversa.
     * 
     * @param conversaId O ID da conversa que será atualizada.
     * @return ResponseEntity com a resposta formatada.
     */
    @Operation(summary = "Atualizar interessado_fechar_negocio", description = "Atualiza o campo interessado_fechar_negocio para true, marcando a conversa como fechada pelo interessado.")
    ResponseEntity<Map<String, Object>> atualizarInteressadoFecharNegocio(Long conversaId);
}

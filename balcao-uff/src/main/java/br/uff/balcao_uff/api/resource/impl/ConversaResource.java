package br.uff.balcao_uff.api.resource.impl;

import java.util.List;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.uff.balcao_uff.api.dto.request.CriarConversaComMensagemRequest;
import br.uff.balcao_uff.api.dto.response.ConversaComMensagemResponse;
import br.uff.balcao_uff.api.dto.response.ConversaResponseDTO;
import br.uff.balcao_uff.api.resource.swagger.ConversaResourceApi;
import br.uff.balcao_uff.commons.util.response.ResponseFormatter;
import br.uff.balcao_uff.service.AuthorizationService;
import br.uff.balcao_uff.service.ConversaService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/conversas")
@CrossOrigin(origins = "*")
@RequiredArgsConstructor
@Tag(name = "Conversas", description = "Gerenciamento de Conversas")
public class ConversaResource implements ConversaResourceApi {

    private final AuthorizationService authorizationService;
    private final ConversaService conversaService;

    /**
     * Endpoint para criar uma conversa com uma mensagem inicial.
     * 
     * Este método cria uma nova conversa entre um interessado e o anunciante de um anúncio, 
     * com uma mensagem inicial enviada pelo interessado. O usuário precisa estar autenticado 
     * para acessar este endpoint.
     * 
     * @param request O objeto contendo os dados necessários para criar a conversa e a mensagem.
     * @return ResponseEntity com a resposta da criação da conversa e da mensagem.
     * @throws UnauthorizedException Se o usuário não estiver autenticado.
     */
    @PostMapping("/iniciar-conversa")
    public ResponseEntity<ConversaComMensagemResponse> criarConversaComMensagem(
            @RequestBody CriarConversaComMensagemRequest request) {
        
        var usuarioAtual = authorizationService.getAuthenticatedUser();
        
        if (usuarioAtual == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        }

        ConversaComMensagemResponse response = conversaService.criarConversaComMensagem(request, usuarioAtual.getId());

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
    
    /**
     * Endpoint para buscar as conversas de um anúncio, filtrando pelo papel do usuário (anunciante ou interessado).
     * 
     * Este método retorna uma lista de conversas associadas a um anúncio, dependendo do papel do usuário 
     * (se é o anunciante ou o interessado). O usuário precisa estar autenticado para acessar este endpoint.
     * 
     * @param anuncioId O ID do anúncio para o qual as conversas devem ser buscadas.
     * @return ResponseEntity com a lista de conversas relacionadas ao anúncio.
     */
    @GetMapping("/por-anuncio/{anuncioId}")
    public ResponseEntity<List<ConversaResponseDTO>> buscarConversasPorAnuncio(
            @PathVariable Long anuncioId) {

        var usuarioAtual = authorizationService.getAuthenticatedUser();

        if (usuarioAtual == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        }

        List<ConversaResponseDTO> conversas = conversaService.buscarConversasPorAnuncio(anuncioId, usuarioAtual);

        return ResponseEntity.status(HttpStatus.OK).body(conversas);
    }
    
    /**
     * Endpoint para atualizar o campo interessado_fechar_negocio para true.
     *
     * @param conversaId O ID da conversa que será atualizada.
     * @return ResponseEntity contendo a resposta formatada.
     */
    @PutMapping("/fechar-negocio/{conversaId}")
    public ResponseEntity<Map<String, Object>> atualizarInteressadoFecharNegocio(@PathVariable Long conversaId) {
        var usuarioAtual = authorizationService.getAuthenticatedUser();

        if (usuarioAtual == null) {
            return ResponseEntity.status(401).body(ResponseFormatter.createErrorResponse("Usuário não autenticado."));
        }

        Map<String, Object> response = conversaService.atualizarInteressadoFecharNegocio(conversaId, usuarioAtual.getId());
        return ResponseEntity.ok(response);
    }

}

package br.uff.balcao_uff.api.resource.swagger;

import br.uff.balcao_uff.api.dto.request.UserReviewRequestDTO;
import br.uff.balcao_uff.api.dto.response.UserReviewResponseDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

public interface UserReviewResourceApi {

    @Operation(
            summary = "Adicionar avaliação de um usuário",
            description = "Permite que um usuário avalie outro usuário com base em uma transação concluída.",
            responses = {
                    @ApiResponse(responseCode = "201", description = "Avaliação criada com sucesso"),
                    @ApiResponse(responseCode = "400", description = "Dados inválidos na solicitação")
            }
    )
    @PostMapping("/reviews")
    ResponseEntity<Void> addReview(
            @RequestBody(description = "Dados da avaliação a ser criada") UserReviewRequestDTO reviewRequestDTO
    );

    @Operation(
            summary = "Listar avaliações de um usuário",
            description = "Retorna todas as avaliações feitas para um determinado usuário.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Avaliações retornadas com sucesso"),
                    @ApiResponse(responseCode = "404", description = "Usuário não encontrado")
            }
    )
    @GetMapping("/reviews/user/{userId}")
    ResponseEntity<List<UserReviewResponseDTO>> getReviewsByUser(
            @PathVariable Long userId
    );

    @Operation(
            summary = "Obter reputação de um usuário",
            description = "Calcula e retorna a reputação média de um usuário com base nas avaliações recebidas.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Reputação retornada com sucesso"),
                    @ApiResponse(responseCode = "404", description = "Usuário não encontrado")
            }
    )
    @GetMapping("/reviews/user/{userId}/reputation")
    ResponseEntity<Double> getUserReputation(
            @PathVariable Long userId
    );
}

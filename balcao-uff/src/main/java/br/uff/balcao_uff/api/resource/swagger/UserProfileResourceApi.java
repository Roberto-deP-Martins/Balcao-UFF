package br.uff.balcao_uff.api.resource.swagger;

import br.uff.balcao_uff.api.dto.response.UserProfileResponseDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;

public interface UserProfileResourceApi {

    @Operation(
            summary = "Obter perfil do usuário autenticado",
            description = "Retorna as informações de perfil do usuário que está logado.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Perfil do usuário retornado com sucesso"),
                    @ApiResponse(responseCode = "401", description = "Usuário não autenticado")
            }
    )
    @GetMapping("/users/profile")
    ResponseEntity<UserProfileResponseDTO> getAuthenticatedUserProfile();
}

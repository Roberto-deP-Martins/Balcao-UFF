package br.uff.balcao_uff.api.resource.swagger;

import br.uff.balcao_uff.api.dto.response.UserProfileResponseDTO;
import br.uff.balcao_uff.api.dto.response.UserResponseDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

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

    @Operation(
            summary = "Obter todos os usuários cadastrados",
            description = "Retorna lista de usuários cadastrados que podem criar anúncios",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Usuários retornado com sucesso"),
                    @ApiResponse(responseCode = "404", description = "Usuários não encontrados")
            }
    )
    @GetMapping("/users")
    public ResponseEntity<List<UserResponseDTO>> getALl();

    @Operation(
            summary = "Obter usuário",
            description = "Retorna usuário cadastrados que podem criar anúncios",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Usuário retornado com sucesso"),
                    @ApiResponse(responseCode = "404", description = "Usuário não encontrado")
            }
    )
    @GetMapping("/users/{userId}")
    public ResponseEntity<UserResponseDTO> getById(@PathVariable Long userId);
}

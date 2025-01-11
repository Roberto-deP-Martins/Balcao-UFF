package br.uff.balcao_uff.api.resource.swagger;

import java.util.List;

import org.springframework.http.ResponseEntity;

import br.uff.balcao_uff.api.dto.request.TransacaoRequestDTO;
import br.uff.balcao_uff.api.dto.response.TransacaoResponseDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;

public interface TransacaoResourceApi {

    @Operation(summary = "Get all transactions")
    ResponseEntity<List<TransacaoResponseDTO>> findAll();

    @Operation(
        summary = "Create new transaction",
        responses = {
            @ApiResponse(responseCode = "201", description = "Transação criada com sucesso"),
            @ApiResponse(responseCode = "400", description = "Dados inválidos"),
            @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
        }
    )
    ResponseEntity<TransacaoResponseDTO> create(TransacaoRequestDTO dto);

    @Operation(
        summary = "Find transaction by user ID",
        responses = {
            @ApiResponse(responseCode = "200", description = "Transação encontrada"),
            @ApiResponse(responseCode = "404", description = "Usuário não encontrado"),
            @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
        }
    )
    ResponseEntity<List<TransacaoResponseDTO>> findByUserId(Long userId);
}

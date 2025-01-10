package br.uff.balcao_uff.api.resource.swagger;

import java.util.List;

import org.springframework.http.ResponseEntity;

import br.uff.balcao_uff.api.dto.response.TransacaoResponseDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;

public interface TransacaoResourceApi {

	
	@Operation(summary = "Get all transactions")
    public ResponseEntity<List<TransacaoResponseDTO>> findAll();
	
	@Operation(summary = "Create new transaction")
	public ResponseEntity<ApiResponse> create();
}

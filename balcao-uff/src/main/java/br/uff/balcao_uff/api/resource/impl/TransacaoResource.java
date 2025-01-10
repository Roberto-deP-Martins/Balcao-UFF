package br.uff.balcao_uff.api.resource.impl;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.uff.balcao_uff.api.dto.response.TransacaoResponseDTO;
import br.uff.balcao_uff.api.resource.swagger.TransacaoResourceApi;
import br.uff.balcao_uff.service.TransacaoService;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/transactions")
@CrossOrigin(origins = "*")
@RequiredArgsConstructor
@Tag(name = "Transaction", description = "Gerenciamento de Transações")
public class TransacaoResource implements TransacaoResourceApi {

	private final TransacaoService service;

	@GetMapping
	public ResponseEntity<List<TransacaoResponseDTO>> findAll() {
		return ResponseEntity.ok(service.getAll());
	}

	@PostMapping
	public ResponseEntity<ApiResponse> create() {
		// TODO Auto-generated method stub
		return null;
	}

}

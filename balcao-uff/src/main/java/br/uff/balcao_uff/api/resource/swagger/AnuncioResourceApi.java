package br.uff.balcao_uff.api.resource.swagger;

import java.util.List;

import org.springframework.http.ResponseEntity;

import br.uff.balcao_uff.api.dto.request.AnuncioCategoryRequestDTO;
import br.uff.balcao_uff.api.dto.request.AnuncioDeleteRequestDTO;
import br.uff.balcao_uff.api.dto.request.AnuncioRequestDTO;
import br.uff.balcao_uff.api.dto.response.AnuncioResponseDTO;
import io.swagger.v3.oas.annotations.Operation;

public interface AnuncioResourceApi {
	
	@Operation(summary = "Create a new advertisement")
	public ResponseEntity<AnuncioResponseDTO> save(AnuncioRequestDTO anuncioRequestDTO);
	
	@Operation(summary = "Update advertisement")
	public ResponseEntity<String> update(AnuncioRequestDTO anuncioRequestDTO);

	@Operation(summary = "Get all advertisements")
	public ResponseEntity<List<AnuncioResponseDTO>> findAll();

	@Operation(summary = "Get advertisement by ID")
	public ResponseEntity<AnuncioResponseDTO> findById(Long id);

	@Operation(summary = "Delete advertisement")
	public ResponseEntity<String> delete(AnuncioDeleteRequestDTO id);

	@Operation(summary = "Search advertisements by category")
	public ResponseEntity<List<AnuncioResponseDTO>> findByCategory(AnuncioCategoryRequestDTO category);
}

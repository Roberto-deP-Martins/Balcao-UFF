package br.uff.balcao_uff.api.resource.impl;

import br.uff.balcao_uff.api.dto.request.AnuncioCategoryRequestDTO;
import br.uff.balcao_uff.api.dto.request.AnuncioDeleteRequestDTO;
import br.uff.balcao_uff.api.dto.request.AnuncioPesquisaAvancadaRequestDTO;
import br.uff.balcao_uff.api.dto.request.AnuncioRequestDTO;
import br.uff.balcao_uff.api.dto.response.AnuncioResponseDTO;
import br.uff.balcao_uff.api.dto.response.AnuncioResponsePerfilDTO;
import br.uff.balcao_uff.api.resource.swagger.AnuncioResourceApi;
import br.uff.balcao_uff.configuration.security.TokenService;
import br.uff.balcao_uff.service.AnuncioService;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/anuncios")
@CrossOrigin(origins = "*")
@RequiredArgsConstructor
@Tag(name = "Anuncios", description = "Gerenciamento de Anúncios")
public class AnuncioResource implements AnuncioResourceApi {

	private final AnuncioService service;

	@Autowired
	TokenService tokenService;

	@PostMapping(value = "/save2", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	public ResponseEntity<AnuncioResponseDTO> save(@RequestPart("anuncio") String anuncioJson,
			@RequestPart(value = "images", required = false) List<MultipartFile> images) {
		try {
			ObjectMapper objectMapper = new ObjectMapper();
			AnuncioRequestDTO anuncioRequestDTO = objectMapper.readValue(anuncioJson, AnuncioRequestDTO.class);

			if (images != null && images.size() > 3) {
				return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
			}

			AnuncioResponseDTO savedAnuncio = service.saveWithImages(anuncioRequestDTO, images);
			return ResponseEntity.status(HttpStatus.CREATED).body(savedAnuncio);
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
		}
	}

	@PostMapping("/update")
	public ResponseEntity<String> update(@RequestBody AnuncioRequestDTO anuncioRequestDTO) {
		try {
			service.update(anuncioRequestDTO);
			return ResponseEntity.ok("Anúncio atualizado com sucesso!");
		} catch (RuntimeException e) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Erro ao atualizar anúncio: " + e.getMessage());
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erro interno ao atualizar anúncio.");
		}
	}

	@GetMapping
	public ResponseEntity<List<AnuncioResponseDTO>> findAll() {
		List<AnuncioResponseDTO> anuncios = service.findAll();
		return ResponseEntity.ok(anuncios);
	}

	@Override
	public ResponseEntity<AnuncioResponseDTO> findById(Long id) {

		return null;
	}

	@PostMapping("/delete")
	public ResponseEntity<String> delete(@RequestBody AnuncioDeleteRequestDTO id) {
	    service.deleteById(id.id());
	    return ResponseEntity.ok("Anúncio deletado com sucesso!");
	}


	@PostMapping("/category")
	public ResponseEntity<List<AnuncioResponseDTO>> findByCategory(
			@RequestBody AnuncioCategoryRequestDTO categoryRequest) {

		List<AnuncioResponseDTO> anuncios = service.findByCategory(categoryRequest.category());
		return ResponseEntity.ok(anuncios);
	}

	@PostMapping("/busca-avancada")
	public ResponseEntity<List<AnuncioResponseDTO>> buscaAvancada(
			@RequestParam AnuncioPesquisaAvancadaRequestDTO request) {
		List<AnuncioResponseDTO> anuncioDto = new ArrayList<>();
		return ResponseEntity.ok(anuncioDto);
	}


	/**
	 * Busca anúncios por localização dentro de um raio específico.
	 * @param lat a latitude do ponto de referência. 
	 * @param lng a longitude do ponto de referência.
	 * @param radius o raio de busca em quilômetros.
	 * @return uma lista de DTOs de resposta contendo os anúncios encontrados.
	 */
	@GetMapping("/nearby")
	public ResponseEntity<List<AnuncioResponseDTO>> findNearby(@RequestParam double lat, @RequestParam double lng,
			@RequestParam double radius) {
		List<AnuncioResponseDTO> anuncios = service.findNearby(lat, lng, radius);
		return ResponseEntity.ok(anuncios);
	}

	@GetMapping("/perfil")
	public ResponseEntity<List<AnuncioResponsePerfilDTO>> getAnunciosByUserId(@RequestParam Long userId) {
		List<AnuncioResponsePerfilDTO> anuncios = service.findAnunciosByUserId(userId);
		return ResponseEntity.ok(anuncios);
	}

}

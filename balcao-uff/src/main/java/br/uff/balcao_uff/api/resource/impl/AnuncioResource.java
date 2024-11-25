package br.uff.balcao_uff.api.resource.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.databind.ObjectMapper;

import br.uff.balcao_uff.api.dto.request.AnuncioCategoryRequestDTO;
import br.uff.balcao_uff.api.dto.request.AnuncioDeleteRequestDTO;
import br.uff.balcao_uff.api.dto.request.AnuncioRequestDTO;
import br.uff.balcao_uff.api.dto.response.AnuncioResponseDTO;
import br.uff.balcao_uff.api.resource.swagger.AnuncioResourceApi;
import br.uff.balcao_uff.configuration.security.TokenService;
import br.uff.balcao_uff.service.AnuncioService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/anuncios")
@CrossOrigin(origins = "*")
@RequiredArgsConstructor
@Tag(name = "Anuncios", description = "Gerenciamento de Anúncios")
public class AnuncioResource implements AnuncioResourceApi {

	private final AnuncioService service;

	@Autowired
	TokenService tokenService;

	@PostMapping("/save")
	public ResponseEntity<AnuncioResponseDTO> save(@RequestBody AnuncioRequestDTO anuncioRequestDTO) {
		AnuncioResponseDTO savedAnuncio = service.save(anuncioRequestDTO);
		return ResponseEntity.status(HttpStatus.CREATED).body(savedAnuncio);
	}

	@PostMapping(value = "/save2", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	public ResponseEntity<AnuncioResponseDTO> save2(
	        @RequestPart("anuncio") String anuncioJson, // JSON como String
	        @RequestPart(value = "images", required = false) List<MultipartFile> images) {
	    try {
	        // Converte o JSON recebido para um objeto Java
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

		try {
			service.deleteById(id.id());
			return ResponseEntity.ok("Anúncio deletado com sucesso!");
		} catch (RuntimeException e) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Erro ao deletar anúncio: " + e.getMessage());
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erro interno ao deletar anúncio.");
		}

	}

	@PostMapping("/category")
	public ResponseEntity<List<AnuncioResponseDTO>> findByCategory(
			@RequestBody AnuncioCategoryRequestDTO categoryRequest) {

		List<AnuncioResponseDTO> anuncios = service.findByCategory(categoryRequest.category());
		return ResponseEntity.ok(anuncios);
	}
}

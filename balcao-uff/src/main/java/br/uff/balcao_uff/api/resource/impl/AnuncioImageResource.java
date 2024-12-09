package br.uff.balcao_uff.api.resource.impl;

import java.nio.file.Path;
import java.nio.file.Paths;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import br.uff.balcao_uff.entity.AnuncioImageEntity;
import br.uff.balcao_uff.service.AnuncioImageService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/anuncioImages")
@CrossOrigin(origins = "*")
@RequiredArgsConstructor
public class AnuncioImageResource {

	@Autowired
	private AnuncioImageService service;

	@Operation(summary = "Salvar uma imagem de anúncio com ID do anúncio")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "201", description = "Imagem criada com sucesso", content = @Content(schema = @Schema(implementation = AnuncioImageEntity.class))),
			@ApiResponse(responseCode = "400", description = "Requisição inválida", content = @Content),
			@ApiResponse(responseCode = "500", description = "Erro interno no servidor", content = @Content) })
	@PostMapping
	public ResponseEntity<AnuncioImageEntity> save(
			@Parameter(description = "ID do Anúncio", required = true) @RequestParam("anuncioId") Long anuncioId,
			@Parameter(description = "Caminho da Imagem", required = true) @RequestParam("imagePath") String imagePath,
			@Parameter(description = "Arquivo da Imagem", required = true, content = @Content(mediaType = "multipart/form-data")) @RequestParam("file") MultipartFile arquivo) {

		AnuncioImageEntity anuncioImageEntity = new AnuncioImageEntity();
		anuncioImageEntity.setImagePath(imagePath);

		return ResponseEntity.status(HttpStatus.CREATED).body(service.save(anuncioId, anuncioImageEntity, arquivo));
	}

	@Value("${app.caminhoImagem}")
	private String caminhoImagem;

	@GetMapping("/image/{filename:.+}")
	public ResponseEntity<Resource> showImage(@PathVariable String filename) {
		try {
			Path file = Paths.get(caminhoImagem).resolve(filename);
			Resource resource = new UrlResource(file.toUri());
			String contentType = "application/octet-stream";
			if (filename.endsWith(".png")) {
				contentType = MediaType.IMAGE_PNG_VALUE;
			} else if (filename.endsWith(".jpg") || filename.endsWith(".jpeg")) {
				contentType = MediaType.IMAGE_JPEG_VALUE;
			}
			return ResponseEntity.ok().contentLength(resource.contentLength())
					.contentType(MediaType.parseMediaType(contentType))
					.body(new InputStreamResource(resource.getInputStream()));
		} catch (Exception e) {
			return ResponseEntity.badRequest().body(null);
		}
	}
}

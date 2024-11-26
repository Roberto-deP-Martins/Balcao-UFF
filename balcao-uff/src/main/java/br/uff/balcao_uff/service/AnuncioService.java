package br.uff.balcao_uff.service;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import br.uff.balcao_uff.api.dto.request.AnuncioPesquisaAvancadaRequestDTO;
import br.uff.balcao_uff.api.dto.request.AnuncioRequestDTO;
import br.uff.balcao_uff.api.dto.response.AnuncioResponseDTO;
import br.uff.balcao_uff.entity.AnuncioEntity;
import br.uff.balcao_uff.entity.AnuncioImageEntity;
import br.uff.balcao_uff.entity.UserEntity;
import br.uff.balcao_uff.repository.AnuncioImageRepository;
import br.uff.balcao_uff.repository.AnuncioRepository;
import br.uff.balcao_uff.repository.UserRepository;
import jakarta.persistence.Query;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class AnuncioService {

	private final AnuncioRepository anuncioRepository;
	private final AnuncioImageRepository anuncioImageRepository;
	private final UserRepository userRepository;

	@Value("${app.caminhoImagem}")
	private String caminhoImagem;

	@Transactional
	public AnuncioResponseDTO save(AnuncioRequestDTO anuncioRequestDTO) {
		UserEntity user = userRepository.findById(anuncioRequestDTO.getUserId())
				.orElseThrow(() -> new RuntimeException("Usuário não encontrado"));

		AnuncioEntity anuncioEntity = AnuncioEntity.builder().title(anuncioRequestDTO.getTitle())
				.description(anuncioRequestDTO.getDescription()).category(anuncioRequestDTO.getCategory())
				.price(anuncioRequestDTO.getPrice()).contactInfo(anuncioRequestDTO.getContactInfo())
				.location(anuncioRequestDTO.getLocation()).user(user).build();

		AnuncioEntity savedAnuncio = anuncioRepository.save(anuncioEntity);

		return convertToDto(savedAnuncio);
	}

	@Transactional
	public void update(AnuncioRequestDTO anuncioRequestDTO) {
		AnuncioEntity existingAnuncio = anuncioRepository.findById(anuncioRequestDTO.getId())
				.orElseThrow(() -> new RuntimeException("Anúncio não encontrado"));

		if (anuncioRequestDTO.getTitle() != null)
			existingAnuncio.setTitle(anuncioRequestDTO.getTitle());
		if (anuncioRequestDTO.getDescription() != null)
			existingAnuncio.setDescription(anuncioRequestDTO.getDescription());
		if (anuncioRequestDTO.getCategory() != null)
			existingAnuncio.setCategory(anuncioRequestDTO.getCategory());
		if (anuncioRequestDTO.getPrice() != 0)
			existingAnuncio.setPrice(anuncioRequestDTO.getPrice());
		if (anuncioRequestDTO.getContactInfo() != null)
			existingAnuncio.setContactInfo(anuncioRequestDTO.getContactInfo());
		if (anuncioRequestDTO.getLocation() != null)
			existingAnuncio.setLocation(anuncioRequestDTO.getLocation());

		anuncioRepository.save(existingAnuncio);
	}

	public List<AnuncioResponseDTO> findAll() {
		List<AnuncioEntity> anuncios = anuncioRepository.findAllByOrderByIdDesc();
		return anuncios.stream().map(this::convertToDto).collect(Collectors.toList());
	}

	public List<AnuncioResponseDTO> findByCategory(String category) {

		List<AnuncioEntity> anuncios = anuncioRepository.findByCategory(category);
		return anuncios.stream().map(this::convertToDto).collect(Collectors.toList());
	}

	@Transactional
	public void deleteById(Long id) {
		AnuncioEntity existingAnuncio = anuncioRepository.findById(id)
				.orElseThrow(() -> new RuntimeException("Anúncio não encontrado"));
		anuncioRepository.delete(existingAnuncio);
	}

	/**
	 * Método auxiliar para converter a entidade para o DTO de resposta
	 * 
	 * @param anuncioEntity
	 * @return
	 */
	private AnuncioResponseDTO convertToDto(AnuncioEntity anuncioEntity) {
		List<String> imagePaths = anuncioEntity.getImages() != null
				? anuncioEntity.getImages().stream().map(AnuncioImageEntity::getImagePath).collect(Collectors.toList())
				: Collections.emptyList();

		return AnuncioResponseDTO.builder()
				.id(anuncioEntity.getId())
				.title(anuncioEntity.getTitle())
				.description(anuncioEntity.getDescription())
				.category(anuncioEntity.getCategory())
				.price(anuncioEntity.getPrice()).contactInfo(anuncioEntity.getContactInfo())
				.location(anuncioEntity.getLocation()).userId(anuncioEntity.getUser().getId())
				.imagePaths(imagePaths)
				.dtCriacao(anuncioEntity.getDtCriacao())
				.build();
	}

	@Transactional
	public AnuncioResponseDTO saveWithImages(AnuncioRequestDTO anuncioRequestDTO, List<MultipartFile> images) {
		UserEntity user = userRepository.findById(anuncioRequestDTO.getUserId())
				.orElseThrow(() -> new RuntimeException("Usuário não encontrado"));

		// Criação do anúncio
		AnuncioEntity anuncioEntity = AnuncioEntity.builder().title(anuncioRequestDTO.getTitle())
				.description(anuncioRequestDTO.getDescription()).category(anuncioRequestDTO.getCategory())
				.price(anuncioRequestDTO.getPrice()).contactInfo(anuncioRequestDTO.getContactInfo())
				.location(anuncioRequestDTO.getLocation()).user(user).build();

		// Salvando o anúncio no banco
		AnuncioEntity savedAnuncio = anuncioRepository.save(anuncioEntity);

		// Salvando as imagens associadas
		if (images != null) {
			images.forEach(image -> {
				try {
					// Gera um nome único e salva a imagem
					String uniqueID = UUID.randomUUID().toString();
					Path caminho = Paths.get(caminhoImagem + uniqueID + "_" + image.getOriginalFilename());
					Files.write(caminho, image.getBytes());

					// Cria a entidade AnuncioImageEntity
					AnuncioImageEntity imageEntity = AnuncioImageEntity.builder().imagePath(caminho.toString())
							.anuncio(savedAnuncio).build();

					// Salva a imagem no repositório
					anuncioImageRepository.save(imageEntity);
				} catch (Exception e) {
					throw new RuntimeException("Erro ao salvar imagem: " + e.getMessage());
				}
			});
		}

		// Recarrega o anúncio com as imagens associadas
		AnuncioEntity anuncioWithImages = anuncioRepository.findById(savedAnuncio.getId())
				.orElseThrow(() -> new RuntimeException("Erro ao carregar o anúncio salvo"));

		// Converte para DTO e retorna
		return convertToDto(anuncioWithImages);
	}

	@SuppressWarnings("unchecked")
	public List<AnuncioResponseDTO> searchAnunciosByAdvancedCriteria(AnuncioPesquisaAvancadaRequestDTO request) {
		Query query = buildQueryAnunciosWithCriteria(request);
		return (List<AnuncioResponseDTO>) query.getResultList();
	}

	private Query buildQueryAnunciosWithCriteria(AnuncioPesquisaAvancadaRequestDTO request) {
		return null;
	}

}
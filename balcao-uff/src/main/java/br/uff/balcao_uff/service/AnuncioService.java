package br.uff.balcao_uff.service;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.List;
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

/**
 * Serviço responsável por gerenciar operações relacionadas aos anúncios,
 * como criação, atualização, exclusão e busca avançada. Também gerencia
 * imagens associadas aos anúncios.
 */
@RequiredArgsConstructor
@Service
public class AnuncioService {

    private final AnuncioRepository anuncioRepository;
    private final AnuncioImageRepository anuncioImageRepository;
    private final UserRepository userRepository;
    private final AuthorizationService authorizationService;

    @Value("${app.caminhoImagem}")
    private String caminhoImagem;

    /**
     * Salva um novo anúncio sem imagens associadas.
     *
     * @param anuncioRequestDTO os dados do anúncio fornecidos pelo cliente.
     * @return o anúncio salvo convertido para um DTO de resposta.
     * @throws RuntimeException se o usuário associado ao anúncio não for encontrado.
     */
    @Transactional
    public AnuncioResponseDTO save(AnuncioRequestDTO anuncioRequestDTO) {
        UserEntity user = userRepository.findById(anuncioRequestDTO.getUserId())
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));

        AnuncioEntity anuncioEntity = AnuncioEntity.builder()
                .title(anuncioRequestDTO.getTitle())
                .description(anuncioRequestDTO.getDescription())
                .category(anuncioRequestDTO.getCategory())
                .price(anuncioRequestDTO.getPrice())
                .contactInfo(anuncioRequestDTO.getContactInfo())
                .location(anuncioRequestDTO.getLocation())
                .user(user)
                .build();

        AnuncioEntity savedAnuncio = anuncioRepository.save(anuncioEntity);

        return convertToDto(savedAnuncio);
    }

    /**
     * Atualiza os dados de um anúncio existente.
     *
     * @param anuncioRequestDTO os novos dados do anúncio.
     * @throws RuntimeException se o anúncio não for encontrado.
     */
    @Transactional
    public void update(AnuncioRequestDTO anuncioRequestDTO) {
        AnuncioEntity existingAnuncio = anuncioRepository.findById(anuncioRequestDTO.getId())
                .orElseThrow(() -> new RuntimeException("Anúncio não encontrado"));

        if (anuncioRequestDTO.getTitle() != null) {
            existingAnuncio.setTitle(anuncioRequestDTO.getTitle());
        }
        if (anuncioRequestDTO.getDescription() != null) {
            existingAnuncio.setDescription(anuncioRequestDTO.getDescription());
        }
        if (anuncioRequestDTO.getCategory() != null) {
            existingAnuncio.setCategory(anuncioRequestDTO.getCategory());
        }
        if (anuncioRequestDTO.getPrice() != 0) {
            existingAnuncio.setPrice(anuncioRequestDTO.getPrice());
        }
        if (anuncioRequestDTO.getContactInfo() != null) {
            existingAnuncio.setContactInfo(anuncioRequestDTO.getContactInfo());
        }
        if (anuncioRequestDTO.getLocation() != null) {
            existingAnuncio.setLocation(anuncioRequestDTO.getLocation());
        }

        anuncioRepository.save(existingAnuncio);
    }

    /**
     * Recupera todos os anúncios ordenados por ID em ordem decrescente.
     *
     * @return uma lista de DTOs de resposta contendo os anúncios encontrados.
     */
    public List<AnuncioResponseDTO> findAll() {
        List<AnuncioEntity> anuncios = anuncioRepository.findAllByOrderByIdDesc();
        return anuncios.stream().map(this::convertToDto).collect(Collectors.toList());
    }

    /**
     * Busca anúncios por categoria.
     *
     * @param category a categoria dos anúncios desejados.
     * @return uma lista de DTOs de resposta contendo os anúncios encontrados.
     */
    public List<AnuncioResponseDTO> findByCategory(String category) {
        List<AnuncioEntity> anuncios = anuncioRepository.findByCategory(category);
        return anuncios.stream().map(this::convertToDto).collect(Collectors.toList());
    }

    /**
     * Exclui um anúncio pelo ID.
     *
     * @param id o identificador do anúncio a ser excluído.
     * @throws RuntimeException se o anúncio não for encontrado.
     */
    @Transactional
    public void deleteById(Long id) {
        AnuncioEntity existingAnuncio = anuncioRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Anúncio não encontrado"));
        anuncioRepository.delete(existingAnuncio);
    }

    /**
     * Converte uma entidade de anúncio em um DTO de resposta.
     *
     * @param anuncioEntity a entidade do anúncio.
     * @return o DTO de resposta correspondente.
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
                .price(anuncioEntity.getPrice())
                .contactInfo(anuncioEntity.getContactInfo())
                .location(anuncioEntity.getLocation())
                .userId(anuncioEntity.getUser().getId())
                .imagePaths(imagePaths)
                .dtCriacao(anuncioEntity.getDtCriacao())
                .build();
    }

    /**
     * Realiza uma busca de anúncios com base em critérios avançados fornecidos.
     *
     * @param request os critérios avançados para busca.
     * @return uma lista de DTOs de resposta contendo os anúncios encontrados.
     */
    @SuppressWarnings("unchecked")
    public List<AnuncioResponseDTO> searchAnunciosByAdvancedCriteria(AnuncioPesquisaAvancadaRequestDTO request) {
        Query query = buildQueryAnunciosWithCriteria(request);
        return (List<AnuncioResponseDTO>) query.getResultList();
    }

    /**
     * Constrói uma consulta com base em critérios avançados para busca de anúncios.
     *
     * @param request os critérios avançados para a consulta.
     * @return a consulta construída.
     */
    private Query buildQueryAnunciosWithCriteria(AnuncioPesquisaAvancadaRequestDTO request) {
        return null;
    }

    /**
     * Salva um novo anúncio com imagens associadas.
     *
     * @param anuncioRequestDTO os dados do anúncio.
     * @param images a lista de imagens a serem associadas ao anúncio.
     * @return o anúncio salvo convertido para um DTO de resposta.
     * @throws RuntimeException se ocorrer erro durante o salvamento das imagens.
     */
    @Transactional
    public AnuncioResponseDTO saveWithImages(AnuncioRequestDTO anuncioRequestDTO, List<MultipartFile> images) {
        UserEntity user = authorizationService.getAuthenticatedUser();
        if (user == null) {
            throw new RuntimeException("Usuário não autenticado");
        }

        AnuncioEntity anuncioEntity = AnuncioEntity.builder()
                .title(anuncioRequestDTO.getTitle())
                .description(anuncioRequestDTO.getDescription())
                .category(anuncioRequestDTO.getCategory())
                .price(anuncioRequestDTO.getPrice())
                .contactInfo(anuncioRequestDTO.getContactInfo())
                .location(anuncioRequestDTO.getLocation())
                .user(user)
                .build();
        AnuncioEntity savedAnuncio = anuncioRepository.save(anuncioEntity);

        if (images != null) {
            images.forEach(image -> {
                try {
                    String uniqueID = UUID.randomUUID().toString();
                    Path caminho = Paths.get(caminhoImagem + uniqueID + "_" + image.getOriginalFilename());
                    Files.write(caminho, image.getBytes());
                    AnuncioImageEntity imageEntity = AnuncioImageEntity.builder()
                            .imagePath(caminho.toString())
                            .anuncio(savedAnuncio)
                            .build();
                    anuncioImageRepository.save(imageEntity);
                } catch (Exception e) {
                    throw new RuntimeException("Erro ao salvar imagem: " + e.getMessage());
                }
            });
        }

        AnuncioEntity anuncioWithImages = anuncioRepository.findById(savedAnuncio.getId())
                .orElseThrow(() -> new RuntimeException("Erro ao carregar o anúncio salvo"));

        return convertToDto(anuncioWithImages);
    }
}

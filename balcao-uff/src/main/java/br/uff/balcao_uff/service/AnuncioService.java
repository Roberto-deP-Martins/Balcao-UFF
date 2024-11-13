package br.uff.balcao_uff.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.uff.balcao_uff.api.dto.request.AnuncioRequestDTO;
import br.uff.balcao_uff.api.dto.response.AnuncioResponseDTO;
import br.uff.balcao_uff.entity.AnuncioEntity;
import br.uff.balcao_uff.entity.UserEntity;
import br.uff.balcao_uff.repository.AnuncioRepository;
import br.uff.balcao_uff.repository.UserRepository;
import lombok.AllArgsConstructor;

@AllArgsConstructor
@Service
public class AnuncioService {

    private final AnuncioRepository anuncioRepository;
    private final UserRepository userRepository;

    // Método para salvar um novo anúncio
    @Transactional
    public AnuncioResponseDTO save(AnuncioRequestDTO anuncioRequestDTO) {
        // Busca o usuário pelo ID
        UserEntity user = userRepository.findById(anuncioRequestDTO.getUserId())
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));

        // Converte o DTO para a entidade
        AnuncioEntity anuncioEntity = AnuncioEntity.builder()
                .title(anuncioRequestDTO.getTitle())
                .description(anuncioRequestDTO.getDescription())
                .category(anuncioRequestDTO.getCategory())
                .price(anuncioRequestDTO.getPrice())
                .contactInfo(anuncioRequestDTO.getContactInfo())
                .location(anuncioRequestDTO.getLocation())
                .user(user)  // Associa o usuário encontrado
                .build();

        // Salva a entidade
        AnuncioEntity savedAnuncio = anuncioRepository.save(anuncioEntity);

        // Retorna o DTO de resposta
        return convertToDto(savedAnuncio);
    }

    // Método para atualizar um anúncio existente
    @Transactional
    public void update(AnuncioRequestDTO anuncioRequestDTO) {
        AnuncioEntity existingAnuncio = anuncioRepository.findById(anuncioRequestDTO.getId())
                .orElseThrow(() -> new RuntimeException("Anúncio não encontrado"));

        // Atualiza os campos da entidade
        if (anuncioRequestDTO.getTitle() != null) existingAnuncio.setTitle(anuncioRequestDTO.getTitle());
        if (anuncioRequestDTO.getDescription() != null) existingAnuncio.setDescription(anuncioRequestDTO.getDescription());
        if (anuncioRequestDTO.getCategory() != null) existingAnuncio.setCategory(anuncioRequestDTO.getCategory());
        if (anuncioRequestDTO.getPrice() != 0) existingAnuncio.setPrice(anuncioRequestDTO.getPrice());
        if (anuncioRequestDTO.getContactInfo() != null) existingAnuncio.setContactInfo(anuncioRequestDTO.getContactInfo());
        if (anuncioRequestDTO.getLocation() != null) existingAnuncio.setLocation(anuncioRequestDTO.getLocation());

        // Salva a entidade atualizada
        anuncioRepository.save(existingAnuncio);
    }

    // Método para listar todos os anúncios
//    public List<AnuncioResponseDTO> findAll() {
//        List<AnuncioEntity> anuncios = anuncioRepository.getAllAnuncios();
//        return anuncios.stream()
//                .map(this::convertToDto)
//                .collect(Collectors.toList());
//    }

    // Método auxiliar para converter a entidade para o DTO de resposta
    private AnuncioResponseDTO convertToDto(AnuncioEntity anuncioEntity) {
        return AnuncioResponseDTO.builder()
                .id(anuncioEntity.getId())
                .title(anuncioEntity.getTitle())
                .description(anuncioEntity.getDescription())
                .category(anuncioEntity.getCategory())
                .price(anuncioEntity.getPrice())
                .contactInfo(anuncioEntity.getContactInfo())
                .location(anuncioEntity.getLocation())
                .userId(anuncioEntity.getUser() != null ? anuncioEntity.getUser().getId() : null)
                .build();
    }
    
    public List<AnuncioEntity> getAll(){
    	return anuncioRepository.findAllAnuncios();
    }
}

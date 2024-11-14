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

    @Transactional
    public void update(AnuncioRequestDTO anuncioRequestDTO) {
        AnuncioEntity existingAnuncio = anuncioRepository.findById(anuncioRequestDTO.getId())
                .orElseThrow(() -> new RuntimeException("Anúncio não encontrado"));

        if (anuncioRequestDTO.getTitle() != null) existingAnuncio.setTitle(anuncioRequestDTO.getTitle());
        if (anuncioRequestDTO.getDescription() != null) existingAnuncio.setDescription(anuncioRequestDTO.getDescription());
        if (anuncioRequestDTO.getCategory() != null) existingAnuncio.setCategory(anuncioRequestDTO.getCategory());
        if (anuncioRequestDTO.getPrice() != 0) existingAnuncio.setPrice(anuncioRequestDTO.getPrice());
        if (anuncioRequestDTO.getContactInfo() != null) existingAnuncio.setContactInfo(anuncioRequestDTO.getContactInfo());
        if (anuncioRequestDTO.getLocation() != null) existingAnuncio.setLocation(anuncioRequestDTO.getLocation());

        anuncioRepository.save(existingAnuncio);
    }

    public List<AnuncioResponseDTO> findAll() {
        List<AnuncioEntity> anuncios = anuncioRepository.findAll();
        return anuncios.stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }
    
    public List<AnuncioResponseDTO> findByCategory(String category){
    	
    	List<AnuncioEntity> anuncios = anuncioRepository.findByCategory(category);
    	return anuncios.stream()
    			.map(this::convertToDto) 
    			.collect(Collectors.toList()); 
    }
    
    @Transactional public void deleteById(Long id) { 
    	AnuncioEntity existingAnuncio = anuncioRepository.findById(id) 
    			.orElseThrow(() -> new RuntimeException("Anúncio não encontrado")); 
    			anuncioRepository.delete(existingAnuncio);
    }

    /**
     * Método auxiliar para converter a entidade para o DTO de resposta
     * @param anuncioEntity
     * @return
     */
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
}
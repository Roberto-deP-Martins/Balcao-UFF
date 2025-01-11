package br.uff.balcao_uff.service;

import java.util.Date;
import java.util.List;

import org.springframework.stereotype.Service;

import br.uff.balcao_uff.api.dto.request.TransacaoRequestDTO;
import br.uff.balcao_uff.api.dto.response.TransacaoResponseDTO;
import br.uff.balcao_uff.commons.util.exceptions.SameUserException;
import br.uff.balcao_uff.commons.util.exceptions.TransactionUpdateException;
import br.uff.balcao_uff.entity.AnuncioEntity;
import br.uff.balcao_uff.entity.TransacaoEntity;
import br.uff.balcao_uff.entity.UserEntity;
import br.uff.balcao_uff.repository.AnuncioRepository;
import br.uff.balcao_uff.repository.TransacaoRepository;
import br.uff.balcao_uff.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class TransacaoService {

	private final TransacaoRepository repository;
	
	private final UserRepository userRepository;
	
	private final AnuncioRepository anuncioRepository;

	public List<TransacaoResponseDTO> getAll() {
	    return repository.findAll().stream()
	            .map(this::transacaoEntityToTransacaoResponseDTO)
	            .toList();
	}

	
	public TransacaoResponseDTO transacaoEntityToTransacaoResponseDTO(TransacaoEntity entity) {
	    
		return TransacaoResponseDTO.builder()
	            .anuncioId(entity.getAnuncio().getId())
	            .anuncianteId(entity.getVendedor().getId())
	            .interessadoId(entity.getComprador().getId())
	            .dtConclusao(entity.getDataConclusao())
	            .anuncianteReview(entity.isVendedorAvaliou())
	            .interessadoReview(entity.isCompradorAvaliou())
	            .build();
	}
	
	@Transactional
	public TransacaoResponseDTO create(TransacaoRequestDTO dto) {

	    if (dto.anuncianteId().equals(dto.interessadoId())) {
	        throw new SameUserException("O anunciante não pode ser o mesmo que o interessado.");
	    }

	    AnuncioEntity anuncio = anuncioRepository.findById(dto.anuncioId())
	            .orElseThrow(() -> new RuntimeException("Anúncio não encontrado"));
	    UserEntity anunciante = userRepository.findById(dto.anuncianteId())
	            .orElseThrow(() -> new RuntimeException("Anunciante não encontrado"));
	    UserEntity interessado = userRepository.findById(dto.interessadoId())
	            .orElseThrow(() -> new RuntimeException("Interessado não encontrado"));

	    TransacaoEntity entity = TransacaoEntity.builder()
	            .anuncio(anuncio)
	            .vendedor(anunciante)
	            .comprador(interessado)
	            .vendedorAvaliou(false)
	            .compradorAvaliou(false)
	            .build();

	    repository.save(entity);

	    anuncio.setDtDelete(new Date());
	    anuncioRepository.save(anuncio);

	    return transacaoEntityToTransacaoResponseDTO(entity);
	}



	public List<TransacaoResponseDTO> findByUserId(Long userId) {
		
	    return repository.findAllByVendedorIdOrCompradorId(userId, userId).stream()
	            .map(this::transacaoEntityToTransacaoResponseDTO)
	            .toList();
	}
	
	public void updateReviewStatus(Long userId, Long otherUserId) {
        try {
            repository.updateTransactionReviewStatus(userId, otherUserId);
        } catch (Exception ex) {
            throw new TransactionUpdateException("Erro ao atualizar os status de avaliação da transação.");
        }
    }
}



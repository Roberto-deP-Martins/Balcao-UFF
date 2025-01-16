package br.uff.balcao_uff.service;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import br.uff.balcao_uff.commons.util.exceptions.ConversaNotFoundException;
import br.uff.balcao_uff.commons.util.exceptions.NegocioNaoAceitoException;
import br.uff.balcao_uff.entity.ConversaEntity;
import br.uff.balcao_uff.repository.*;
import org.springframework.stereotype.Service;

import br.uff.balcao_uff.api.dto.request.TransacaoRequestDTO;
import br.uff.balcao_uff.api.dto.response.TransacaoResponseDTO;
import br.uff.balcao_uff.commons.util.exceptions.SameUserException;
import br.uff.balcao_uff.commons.util.exceptions.TransactionUpdateException;
import br.uff.balcao_uff.entity.AnuncioEntity;
import br.uff.balcao_uff.entity.TransacaoEntity;
import br.uff.balcao_uff.entity.UserEntity;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class TransacaoService {

	private final TransacaoRepository repository;
	
	private final UserRepository userRepository;
	
	private final AnuncioRepository anuncioRepository;

	private final ConversaRepository conversaRepository;

	private final UserReviewRepository userReviewRepository;

	public List<TransacaoResponseDTO> getAll() {
	    return repository.findAll().stream()
	            .map(this::transacaoEntityToTransacaoResponseDTO)
	            .toList();
	}

	
	public TransacaoResponseDTO transacaoEntityToTransacaoResponseDTO(TransacaoEntity entity) {
	    
		return TransacaoResponseDTO.builder()
	            .anuncioId(entity.getAnuncio().getId())
	            .anuncioName(entity.getAnuncio().getTitle())
	            .anuncianteId(entity.getVendedor().getId())
	            .interessadoId(entity.getComprador().getId())
	            .dtConclusao(entity.getDataConclusao())
	            .anuncianteReview(entity.isVendedorAvaliou())
	            .interessadoReview(entity.isCompradorAvaliou())
	            .build();
	}
	
	@Transactional
	public TransacaoResponseDTO create(TransacaoRequestDTO dto) {

		Optional<ConversaEntity> conversaByIds = getConversaEntity(dto);
		verificarSeConversaExiste(conversaByIds);
		verificarSeAnuncianteIgualInteressado(dto);
		verificarSeInteressadoQuerFecharNegocio(conversaByIds);

		AnuncioEntity anuncio = anuncioRepository.findById(dto.anuncioId())
	            .orElseThrow(() -> new RuntimeException("Anúncio não encontrado"));
		UserEntity anunciante = userRepository.findById(dto.anuncianteId())
	            .orElseThrow(() -> new RuntimeException("Anunciante não encontrado"));
		UserEntity interessado = userRepository.findById(dto.interessadoId())
	            .orElseThrow(() -> new RuntimeException("Interessado não encontrado"));

		TransacaoEntity entity = getTransacaoEntity(dto, anuncio, anunciante, interessado);

		repository.save(entity);

		anuncio.setDtDelete(new Date());
		anuncioRepository.save(anuncio);

	    return transacaoEntityToTransacaoResponseDTO(entity);
	}

	private TransacaoEntity getTransacaoEntity(TransacaoRequestDTO dto, AnuncioEntity anuncio, UserEntity anunciante, UserEntity interessado) {
		TransacaoEntity entity = TransacaoEntity.builder()
	            .anuncio(anuncio)
	            .vendedor(anunciante)
	            .comprador(interessado)
	            .build();

		boolean anuncianteReview = userReviewRepository.existsByReviewerIdAndReviewedId(dto.anuncianteId(), dto.interessadoId());
        entity.setVendedorAvaliou(anuncianteReview);

		boolean interessadoReview = userReviewRepository.existsByReviewerIdAndReviewedId(dto.interessadoId(), dto.anuncianteId());
        entity.setCompradorAvaliou(interessadoReview);

		return entity;
	}

	private Optional<ConversaEntity> getConversaEntity(TransacaoRequestDTO dto) {
        return conversaRepository.findConversaByIds(dto.anuncioId(),
                                                                                    dto.interessadoId(),
                                                                                    dto.anuncianteId());
	}

	private static void verificarSeInteressadoQuerFecharNegocio(Optional<ConversaEntity> conversaByIds) {
		if(!conversaByIds.get().isInteressadoFecharNegocio()){
			throw new NegocioNaoAceitoException("O interessado não quer fechar negócio ainda.");
		}
	}

	private static void verificarSeAnuncianteIgualInteressado(TransacaoRequestDTO dto) {
		if (dto.anuncianteId().equals(dto.interessadoId())) {
			throw new SameUserException("O anunciante não pode ser o mesmo que o interessado.");
		}
	}

	private static void verificarSeConversaExiste(Optional<ConversaEntity> conversaByIds) {
		if(conversaByIds.isEmpty()){
			throw new ConversaNotFoundException("Conversa não encontrada");
		}
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



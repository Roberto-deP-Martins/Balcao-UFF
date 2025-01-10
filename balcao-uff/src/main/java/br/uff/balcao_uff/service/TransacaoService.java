package br.uff.balcao_uff.service;

import java.util.List;

import org.springframework.stereotype.Service;

import br.uff.balcao_uff.api.dto.response.TransacaoResponseDTO;
import br.uff.balcao_uff.entity.TransacaoEntity;
import br.uff.balcao_uff.repository.TransacaoRepository;
import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class TransacaoService {

	private final TransacaoRepository repository;

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


}

package br.uff.balcao_uff.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import br.uff.balcao_uff.entity.ConversaEntity;

import java.util.List;

public interface ConversaRepository extends JpaRepository<ConversaEntity, Long> {
	boolean existsByAnuncioIdAndInteressadoId(Long anuncioId, Long interessadoId);

	List<ConversaEntity> findByAnuncioId(Long anuncioId);

	List<ConversaEntity> findByAnuncioIdAndInteressadoId(Long anuncioId, Long interessadoId);

	List<ConversaEntity> findByAnuncioIdAndAnuncianteIdOrAnuncioIdAndInteressadoId(Long anuncioId, Long anuncianteId,
			Long anuncioId2, Long interessadoId);
}

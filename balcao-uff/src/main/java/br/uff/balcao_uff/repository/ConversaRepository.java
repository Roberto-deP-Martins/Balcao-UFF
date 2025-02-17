package br.uff.balcao_uff.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import br.uff.balcao_uff.entity.ConversaEntity;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ConversaRepository extends JpaRepository<ConversaEntity, Long> {
	boolean existsByAnuncioIdAndInteressadoId(Long anuncioId, Long interessadoId);

	List<ConversaEntity> findByAnuncioId(Long anuncioId);

	List<ConversaEntity> findByAnuncioIdAndInteressadoId(Long anuncioId, Long interessadoId);

	List<ConversaEntity> findByAnuncioIdAndAnuncianteIdOrAnuncioIdAndInteressadoId(Long anuncioId, Long anuncianteId,
			Long anuncioId2, Long interessadoId);

	@Query("SELECT c FROM ConversaEntity c WHERE c.anuncio.id = :anuncioId AND c.interessado.id = :interessadoId AND c.anunciante.id = :anuncianteId")
	Optional<ConversaEntity> findConversaByIds(@Param("anuncioId") Long anuncioId,
											  @Param("interessadoId") Long interessadoId,
											  @Param("anuncianteId") Long anuncianteId);
}

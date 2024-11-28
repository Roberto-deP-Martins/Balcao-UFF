package br.uff.balcao_uff.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import br.uff.balcao_uff.entity.ConversaEntity;

public interface ConversaRepository extends JpaRepository<ConversaEntity, Long> {

	boolean existsByAnuncioIdAndInteressadoId(Long anuncioId, Long interessadoId);

}

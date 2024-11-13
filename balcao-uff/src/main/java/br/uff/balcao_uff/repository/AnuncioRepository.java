package br.uff.balcao_uff.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import br.uff.balcao_uff.entity.AnuncioEntity;

public interface AnuncioRepository extends JpaRepository<AnuncioEntity, Long> {

	public static final String FIND_BY_USERBNAME = "SELECT * FROM tb_anuncio";

	@Query(value = FIND_BY_USERBNAME, nativeQuery = true)
	List<AnuncioEntity> findAllAnuncios();
}

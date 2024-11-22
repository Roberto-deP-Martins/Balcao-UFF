package br.uff.balcao_uff.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import br.uff.balcao_uff.entity.AnuncioEntity;

public interface AnuncioRepository extends JpaRepository<AnuncioEntity, Long> {

	static final String FIND_BY_CATEGORY = "SELECT * "
											+ " FROM tb_anuncio ta "
											+ "	WHERE LOWER(ta.category) "
											+ "	LIKE LOWER(:category)";

	@Query(value = FIND_BY_CATEGORY, nativeQuery = true)
	List<AnuncioEntity> findByCategory(@Param("category")String category);
}
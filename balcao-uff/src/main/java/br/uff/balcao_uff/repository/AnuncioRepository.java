package br.uff.balcao_uff.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import br.uff.balcao_uff.api.dto.response.AnuncioResponseDTO;
import br.uff.balcao_uff.entity.AnuncioEntity;

public interface AnuncioRepository extends JpaRepository<AnuncioEntity, Long> {

	static final String SELECT_ANUNCIOSDTO_FROM_ANUNCIO_PART1 = "SELECT new br.uff.balcao_uff.api.dto.response.AnuncioResponseDTO(";
	static final String SELECT_ANUNCIOSDTO_FROM_ANUNCIO_PART2 = ") FROM AnuncioEntity ae LEFT JOIN ae.images img";

	static final String FIND_ANUNCIOS_BASE = SELECT_ANUNCIOSDTO_FROM_ANUNCIO_PART1 
											+ " ae.id," 
											+ " ae.title,"
											+ " ae.description," 
											+ " ae.category," 
											+ " ae.price," 
											+ " ae.contactInfo," 
											+ " ae.location,"
											+ " ae.user.id," 
											+ " STRING_AGG(img.imagePath, ',') AS imagePaths " 
											+ SELECT_ANUNCIOSDTO_FROM_ANUNCIO_PART2;


	@Query(value = FIND_ANUNCIOS_BASE + " GROUP BY ae.id, ae.title, ae.description, ae.category, ae.price, ae.contactInfo, ae.location, ae.user.id", nativeQuery = true) 
	List<AnuncioResponseDTO> searchAnunciosByAdvancedCriteria(@Param("category") String category);

	static final String FIND_BY_CATEGORY = "SELECT * " + " FROM tb_anuncio ta " + "	WHERE LOWER(ta.category) "
			+ "	LIKE LOWER(:category)" + " ORDER BY ta.id DESC";

	@Query(value = FIND_BY_CATEGORY, nativeQuery = true)
	List<AnuncioEntity> findByCategory(@Param("category") String category);

	@Query("SELECT a FROM AnuncioEntity a ORDER BY a.id DESC")
	List<AnuncioEntity> findAllByOrderByIdDesc();
}
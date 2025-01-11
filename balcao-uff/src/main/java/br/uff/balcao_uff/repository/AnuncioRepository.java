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
            + SELECT_ANUNCIOSDTO_FROM_ANUNCIO_PART2
            + " WHERE ae.dtDelete IS NULL";

    static final String CALCULO_DISTANCIA = "SELECT a "
            + "FROM AnuncioEntity a "
            + "JOIN a.location l "
            + "WHERE a.dtDelete IS NULL AND "
            + "(6371 * acos(cos(radians(:lat)) * "
            + "cos(radians(l.latitude)) * "
            + "cos(radians(l.longitude) - "
            + "radians(:lng)) + "
            + "sin(radians(:lat)) * "
            + "sin(radians(l.latitude)))) < :radius";

    @Query(value = FIND_ANUNCIOS_BASE
            + " GROUP BY ae.id, ae.title, ae.description, ae.category, ae.price, ae.contactInfo, ae.location, ae.user.id", nativeQuery = true)
    List<AnuncioResponseDTO> searchAnunciosByAdvancedCriteria(@Param("category") String category);

    static final String FIND_BY_CATEGORY = "SELECT * "
            + " FROM tb_anuncio ta "
            + " WHERE ta.dt_delete IS NULL AND LOWER(ta.category) "
            + " LIKE LOWER(:category) "
            + " ORDER BY ta.id DESC";

    @Query(value = FIND_BY_CATEGORY, nativeQuery = true)
    List<AnuncioEntity> findByCategory(@Param("category") String category);

    @Query("SELECT a FROM AnuncioEntity a WHERE a.dtDelete IS NULL ORDER BY a.id DESC")
    List<AnuncioEntity> findAllByOrderByIdDesc();

    /**
	 * Encontra anúncios próximos com base na localização fornecida.
	 * 
	 * O cálculo é realizado utilizando a fórmula de Haversine, que determina a 
	 * distância entre dois pontos na superfície de uma esfera a partir da 
	 * latitude e longitude. Usada para calcular distâncias entre 
	 * dois locais na Terra.
	 * 
	 * @param lat    Latitude do ponto de referência.
	 * @param lng    Longitude do ponto de referência.
	 * @param radius Raio em quilômetros para buscar anúncios próximos.
	 * @return Lista de entidades de anúncio próximas à localização fornecida.
	 */
    @Query(CALCULO_DISTANCIA)
    List<AnuncioEntity> findNearby(@Param("lat") double lat, @Param("lng") double lng, @Param("radius") double radius);
}

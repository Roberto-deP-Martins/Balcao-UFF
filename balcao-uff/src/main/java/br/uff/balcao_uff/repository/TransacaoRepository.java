package br.uff.balcao_uff.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import br.uff.balcao_uff.entity.TransacaoEntity;
import jakarta.transaction.Transactional;

@Repository
public interface TransacaoRepository extends JpaRepository<TransacaoEntity, Long> {

	static final String UPDATE_REVIEW = "UPDATE tb_transacoes SET"
										+ " vendedor_avaliou = CASE"
										+ " WHEN vendedor_id = :userId THEN true" + " ELSE vendedor_avaliou END," + " comprador_avaliou = CASE"
										+ " WHEN comprador_id = :userId THEN true" + " ELSE comprador_avaliou END"
										+ " WHERE (vendedor_id = :userId AND comprador_id = :otherUserId)"
										+ " OR (vendedor_id = :otherUserId AND comprador_id = :userId)";

	List<TransacaoEntity> findByVendedorId(Long vendedorId);

	List<TransacaoEntity> findByCompradorId(Long compradorId);

	/**
	 * Buscar todas as transações relacionadas ao usuário como vendedor ou comprador
	 * 
	 * @param vendedorId
	 * @param compradorId
	 * @return
	 */
	List<TransacaoEntity> findAllByVendedorIdOrCompradorId(Long vendedorId, Long compradorId);

	@Transactional
	@Modifying
	@Query(value = UPDATE_REVIEW, nativeQuery = true)
	void updateTransactionReviewStatus(@Param("userId") Long userId, @Param("otherUserId") Long otherUserId);


}
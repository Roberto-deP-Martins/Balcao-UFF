package br.uff.balcao_uff.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.uff.balcao_uff.entity.TransacaoEntity;

@Repository
public interface TransacaoRepository extends JpaRepository<TransacaoEntity, Long> {

	List<TransacaoEntity> findByVendedorId(Long vendedorId);

	List<TransacaoEntity> findByCompradorId(Long compradorId);

	/**
	 * Buscar todas as transações relacionadas ao usuário como vendedor ou comprador
	 * @param vendedorId
	 * @param compradorId
	 * @return
	 */
    List<TransacaoEntity> findAllByVendedorIdOrCompradorId(Long vendedorId, Long compradorId);
}
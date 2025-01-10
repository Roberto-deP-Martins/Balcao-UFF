package br.uff.balcao_uff.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.uff.balcao_uff.entity.TransacaoEntity;

@Repository
public interface TransacaoRepository extends JpaRepository<TransacaoEntity, Long> {

	List<TransacaoEntity> findByVendedorId(Long vendedorId);

	List<TransacaoEntity> findByCompradorId(Long compradorId);
}
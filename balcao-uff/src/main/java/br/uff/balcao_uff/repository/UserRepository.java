package br.uff.balcao_uff.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import br.uff.balcao_uff.entity.AnuncioEntity;

public interface UserRepository extends JpaRepository<AnuncioEntity, Long> {

	List<AnuncioEntity> findByCategory(String category);
}

package br.uff.balcao_uff.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import br.uff.balcao_uff.entity.AnuncioEntity;

public interface AnuncioRepository extends JpaRepository<AnuncioEntity, Long> {

}
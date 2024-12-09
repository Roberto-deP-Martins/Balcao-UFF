package br.uff.balcao_uff.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.uff.balcao_uff.entity.AnuncioImageEntity;

@Repository
public interface AnuncioImageRepository extends JpaRepository<AnuncioImageEntity, Long> {


}
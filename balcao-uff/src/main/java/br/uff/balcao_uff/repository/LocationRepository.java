package br.uff.balcao_uff.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import br.uff.balcao_uff.entity.LocationEntity;

public interface LocationRepository extends JpaRepository<LocationEntity, Long> {
    
}

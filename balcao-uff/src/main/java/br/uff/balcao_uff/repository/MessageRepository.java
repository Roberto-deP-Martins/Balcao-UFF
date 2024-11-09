package br.uff.balcao_uff.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import br.uff.balcao_uff.entity.UserEntity;

public interface MessageRepository extends JpaRepository<UserEntity, Long> {
	
	UserEntity findByEmail(String email);
}

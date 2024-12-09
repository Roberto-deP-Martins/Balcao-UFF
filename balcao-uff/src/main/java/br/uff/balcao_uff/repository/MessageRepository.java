package br.uff.balcao_uff.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import br.uff.balcao_uff.entity.MessageEntity;

public interface MessageRepository extends JpaRepository<MessageEntity, Long> {

	public List<MessageEntity> findByConversaId(Long id);
}

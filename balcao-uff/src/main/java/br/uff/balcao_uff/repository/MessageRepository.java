package br.uff.balcao_uff.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import br.uff.balcao_uff.entity.MessageEntity;
import br.uff.balcao_uff.entity.UserEntity;

public interface MessageRepository extends JpaRepository<MessageEntity, Long> {

}

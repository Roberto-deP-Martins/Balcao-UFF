package br.uff.balcao_uff.repository;

import br.uff.balcao_uff.entity.UserReviewEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserReviewRepository extends JpaRepository<UserReviewEntity, Long> {
    List<UserReviewEntity> findByReviewedId(Long reviewedId);
}

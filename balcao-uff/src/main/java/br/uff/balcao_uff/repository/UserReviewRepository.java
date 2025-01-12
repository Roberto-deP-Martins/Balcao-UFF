package br.uff.balcao_uff.repository;

import br.uff.balcao_uff.entity.UserReviewEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserReviewRepository extends JpaRepository<UserReviewEntity, Long> {
    List<UserReviewEntity> findByReviewedId(Long reviewedId);

    @Query("SELECT CASE WHEN COUNT(u) > 0 THEN TRUE ELSE FALSE END FROM UserReviewEntity u " +
            "WHERE u.reviewer.id = :reviewerId AND u.reviewed.id = :reviewedId")
    boolean existsByReviewerIdAndReviewedId(@Param("reviewerId") Long reviewerId,
                                            @Param("reviewedId") Long reviewedId);
}

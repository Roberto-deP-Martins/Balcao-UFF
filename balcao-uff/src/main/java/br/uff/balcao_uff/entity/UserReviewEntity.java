package br.uff.balcao_uff.entity;

import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "tb_user_reviews")
public class UserReviewEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "reviewer_id", nullable = false)
    private UserEntity reviewer;

    @ManyToOne
    @JoinColumn(name = "reviewed_id", nullable = false)
    private UserEntity reviewed;

    @Column(nullable = false)
    private int rating; // e.g., de 1 a 5

    @Column(length = 500)
    private String comment;

    @Column(name = "review_date", nullable = false)
    private LocalDateTime reviewDate;

    @PrePersist
    public void onCreate() {
        if (this.reviewDate == null) {
            this.reviewDate = LocalDateTime.now();
        }
    }


}

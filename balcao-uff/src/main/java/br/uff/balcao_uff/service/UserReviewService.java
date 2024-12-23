package br.uff.balcao_uff.service;

import br.uff.balcao_uff.api.dto.response.UserReviewResponseDTO;
import br.uff.balcao_uff.entity.UserEntity;
import br.uff.balcao_uff.entity.UserReviewEntity;
import br.uff.balcao_uff.repository.UserRepository;
import br.uff.balcao_uff.repository.UserReviewRepository;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserReviewService {
    private final UserReviewRepository userReviewRepository;
    private final UserRepository userRepository;

    public void addReview(Long reviewerId, Long reviewedId, int rating, String comment) {
        if (rating < 1 || rating > 5) {
            throw new IllegalArgumentException("A nota deve estar entre 1 e 5.");
        }

        UserEntity reviewer = userRepository.findById(reviewerId)
                .orElseThrow(() -> new RuntimeException("Usuário avaliador não encontrado"));
        UserEntity reviewed = userRepository.findById(reviewedId)
                .orElseThrow(() -> new RuntimeException("Usuário avaliado não encontrado"));

        UserReviewEntity review = UserReviewEntity.builder()
                .reviewer(reviewer)
                .reviewed(reviewed)
                .rating(rating)
                .comment(comment)
                .build();

        userReviewRepository.save(review);
    }

    public double calculateReputation(Long userId) {
        List<UserReviewEntity> reviews = userReviewRepository.findByReviewedId(userId);
        if (reviews.isEmpty()) {
            return 0.0; // Sem avaliações
        }

        double sum = reviews.stream().mapToInt(UserReviewEntity::getRating).sum();
        return sum / reviews.size();
    }

    public List<UserReviewResponseDTO> getReviewsByUser(Long userId) {
        List<UserReviewEntity> reviews = userReviewRepository.findByReviewedId(userId);

        return reviews.stream()
                .map(review -> UserReviewResponseDTO.builder()
                        .reviewerName(review.getReviewer().getEmail())
                        .rating(review.getRating())
                        .comment(review.getComment())
                        .reviewDate(review.getReviewDate())
                        .build())
                .collect(Collectors.toList());
    }
}

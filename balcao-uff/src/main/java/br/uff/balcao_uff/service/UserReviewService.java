package br.uff.balcao_uff.service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import br.uff.balcao_uff.api.dto.request.UserReviewRequestDTO;
import br.uff.balcao_uff.api.dto.response.ReputationResponseDTO;
import br.uff.balcao_uff.api.dto.response.UserReviewResponseDTO;
import br.uff.balcao_uff.commons.util.exceptions.DuplicateReviewException;
import br.uff.balcao_uff.commons.util.response.ResponseFormatter;
import br.uff.balcao_uff.entity.UserEntity;
import br.uff.balcao_uff.entity.UserReviewEntity;
import br.uff.balcao_uff.repository.UserRepository;
import br.uff.balcao_uff.repository.UserReviewRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserReviewService {
    private final UserReviewRepository userReviewRepository;
    private final UserRepository userRepository;
    
    private final TransacaoService transacaoService;

    /**
     * Adiciona uma avaliação de usuário com base nos dados recebidos.
     *
     * @param reviewRequestDTO Objeto contendo os dados da avaliação.
     * @return Mapa formatado com a mensagem de sucesso.
     */
    public Map<String, Object> addReview(UserReviewRequestDTO reviewRequestDTO) {
        try {
            // Valida a nota (rating)
            if (reviewRequestDTO.getRating() < 1 || reviewRequestDTO.getRating() > 5) {
                throw new IllegalArgumentException("A nota deve estar entre 1 e 5.");
            }

            // Atualiza o status da transação
            transacaoService.updateReviewStatus(reviewRequestDTO.getReviewerId(), reviewRequestDTO.getReviewedId());

            // Busca os usuários envolvidos
            UserEntity reviewer = userRepository.findById(reviewRequestDTO.getReviewerId())
                    .orElseThrow(() -> new RuntimeException("Usuário avaliador não encontrado"));
            UserEntity reviewed = userRepository.findById(reviewRequestDTO.getReviewedId())
                    .orElseThrow(() -> new RuntimeException("Usuário avaliado não encontrado"));

            // Cria a entidade de avaliação
            UserReviewEntity review = UserReviewEntity.builder()
                    .reviewer(reviewer)
                    .reviewed(reviewed)
                    .rating(reviewRequestDTO.getRating())
                    .comment(reviewRequestDTO.getComment())
                    .build();

            // Salva a avaliação no banco de dados
            userReviewRepository.save(review);

            // Retorna a resposta formatada de sucesso
            return ResponseFormatter.createSuccessResponse("Avaliação adicionada com sucesso!");
        } catch (DataIntegrityViolationException ex) {
            // Lança exceção personalizada caso a chave única seja violada
            throw new DuplicateReviewException("Já existe uma avaliação entre esses usuários.");
        }
    }

    public ReputationResponseDTO calculateReputation(Long userId) {
        List<UserReviewEntity> reviews = userReviewRepository.findByReviewedId(userId);
        if (reviews.isEmpty()) {
            return ReputationResponseDTO.builder()
                    .reputation(0.0)
                    .build();
        }

        double sum = reviews.stream().mapToInt(UserReviewEntity::getRating).sum();
        return ReputationResponseDTO.builder()
                .reputation(sum / reviews.size())
                .build();
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

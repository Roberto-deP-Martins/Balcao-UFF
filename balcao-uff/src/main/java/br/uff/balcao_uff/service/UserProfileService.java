package br.uff.balcao_uff.service;

import br.uff.balcao_uff.api.dto.response.UserProfileResponseDTO;
import br.uff.balcao_uff.entity.UserEntity;
import br.uff.balcao_uff.entity.UserReviewEntity;
import br.uff.balcao_uff.repository.UserRepository;
import br.uff.balcao_uff.repository.UserReviewRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.OptionalDouble;

@Service
public class UserProfileService {

    private final UserRepository userRepository;
    private final UserReviewRepository userReviewRepository;

    public UserProfileService(UserRepository userRepository, UserReviewRepository userReviewRepository) {
        this.userRepository = userRepository;
        this.userReviewRepository = userReviewRepository;
    }

    // Método para obter as informações do perfil do usuário
    public UserProfileResponseDTO getUserProfile(Long userId) {
        // Busca o usuário no banco de dados
        UserEntity user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));

        // Calcula a reputação com base nas avaliações
        double reputation = calculateReputation(userId);

        return UserProfileResponseDTO.builder()
                .id(user.getId())
                .name(user.getName())
                .email(user.getEmail())
                .reputation(reputation)
                .build();
    }

    // Método para calcular a reputação do usuário
    private double calculateReputation(Long userId) {
        List<UserReviewEntity> reviews = userReviewRepository.findByReviewedId(userId);

        if (reviews.isEmpty()) {
            return 0; // Se não houver avaliações, a reputação é 0
        }

        // Calculando a média dos ratings
        OptionalDouble averageRating = reviews.stream()
                .mapToInt(UserReviewEntity::getRating)
                .average();

        return averageRating.orElse(0); // Retorna a média, ou 0 se não houver avaliações
    }
}
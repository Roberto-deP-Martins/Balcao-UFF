package br.uff.balcao_uff.api.resource.impl;

import br.uff.balcao_uff.api.dto.request.UserReviewRequestDTO;
import br.uff.balcao_uff.api.dto.response.UserReviewResponseDTO;
import br.uff.balcao_uff.api.resource.swagger.UserReviewResourceApi;
import br.uff.balcao_uff.service.UserReviewService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@Tag(name = "Reviews de usuário", description = "Gerenciamento de Reviews")
@RequestMapping("/reviews")
public class UserReviewResource implements UserReviewResourceApi {
    private final UserReviewService userReviewService;

    @PostMapping
    public ResponseEntity<Void> addReview(@RequestBody UserReviewRequestDTO reviewRequestDTO) {
        userReviewService.addReview(
                reviewRequestDTO.getReviewerId(),
                reviewRequestDTO.getReviewedId(),
                reviewRequestDTO.getRating(),
                reviewRequestDTO.getComment()
        );
        return ResponseEntity.ok().build();
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<UserReviewResponseDTO>> getReviewsByUser(@PathVariable Long userId) {
        List<UserReviewResponseDTO> reviews = userReviewService.getReviewsByUser(userId);
        return ResponseEntity.ok(reviews);
    }


    @GetMapping("/user/{userId}/reputation")
    public ResponseEntity<Double> getUserReputation(@PathVariable Long userId) {
        double reputation = userReviewService.calculateReputation(userId);
        return ResponseEntity.ok(reputation);
    }

}

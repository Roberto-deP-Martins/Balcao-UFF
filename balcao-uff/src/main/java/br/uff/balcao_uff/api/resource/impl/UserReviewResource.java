package br.uff.balcao_uff.api.resource.impl;

import br.uff.balcao_uff.api.dto.request.UserReviewRequestDTO;
import br.uff.balcao_uff.api.dto.response.ReputationResponseDTO;
import br.uff.balcao_uff.api.dto.response.UserReviewResponseDTO;
import br.uff.balcao_uff.api.resource.swagger.UserReviewResourceApi;
import br.uff.balcao_uff.service.UserReviewService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@Tag(name = "Reviews de usuário", description = "Gerenciamento de Reviews")
@RequestMapping("/reviews")
public class UserReviewResource implements UserReviewResourceApi {
    private final UserReviewService userReviewService;

    @PostMapping
    public ResponseEntity<Map<String, Object>> addReview(@RequestBody UserReviewRequestDTO reviewRequestDTO) {
        Map<String, Object> responseBody = userReviewService.addReview(reviewRequestDTO);
        return ResponseEntity.ok(responseBody);
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<UserReviewResponseDTO>> getReviewsByUser(@PathVariable Long userId) {
        List<UserReviewResponseDTO> reviews = userReviewService.getReviewsByUser(userId);
        return ResponseEntity.ok(reviews);
    }


    @GetMapping("/user/{userId}/reputation")
    public ResponseEntity<ReputationResponseDTO> getUserReputation(@PathVariable Long userId) {
        ReputationResponseDTO reputation = userReviewService.calculateReputation(userId);
        return ResponseEntity.ok(reputation);
    }

}

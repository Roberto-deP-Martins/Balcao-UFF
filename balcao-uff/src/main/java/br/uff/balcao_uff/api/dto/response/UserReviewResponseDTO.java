package br.uff.balcao_uff.api.dto.response;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserReviewResponseDTO {
    private String reviewerName;
    private int rating;
    private String comment;
    private LocalDateTime reviewDate;
}
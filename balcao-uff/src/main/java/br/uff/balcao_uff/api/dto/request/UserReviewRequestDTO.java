package br.uff.balcao_uff.api.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserReviewRequestDTO {
    private Long reviewerId;
    private Long reviewedId;
    private int rating;
    private String comment;
}


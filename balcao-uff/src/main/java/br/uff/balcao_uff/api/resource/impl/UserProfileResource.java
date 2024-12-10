package br.uff.balcao_uff.api.resource.impl;
import br.uff.balcao_uff.api.dto.response.UserProfileResponseDTO;
import br.uff.balcao_uff.api.resource.swagger.UserProfileResourceApi;
import br.uff.balcao_uff.service.AuthorizationService;
import br.uff.balcao_uff.service.UserProfileService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
@RestController
@RequiredArgsConstructor
public class UserProfileResource implements UserProfileResourceApi {

    private final AuthorizationService authorizationService;
    private final UserProfileService userProfileService;

    @Override
    public ResponseEntity<UserProfileResponseDTO> getAuthenticatedUserProfile() {
        var authenticatedUser = authorizationService.getAuthenticatedUser();

        if (authenticatedUser == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        }

        UserProfileResponseDTO userProfile = userProfileService.getUserProfile(authenticatedUser.getId());
        return ResponseEntity.ok(userProfile);
    }
}
package br.uff.balcao_uff.api.resource.impl;
import br.uff.balcao_uff.api.dto.response.UserProfileResponseDTO;
import br.uff.balcao_uff.api.dto.response.UserResponseDTO;
import br.uff.balcao_uff.api.resource.swagger.UserProfileResourceApi;
import br.uff.balcao_uff.service.AuthorizationService;
import br.uff.balcao_uff.service.UserProfileService;
import br.uff.balcao_uff.service.UserService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@CrossOrigin(origins = "*")
@RequiredArgsConstructor
@Tag(name = "Users", description = "Gerenciamento de usuários")
public class UserProfileResource implements UserProfileResourceApi {

    private final AuthorizationService authorizationService;
    private final UserProfileService userProfileService;
    private final UserService userService;

    @Override
    public ResponseEntity<UserProfileResponseDTO> getAuthenticatedUserProfile() {
        var authenticatedUser = authorizationService.getAuthenticatedUser();

        if (authenticatedUser == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        }

        UserProfileResponseDTO userProfile = userProfileService.getUserProfile(authenticatedUser.getId());
        return ResponseEntity.ok(userProfile);
    }

    @Override
    public ResponseEntity<List<UserResponseDTO>> getALl(){
        return ResponseEntity.ok(userService.getAll());
    }

    @Override
    public ResponseEntity<UserResponseDTO> getById(Long userId) {
        return ResponseEntity.ok(userService.getById(userId));
    }
}
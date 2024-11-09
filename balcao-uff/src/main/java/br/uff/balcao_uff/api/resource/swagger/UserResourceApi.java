package br.uff.balcao_uff.api.resource.swagger;

import java.util.List;

import org.springframework.http.ResponseEntity;

import br.uff.balcao_uff.api.dto.request.UserRequestDTO;
import br.uff.balcao_uff.api.dto.response.UserResponseDTO;
import io.swagger.v3.oas.annotations.Operation;

public interface UserResourceApi {
	
	@Operation(summary = "Create a new user")
    public ResponseEntity<UserResponseDTO> save(UserRequestDTO userRequestDTO);

    @Operation(summary = "Get all users")
    public ResponseEntity<List<UserResponseDTO>> findAll();

    @Operation(summary = "Get user by ID")
    public ResponseEntity<UserResponseDTO> findById(Long id);

    @Operation(summary = "Delete a user")
    public ResponseEntity<Void> delete(Long id);
}

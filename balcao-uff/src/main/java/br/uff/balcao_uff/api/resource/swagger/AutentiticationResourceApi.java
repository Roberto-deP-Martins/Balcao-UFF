package br.uff.balcao_uff.api.resource.swagger;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;

import br.uff.balcao_uff.api.dto.AuthenticationDTO;
import br.uff.balcao_uff.api.dto.request.UserRequestDTO;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;

public interface AutentiticationResourceApi {

	@Operation(summary = "Login")
	public ResponseEntity login(AuthenticationDTO data);

	@Operation(summary = "Register user")
	public ResponseEntity register(@RequestBody @Valid UserRequestDTO data);
}

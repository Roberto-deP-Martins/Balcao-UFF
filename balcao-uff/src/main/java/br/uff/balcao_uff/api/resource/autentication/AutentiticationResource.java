package br.uff.balcao_uff.api.resource.autentication;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.uff.balcao_uff.api.dto.AuthenticationDTO;
import br.uff.balcao_uff.api.dto.request.UserRequestDTO;
import br.uff.balcao_uff.api.dto.response.LoginResponseDTO;
import br.uff.balcao_uff.api.resource.swagger.AutentiticationResourceApi;
import br.uff.balcao_uff.configuration.security.TokenService;
import br.uff.balcao_uff.entity.UserEntity;
import br.uff.balcao_uff.repository.UserRepository;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;


@RestController
@RequestMapping("/auth")
@Tag(name = "Autenticação", description = "Autenticação e criação de usuário")
public class AutentiticationResource implements AutentiticationResourceApi{
	
	@Autowired
	private AuthenticationManager authenticationManager;
	
	@Autowired
	private UserRepository repository;
	
	@Autowired
	TokenService tokenService;

	@PostMapping("/login")
	public ResponseEntity  login(@RequestBody @Valid AuthenticationDTO data) {
		var usernamePassword = new UsernamePasswordAuthenticationToken(data.cpf(), data.password());
		var auth = this.authenticationManager.authenticate(usernamePassword);
		
		var token = tokenService.generateToken((UserEntity)auth.getPrincipal());
		return ResponseEntity.ok(new LoginResponseDTO(token));
	}
	
	@PostMapping("/register")
	public ResponseEntity register(@RequestBody @Valid UserRequestDTO data) {
		if(this.repository.findByCpf(data.cpf()) != null) {
			return ResponseEntity.badRequest().build();
		}
		String encryptedPassword = new BCryptPasswordEncoder().encode(data.password());
		UserEntity newUser = new UserEntity(data.name(), data.email(), encryptedPassword, data.cpf(), data.role());
		
		this.repository.save(newUser);
		return ResponseEntity.ok().build();
		
	}
}

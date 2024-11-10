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
import br.uff.balcao_uff.entity.UserEntity;
import br.uff.balcao_uff.repository.UserRepository;
import jakarta.validation.Valid;


@RestController
@RequestMapping("/auth")
public class AutentiticationResource {
	
	@Autowired
	private AuthenticationManager authenticationManager;
	
	@Autowired
	private UserRepository repository;

	@PostMapping("/login")
	public ResponseEntity  login(@RequestBody @Valid AuthenticationDTO data) {
		var usernamePassword = new UsernamePasswordAuthenticationToken(data.cpf(), data.password());
		var auth = this.authenticationManager.authenticate(usernamePassword);
		return ResponseEntity.ok().build();
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

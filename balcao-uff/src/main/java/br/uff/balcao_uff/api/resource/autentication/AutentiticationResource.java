package br.uff.balcao_uff.api.resource.autentication;

import java.util.Collections;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.json.gson.GsonFactory;

import br.uff.balcao_uff.api.dto.AuthenticationDTO;
import br.uff.balcao_uff.api.dto.request.UserRequestDTO;
import br.uff.balcao_uff.api.dto.response.LoginResponseDTO;
import br.uff.balcao_uff.api.resource.swagger.AutentiticationResourceApi;
import br.uff.balcao_uff.commons.util.enums.UserRole;
import br.uff.balcao_uff.configuration.security.SecurityConfigurations;
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
    private TokenService tokenService;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody @Valid AuthenticationDTO data) {
        var usernamePassword = new UsernamePasswordAuthenticationToken(data.cpf(), data.password());
        var auth = this.authenticationManager.authenticate(usernamePassword);
        
        var token = tokenService.generateToken((UserEntity)auth.getPrincipal());
        return ResponseEntity.ok(new LoginResponseDTO(token));
    }
    
    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody @Valid UserRequestDTO data) {
        Logger logger = LoggerFactory.getLogger(SecurityConfigurations.class);

        try {
            if (this.repository.findByCpf(data.cpf()) != null) {
                logger.error("User with CPF " + data.cpf() + " already exists.");
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("User with this CPF already exists.");
            }

            String encryptedPassword = new BCryptPasswordEncoder().encode(data.password());
            UserEntity newUser = new UserEntity(data.name(), data.email(), encryptedPassword, data.cpf(), data.role());

            this.repository.save(newUser);

            logger.info("User registered successfully with CPF " + data.cpf());
            return ResponseEntity.ok("User registered successfully.");
        } catch (Exception e) {
            logger.error("Error occurred while registering user: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred while registering the user.");
        }
    }
    
    @PostMapping("/google-login")
    public ResponseEntity<?> googleLogin(@RequestBody Map<String, String> request) {
        try {
            String token = request.get("token");

            GoogleIdTokenVerifier verifier = new GoogleIdTokenVerifier.Builder(
                    GoogleNetHttpTransport.newTrustedTransport(), GsonFactory.getDefaultInstance())
                    .setAudience(Collections.singletonList("1080697336488-5ufvcf9p11nvem01u8d9glmg8s98iab4.apps.googleusercontent.com"))
                    .build();

            GoogleIdToken idToken = verifier.verify(token);
            if (idToken != null) {
                GoogleIdToken.Payload payload = idToken.getPayload();
                String email = payload.getEmail();
                String name = (String) payload.get("name");

                String role = "USER";
                if ("jhonatansilva@id.uff.br".equals(email)) {
                    role = "ADMIN";
                }

                String cpf = email.split("@")[0];

                UserEntity existingUser = this.repository.findByEmail(email);
                if (existingUser != null) {
                    String jwt = tokenService.generateToken(existingUser);
                    return ResponseEntity.ok(new LoginResponseDTO(jwt));
                }

                UserEntity googleUser = new UserEntity(name, email, cpf, cpf, UserRole.valueOf(role));
                this.repository.save(googleUser);

                String jwt = tokenService.generateToken(googleUser);

                return ResponseEntity.ok(new LoginResponseDTO(jwt));
            } else {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid Google token.");
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error validating Google token.");
        }
    }

}

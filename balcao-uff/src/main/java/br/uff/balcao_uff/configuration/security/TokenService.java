package br.uff.balcao_uff.configuration.security;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTVerificationException;

import br.uff.balcao_uff.entity.UserEntity;

@Service
public class TokenService {

    @Value("${api.security.token.secret}")
    private String secret;

    /**
     * Gera um token JWT para o usuário fornecido. O subject pode ser o e-mail
     * (para autenticação via Google) ou o CPF (para autenticação via CPF e senha).
     */
    public String generateToken(UserEntity user) {
        try {
            Algorithm algorithm = Algorithm.HMAC256(secret);
            
            // Verifica se o usuário tem um e-mail (significa que é um login via Google)
            String subject = (user.getEmail() != null) ? user.getEmail() : user.getCpf();
            
            return JWT.create()
                    .withIssuer("auth-api")
                    .withSubject(subject)  // Usando email ou CPF como subject
                    .withClaim("role", user.getRole().name()) // Adiciona a role no token
                    .withExpiresAt(genExpirationDate())
                    .sign(algorithm);
        } catch (JWTCreationException exception) {
            throw new RuntimeException("Erro ao gerar o token", exception);
        }
    }

    /**
     * Valida o token JWT e retorna o CPF ou e-mail (subject) se for válido.
     * O subject será utilizado para determinar se o login foi feito via Google (e-mail)
     * ou via CPF.
     */
    public String validateToken(String token) {
        try {
            Algorithm algorithm = Algorithm.HMAC256(secret);
            return JWT.require(algorithm)
                    .withIssuer("auth-api")
                    .build()
                    .verify(token)
                    .getSubject();
        } catch (JWTVerificationException exception) {
            throw new RuntimeException("Token inválido ou expirado", exception);
        }
    }

    /**
     * Gera a data de expiração para o token (1 hora a partir do momento atual).
     */
    private Instant genExpirationDate() {
        return LocalDateTime.now().plusHours(1).toInstant(ZoneOffset.of("-03:00"));
    }
}

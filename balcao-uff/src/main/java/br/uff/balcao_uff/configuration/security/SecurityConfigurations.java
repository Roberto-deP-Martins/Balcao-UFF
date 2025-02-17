package br.uff.balcao_uff.configuration.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@EnableWebSecurity
public class SecurityConfigurations {

    @Autowired
    SecurityFilter securityFilter;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
        return httpSecurity
                .cors()
                .and()
                .csrf(csrf -> csrf.disable())
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(authorize -> authorize
                        /**
                         * Autenticação
                         */
                        .requestMatchers(HttpMethod.POST, "/auth/login").permitAll()
                        .requestMatchers(HttpMethod.POST, "/auth/register").permitAll()
                        .requestMatchers(HttpMethod.POST, "/auth/google-login").permitAll()
                        .requestMatchers(HttpMethod.GET, "/auth/current-user").authenticated()

                        /**
                         * Anúncios
                         */
                        .requestMatchers(HttpMethod.POST, "/anuncios").authenticated()
                        .requestMatchers(HttpMethod.GET, "/anuncios").authenticated()
                        .requestMatchers(HttpMethod.POST, "/anuncios/category").authenticated()
                        .requestMatchers(HttpMethod.POST, "/anuncios/delete").authenticated()
                        .requestMatchers(HttpMethod.GET, "/anuncioImages/image/**").permitAll()

                        /**
                         * Reviews
                         */
                        .requestMatchers(HttpMethod.POST, "/reviews/**").authenticated()
                        .requestMatchers(HttpMethod.GET, "/reviews/user/**").authenticated()

                        /**
                         * Perfil Usuário
                         */
                        .requestMatchers(HttpMethod.POST, "/users/profile/**").authenticated()

                        /**
                         * Mensagens e conversas
                         */
                        .requestMatchers(HttpMethod.POST, "/messages/**").authenticated()
                        .requestMatchers(HttpMethod.POST, "/conversas/**").authenticated()
                        .requestMatchers(HttpMethod.POST, "/conversas/por-anuncio/**").authenticated()


                        /**
                         * Swagger
                         */
                        .requestMatchers("/swagger-ui/**").permitAll()
                        .requestMatchers("/v3/api-docs/**").permitAll()
                        .anyRequest().authenticated()
                )
                .addFilterBefore(securityFilter, UsernamePasswordAuthenticationFilter.class)
                .build();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
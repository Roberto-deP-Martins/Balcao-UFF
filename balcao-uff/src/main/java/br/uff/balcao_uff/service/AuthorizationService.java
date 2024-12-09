package br.uff.balcao_uff.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import br.uff.balcao_uff.entity.UserEntity;
import br.uff.balcao_uff.repository.UserRepository;

@Service
public class AuthorizationService implements UserDetailsService {

	@Autowired
	UserRepository repository;

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		return repository.findByCpf(username);
	}
	
	/**
     * Obtém o usuário autenticado com base no contexto de segurança.
     *
     * @return o usuário autenticado como um objeto UserEntity.
     * @throws UsernameNotFoundException se o usuário autenticado não for encontrado no banco de dados.
     */
	public UserEntity getAuthenticatedUser() {
	    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
	    if (authentication != null && authentication.isAuthenticated()) {
	        Object principal = authentication.getPrincipal();
	        if (principal instanceof UserEntity) {
	            return (UserEntity) principal;
	        }
	    }
	    throw new RuntimeException("Usuário não autenticado.");
	}


}

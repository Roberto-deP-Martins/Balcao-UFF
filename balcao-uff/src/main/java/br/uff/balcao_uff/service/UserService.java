package br.uff.balcao_uff.service;

import org.springframework.stereotype.Service;

import br.uff.balcao_uff.repository.UserRepository;
import lombok.AllArgsConstructor;

@AllArgsConstructor
@Service
public class UserService {
	private final UserRepository userRepository;

}

package br.uff.balcao_uff.service;

import br.uff.balcao_uff.api.dto.response.UserResponseDTO;
import br.uff.balcao_uff.commons.util.exceptions.UserNotFoundException;
import br.uff.balcao_uff.entity.UserEntity;
import br.uff.balcao_uff.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@AllArgsConstructor
@Service
public class UserService {
	private final UserRepository userRepository;

	public List<UserResponseDTO> getAll() {
		return userRepository.findAll().stream()
				.map(this::userToDto)
				.toList();
	}

	public UserResponseDTO getById(Long id) {
		UserEntity user = userRepository.findById(id)
				.orElseThrow(() -> new UserNotFoundException("Usuário não encontrado com ID: " + id));
		return userToDto(user);
	}

	private UserResponseDTO userToDto(UserEntity user) {
		return UserResponseDTO.builder()
				.id(user.getId())
				.name(user.getName())
				.email(user.getEmail())
				.role(String.valueOf(user.getRole()))
				.build();
	}
}

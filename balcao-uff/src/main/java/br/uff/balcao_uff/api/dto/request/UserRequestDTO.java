package br.uff.balcao_uff.api.dto.request;

import br.uff.balcao_uff.commons.util.enums.UserRole;

public record UserRequestDTO(String name, 
		String email, 
		String password, 
		String cpf, 
		UserRole role) {
}
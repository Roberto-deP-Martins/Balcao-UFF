package br.uff.balcao_uff.entity;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.fasterxml.jackson.annotation.JsonProperty;

import br.uff.balcao_uff.commons.util.enums.UserRole;
import br.uff.balcao_uff.commons.util.enums.UserRoleConverter;
import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "tb_users")
public class UserEntity implements Serializable, UserDetails {

	private static final long serialVersionUID = -2945116090533031537L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@JsonProperty(value = "name")
	private String name;

	@JsonProperty(value = "email")
	private String email;

	@JsonProperty(value = "password")
	private String password;

	@JsonProperty(value = "cpf")
	private String cpf;

	 @Column(nullable = false) 
	 @Convert(converter = UserRoleConverter.class) 
	 @JsonProperty(value = "role")
	private UserRole role;

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {

		if (this.role == UserRole.ADMIN) {
			return List.of(new SimpleGrantedAuthority("ROLE_ADMIN"), new SimpleGrantedAuthority("ROLE_USER"));
		} else {
			return List.of(new SimpleGrantedAuthority("ROLE_USER"));
		}
	}

	@Override
	public String getUsername() {
		return cpf;
	}

	public UserEntity(String name, String email, String password, String cpf, UserRole role) {
		super();
		this.name = name;
		this.email = email;
		this.password = password;
		this.cpf = cpf;
		this.role = role;
	}
	
	

}

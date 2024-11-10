package br.uff.balcao_uff.entity;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonProperty;

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
@Table(name = "tb_user")
public class UserEntity implements Serializable {

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
	
	@JsonProperty(value = "role")
	private String role;

}

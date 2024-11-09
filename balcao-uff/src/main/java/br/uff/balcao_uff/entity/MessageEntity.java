package br.uff.balcao_uff.entity;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
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
@Table(name = "tb_message")
public class MessageEntity implements Serializable {

	private static final long serialVersionUID = -2369341048098582768L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@JsonProperty(value = "content")
	private String content;
	
	@JsonProperty(value = "isRead")
	private boolean isRead;

	@ManyToOne
	@JoinColumn(name = "sender_id", referencedColumnName = "id")
	private UserEntity sender;

	@ManyToOne
	@JoinColumn(name = "receiver_id", referencedColumnName = "id")
	private UserEntity receiver;

	@ManyToOne
	@JoinColumn(name = "anuncio_id", referencedColumnName = "id")
	private AnuncioEntity anuncio;
}

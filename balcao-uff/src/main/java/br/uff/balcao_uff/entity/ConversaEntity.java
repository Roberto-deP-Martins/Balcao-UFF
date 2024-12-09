package br.uff.balcao_uff.entity;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
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
@Table(name = "tb_conversa")
public class ConversaEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @JsonProperty(value="data_criacao")
    private LocalDateTime data_criacao;

    @ManyToOne
    @JoinColumn(name = "anuncio_id", referencedColumnName = "id", nullable = false)
    private AnuncioEntity anuncio;

    @ManyToOne
    @JoinColumn(name = "interessado_id", referencedColumnName = "id", nullable = false)
    private UserEntity interessado;

    @ManyToOne
    @JoinColumn(name = "anunciante_id", referencedColumnName = "id", nullable = false)
    private UserEntity anunciante;

    @OneToMany(mappedBy = "conversa")
    private List<MessageEntity> messages;
}


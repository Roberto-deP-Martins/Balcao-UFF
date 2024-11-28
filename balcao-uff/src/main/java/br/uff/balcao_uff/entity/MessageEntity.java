package br.uff.balcao_uff.entity;

import java.io.Serializable;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.persistence.*;

import lombok.*;

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
    @Column(nullable = false)
    private String content;

    @JsonProperty(value = "isRead")
    @Column(nullable = false)
    private boolean isRead = false;

    @ManyToOne
    @JoinColumn(name = "sender_id", referencedColumnName = "id", nullable = false)
    private UserEntity sender;

    @ManyToOne
    @JoinColumn(name = "conversa_id", referencedColumnName = "id", nullable = false)
    private ConversaEntity conversa;

    @Column(name = "dt_envio", nullable = false)
    private Date dtEnvio;

    @PrePersist
    protected void onSend() {
        if (this.dtEnvio == null) {
            this.dtEnvio = new Date();
        }
    }
}

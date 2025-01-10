package br.uff.balcao_uff.entity;

import java.io.Serializable;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
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
@Table(name = "tb_transacoes")
public class TransacaoEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "anuncio_id", referencedColumnName = "id", nullable = false)
    @JsonProperty("anuncio")
    private AnuncioEntity anuncio;

    @ManyToOne
    @JoinColumn(name = "vendedor_id", referencedColumnName = "id", nullable = false)
    @JsonProperty("vendedor")
    private UserEntity vendedor;

    @ManyToOne
    @JoinColumn(name = "comprador_id", referencedColumnName = "id", nullable = false)
    @JsonProperty("comprador")
    private UserEntity comprador;

    @Column(name = "data_conclusao", nullable = false)
    @JsonProperty("data_conclusao")
    private Date dataConclusao;

    @Column(name = "vendedor_avaliou", nullable = false)
    @JsonProperty("vendedor_avaliou")
    private boolean vendedorAvaliou;

    @Column(name = "comprador_avaliou", nullable = false)
    @JsonProperty("comprador_avaliou")
    private boolean compradorAvaliou;

    @PrePersist
    protected void onCreate() {
        this.dataConclusao = new Date();
    }
}

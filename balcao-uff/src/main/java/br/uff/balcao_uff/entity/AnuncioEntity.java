package br.uff.balcao_uff.entity;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

import br.uff.balcao_uff.commons.util.interfaces.IDeleteLogico;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreRemove;
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
@Table(name = "tb_anuncio")
public class AnuncioEntity implements Serializable, IDeleteLogico {

    private static final long serialVersionUID = -140827022243065844L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JsonProperty(value = "title")
    private String title;

    @JsonProperty(value = "description")
    private String description;

    @JsonProperty(value = "category")
    private String category;

    @JsonProperty(value = "price")
    private double price;

    @JsonProperty(value = "contact_info")
    @Column(name = "contact_info")
    private String contactInfo;

    @ManyToOne
    @JoinColumn(name = "location_id", referencedColumnName = "id")
    private LocationEntity location;

    @Column(name = "dt_criacao")
    private Date dtCriacao;

    @PrePersist
    protected void onCreate() {
        this.dtCriacao = new Date();
        this.dtDelete = null;
    }

    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private UserEntity user;

    @OneToMany(mappedBy = "anuncio", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<AnuncioImageEntity> images;

    @Column(name = "dt_Delete")
    private Date dtDelete;

    @Override
    public Date getDtDelete() {
        return dtDelete;
    }

    @Override
    public void setDtDelete(Date dtDelete) {
        this.dtDelete = dtDelete;
    }

    @PreRemove
    protected void onDelete() {
        this.dtDelete = new Date();
    }

}

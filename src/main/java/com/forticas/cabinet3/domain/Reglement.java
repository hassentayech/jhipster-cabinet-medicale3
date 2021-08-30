package com.forticas.cabinet3.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.forticas.cabinet3.domain.enumeration.TypePayement;
import java.io.Serializable;
import java.time.ZonedDateTime;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Reglement.
 */
@Entity
@Table(name = "reglement")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Reglement implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @NotNull
    @Column(name = "date", nullable = false)
    private ZonedDateTime date;

    @NotNull
    @Column(name = "valeur", nullable = false)
    private Integer valeur;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "type_payement", nullable = false)
    private TypePayement typePayement;

    @Column(name = "remarque")
    private String remarque;

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties(value = { "certificat", "crEchographie", "ordonnances", "reglements", "casTraiter" }, allowSetters = true)
    private Visite visite;

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Reglement id(Long id) {
        this.id = id;
        return this;
    }

    public ZonedDateTime getDate() {
        return this.date;
    }

    public Reglement date(ZonedDateTime date) {
        this.date = date;
        return this;
    }

    public void setDate(ZonedDateTime date) {
        this.date = date;
    }

    public Integer getValeur() {
        return this.valeur;
    }

    public Reglement valeur(Integer valeur) {
        this.valeur = valeur;
        return this;
    }

    public void setValeur(Integer valeur) {
        this.valeur = valeur;
    }

    public TypePayement getTypePayement() {
        return this.typePayement;
    }

    public Reglement typePayement(TypePayement typePayement) {
        this.typePayement = typePayement;
        return this;
    }

    public void setTypePayement(TypePayement typePayement) {
        this.typePayement = typePayement;
    }

    public String getRemarque() {
        return this.remarque;
    }

    public Reglement remarque(String remarque) {
        this.remarque = remarque;
        return this;
    }

    public void setRemarque(String remarque) {
        this.remarque = remarque;
    }

    public Visite getVisite() {
        return this.visite;
    }

    public Reglement visite(Visite visite) {
        this.setVisite(visite);
        return this;
    }

    public void setVisite(Visite visite) {
        this.visite = visite;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Reglement)) {
            return false;
        }
        return id != null && id.equals(((Reglement) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Reglement{" +
            "id=" + getId() +
            ", date='" + getDate() + "'" +
            ", valeur=" + getValeur() +
            ", typePayement='" + getTypePayement() + "'" +
            ", remarque='" + getRemarque() + "'" +
            "}";
    }
}

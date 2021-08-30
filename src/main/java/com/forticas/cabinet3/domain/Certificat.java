package com.forticas.cabinet3.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Certificat.
 */
@Entity
@Table(name = "certificat")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Certificat implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @NotNull
    @Column(name = "nbr_jours", nullable = false)
    private Integer nbrJours;

    @Column(name = "description")
    private String description;

    @JsonIgnoreProperties(value = { "certificat", "crEchographie", "ordonnances", "reglements", "casTraiter" }, allowSetters = true)
    @OneToOne(mappedBy = "certificat")
    private Visite visite;

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Certificat id(Long id) {
        this.id = id;
        return this;
    }

    public Integer getNbrJours() {
        return this.nbrJours;
    }

    public Certificat nbrJours(Integer nbrJours) {
        this.nbrJours = nbrJours;
        return this;
    }

    public void setNbrJours(Integer nbrJours) {
        this.nbrJours = nbrJours;
    }

    public String getDescription() {
        return this.description;
    }

    public Certificat description(String description) {
        this.description = description;
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Visite getVisite() {
        return this.visite;
    }

    public Certificat visite(Visite visite) {
        this.setVisite(visite);
        return this;
    }

    public void setVisite(Visite visite) {
        if (this.visite != null) {
            this.visite.setCertificat(null);
        }
        if (visite != null) {
            visite.setCertificat(this);
        }
        this.visite = visite;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Certificat)) {
            return false;
        }
        return id != null && id.equals(((Certificat) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Certificat{" +
            "id=" + getId() +
            ", nbrJours=" + getNbrJours() +
            ", description='" + getDescription() + "'" +
            "}";
    }
}

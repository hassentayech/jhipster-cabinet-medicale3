package com.forticas.cabinet3.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Ordonnance.
 */
@Entity
@Table(name = "ordonnance")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Ordonnance implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @OneToMany(mappedBy = "ordonnance")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "ordonnance" }, allowSetters = true)
    private Set<Prescription> prescriptions = new HashSet<>();

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

    public Ordonnance id(Long id) {
        this.id = id;
        return this;
    }

    public Set<Prescription> getPrescriptions() {
        return this.prescriptions;
    }

    public Ordonnance prescriptions(Set<Prescription> prescriptions) {
        this.setPrescriptions(prescriptions);
        return this;
    }

    public Ordonnance addPrescription(Prescription prescription) {
        this.prescriptions.add(prescription);
        prescription.setOrdonnance(this);
        return this;
    }

    public Ordonnance removePrescription(Prescription prescription) {
        this.prescriptions.remove(prescription);
        prescription.setOrdonnance(null);
        return this;
    }

    public void setPrescriptions(Set<Prescription> prescriptions) {
        if (this.prescriptions != null) {
            this.prescriptions.forEach(i -> i.setOrdonnance(null));
        }
        if (prescriptions != null) {
            prescriptions.forEach(i -> i.setOrdonnance(this));
        }
        this.prescriptions = prescriptions;
    }

    public Visite getVisite() {
        return this.visite;
    }

    public Ordonnance visite(Visite visite) {
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
        if (!(o instanceof Ordonnance)) {
            return false;
        }
        return id != null && id.equals(((Ordonnance) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Ordonnance{" +
            "id=" + getId() +
            "}";
    }
}

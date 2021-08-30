package com.forticas.cabinet3.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Prescription.
 */
@Entity
@Table(name = "prescription")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Prescription implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @NotNull
    @Column(name = "prescription", nullable = false)
    private String prescription;

    @Column(name = "prise")
    private String prise;

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties(value = { "prescriptions", "visite" }, allowSetters = true)
    private Ordonnance ordonnance;

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Prescription id(Long id) {
        this.id = id;
        return this;
    }

    public String getPrescription() {
        return this.prescription;
    }

    public Prescription prescription(String prescription) {
        this.prescription = prescription;
        return this;
    }

    public void setPrescription(String prescription) {
        this.prescription = prescription;
    }

    public String getPrise() {
        return this.prise;
    }

    public Prescription prise(String prise) {
        this.prise = prise;
        return this;
    }

    public void setPrise(String prise) {
        this.prise = prise;
    }

    public Ordonnance getOrdonnance() {
        return this.ordonnance;
    }

    public Prescription ordonnance(Ordonnance ordonnance) {
        this.setOrdonnance(ordonnance);
        return this;
    }

    public void setOrdonnance(Ordonnance ordonnance) {
        this.ordonnance = ordonnance;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Prescription)) {
            return false;
        }
        return id != null && id.equals(((Prescription) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Prescription{" +
            "id=" + getId() +
            ", prescription='" + getPrescription() + "'" +
            ", prise='" + getPrise() + "'" +
            "}";
    }
}

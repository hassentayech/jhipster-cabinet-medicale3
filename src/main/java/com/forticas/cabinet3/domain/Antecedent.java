package com.forticas.cabinet3.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.forticas.cabinet3.domain.enumeration.TypeAntecedent;
import java.io.Serializable;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Type;

/**
 * A Antecedent.
 */
@Entity
@Table(name = "antecedent")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Antecedent implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "type", nullable = false)
    private TypeAntecedent type;

    @NotNull
    @Column(name = "periode", nullable = false)
    private String periode;

    @Lob
    @Type(type = "org.hibernate.type.TextType")
    @Column(name = "antecedent", nullable = false)
    private String antecedent;

    @Lob
    @Type(type = "org.hibernate.type.TextType")
    @Column(name = "traitement")
    private String traitement;

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties(value = { "rendezVous", "casTraiters", "constantes", "antecedents" }, allowSetters = true)
    private Patient patient;

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Antecedent id(Long id) {
        this.id = id;
        return this;
    }

    public TypeAntecedent getType() {
        return this.type;
    }

    public Antecedent type(TypeAntecedent type) {
        this.type = type;
        return this;
    }

    public void setType(TypeAntecedent type) {
        this.type = type;
    }

    public String getPeriode() {
        return this.periode;
    }

    public Antecedent periode(String periode) {
        this.periode = periode;
        return this;
    }

    public void setPeriode(String periode) {
        this.periode = periode;
    }

    public String getAntecedent() {
        return this.antecedent;
    }

    public Antecedent antecedent(String antecedent) {
        this.antecedent = antecedent;
        return this;
    }

    public void setAntecedent(String antecedent) {
        this.antecedent = antecedent;
    }

    public String getTraitement() {
        return this.traitement;
    }

    public Antecedent traitement(String traitement) {
        this.traitement = traitement;
        return this;
    }

    public void setTraitement(String traitement) {
        this.traitement = traitement;
    }

    public Patient getPatient() {
        return this.patient;
    }

    public Antecedent patient(Patient patient) {
        this.setPatient(patient);
        return this;
    }

    public void setPatient(Patient patient) {
        this.patient = patient;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Antecedent)) {
            return false;
        }
        return id != null && id.equals(((Antecedent) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Antecedent{" +
            "id=" + getId() +
            ", type='" + getType() + "'" +
            ", periode='" + getPeriode() + "'" +
            ", antecedent='" + getAntecedent() + "'" +
            ", traitement='" + getTraitement() + "'" +
            "}";
    }
}

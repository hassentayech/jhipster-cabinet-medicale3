package com.forticas.cabinet3.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.time.LocalDate;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A RendezVous.
 */
@Entity
@Table(name = "rendez_vous")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class RendezVous implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @NotNull
    @Column(name = "date", nullable = false)
    private LocalDate date;

    @NotNull
    @Column(name = "tranche_horaire", nullable = false)
    private String trancheHoraire;

    @NotNull
    @Column(name = "nbr_tranche", nullable = false)
    private String nbrTranche;

    @Column(name = "motif")
    private String motif;

    @Column(name = "present")
    private Boolean present;

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

    public RendezVous id(Long id) {
        this.id = id;
        return this;
    }

    public LocalDate getDate() {
        return this.date;
    }

    public RendezVous date(LocalDate date) {
        this.date = date;
        return this;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public String getTrancheHoraire() {
        return this.trancheHoraire;
    }

    public RendezVous trancheHoraire(String trancheHoraire) {
        this.trancheHoraire = trancheHoraire;
        return this;
    }

    public void setTrancheHoraire(String trancheHoraire) {
        this.trancheHoraire = trancheHoraire;
    }

    public String getNbrTranche() {
        return this.nbrTranche;
    }

    public RendezVous nbrTranche(String nbrTranche) {
        this.nbrTranche = nbrTranche;
        return this;
    }

    public void setNbrTranche(String nbrTranche) {
        this.nbrTranche = nbrTranche;
    }

    public String getMotif() {
        return this.motif;
    }

    public RendezVous motif(String motif) {
        this.motif = motif;
        return this;
    }

    public void setMotif(String motif) {
        this.motif = motif;
    }

    public Boolean getPresent() {
        return this.present;
    }

    public RendezVous present(Boolean present) {
        this.present = present;
        return this;
    }

    public void setPresent(Boolean present) {
        this.present = present;
    }

    public Patient getPatient() {
        return this.patient;
    }

    public RendezVous patient(Patient patient) {
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
        if (!(o instanceof RendezVous)) {
            return false;
        }
        return id != null && id.equals(((RendezVous) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "RendezVous{" +
            "id=" + getId() +
            ", date='" + getDate() + "'" +
            ", trancheHoraire='" + getTrancheHoraire() + "'" +
            ", nbrTranche='" + getNbrTranche() + "'" +
            ", motif='" + getMotif() + "'" +
            ", present='" + getPresent() + "'" +
            "}";
    }
}

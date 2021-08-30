package com.forticas.cabinet3.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.forticas.cabinet3.domain.enumeration.EchoAspect;
import java.io.Serializable;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A CrEchographie.
 */
@Entity
@Table(name = "cr_echographie")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class CrEchographie implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(name = "aspect_foie")
    private EchoAspect aspectFoie;

    @Column(name = "observation_foie")
    private String observationFoie;

    @Enumerated(EnumType.STRING)
    @Column(name = "aspect_vesicule")
    private EchoAspect aspectVesicule;

    @Column(name = "observation_vesicule")
    private String observationVesicule;

    @Enumerated(EnumType.STRING)
    @Column(name = "aspect_troc_voie_veine")
    private EchoAspect aspectTrocVoieVeine;

    @Column(name = "observation_troc_voie_veine")
    private String observationTrocVoieVeine;

    @Enumerated(EnumType.STRING)
    @Column(name = "aspect_reins")
    private EchoAspect aspectReins;

    @Column(name = "observation_reins")
    private String observationReins;

    @Enumerated(EnumType.STRING)
    @Column(name = "aspect_rate")
    private EchoAspect aspectRate;

    @Column(name = "observation_rate")
    private String observationRate;

    @Enumerated(EnumType.STRING)
    @Column(name = "aspect_pancreas")
    private EchoAspect aspectPancreas;

    @Column(name = "observation_pancreas")
    private String observationPancreas;

    @Column(name = "autre_observation")
    private String autreObservation;

    @JsonIgnoreProperties(value = { "certificat", "crEchographie", "ordonnances", "reglements", "casTraiter" }, allowSetters = true)
    @OneToOne(mappedBy = "crEchographie")
    private Visite visite;

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public CrEchographie id(Long id) {
        this.id = id;
        return this;
    }

    public EchoAspect getAspectFoie() {
        return this.aspectFoie;
    }

    public CrEchographie aspectFoie(EchoAspect aspectFoie) {
        this.aspectFoie = aspectFoie;
        return this;
    }

    public void setAspectFoie(EchoAspect aspectFoie) {
        this.aspectFoie = aspectFoie;
    }

    public String getObservationFoie() {
        return this.observationFoie;
    }

    public CrEchographie observationFoie(String observationFoie) {
        this.observationFoie = observationFoie;
        return this;
    }

    public void setObservationFoie(String observationFoie) {
        this.observationFoie = observationFoie;
    }

    public EchoAspect getAspectVesicule() {
        return this.aspectVesicule;
    }

    public CrEchographie aspectVesicule(EchoAspect aspectVesicule) {
        this.aspectVesicule = aspectVesicule;
        return this;
    }

    public void setAspectVesicule(EchoAspect aspectVesicule) {
        this.aspectVesicule = aspectVesicule;
    }

    public String getObservationVesicule() {
        return this.observationVesicule;
    }

    public CrEchographie observationVesicule(String observationVesicule) {
        this.observationVesicule = observationVesicule;
        return this;
    }

    public void setObservationVesicule(String observationVesicule) {
        this.observationVesicule = observationVesicule;
    }

    public EchoAspect getAspectTrocVoieVeine() {
        return this.aspectTrocVoieVeine;
    }

    public CrEchographie aspectTrocVoieVeine(EchoAspect aspectTrocVoieVeine) {
        this.aspectTrocVoieVeine = aspectTrocVoieVeine;
        return this;
    }

    public void setAspectTrocVoieVeine(EchoAspect aspectTrocVoieVeine) {
        this.aspectTrocVoieVeine = aspectTrocVoieVeine;
    }

    public String getObservationTrocVoieVeine() {
        return this.observationTrocVoieVeine;
    }

    public CrEchographie observationTrocVoieVeine(String observationTrocVoieVeine) {
        this.observationTrocVoieVeine = observationTrocVoieVeine;
        return this;
    }

    public void setObservationTrocVoieVeine(String observationTrocVoieVeine) {
        this.observationTrocVoieVeine = observationTrocVoieVeine;
    }

    public EchoAspect getAspectReins() {
        return this.aspectReins;
    }

    public CrEchographie aspectReins(EchoAspect aspectReins) {
        this.aspectReins = aspectReins;
        return this;
    }

    public void setAspectReins(EchoAspect aspectReins) {
        this.aspectReins = aspectReins;
    }

    public String getObservationReins() {
        return this.observationReins;
    }

    public CrEchographie observationReins(String observationReins) {
        this.observationReins = observationReins;
        return this;
    }

    public void setObservationReins(String observationReins) {
        this.observationReins = observationReins;
    }

    public EchoAspect getAspectRate() {
        return this.aspectRate;
    }

    public CrEchographie aspectRate(EchoAspect aspectRate) {
        this.aspectRate = aspectRate;
        return this;
    }

    public void setAspectRate(EchoAspect aspectRate) {
        this.aspectRate = aspectRate;
    }

    public String getObservationRate() {
        return this.observationRate;
    }

    public CrEchographie observationRate(String observationRate) {
        this.observationRate = observationRate;
        return this;
    }

    public void setObservationRate(String observationRate) {
        this.observationRate = observationRate;
    }

    public EchoAspect getAspectPancreas() {
        return this.aspectPancreas;
    }

    public CrEchographie aspectPancreas(EchoAspect aspectPancreas) {
        this.aspectPancreas = aspectPancreas;
        return this;
    }

    public void setAspectPancreas(EchoAspect aspectPancreas) {
        this.aspectPancreas = aspectPancreas;
    }

    public String getObservationPancreas() {
        return this.observationPancreas;
    }

    public CrEchographie observationPancreas(String observationPancreas) {
        this.observationPancreas = observationPancreas;
        return this;
    }

    public void setObservationPancreas(String observationPancreas) {
        this.observationPancreas = observationPancreas;
    }

    public String getAutreObservation() {
        return this.autreObservation;
    }

    public CrEchographie autreObservation(String autreObservation) {
        this.autreObservation = autreObservation;
        return this;
    }

    public void setAutreObservation(String autreObservation) {
        this.autreObservation = autreObservation;
    }

    public Visite getVisite() {
        return this.visite;
    }

    public CrEchographie visite(Visite visite) {
        this.setVisite(visite);
        return this;
    }

    public void setVisite(Visite visite) {
        if (this.visite != null) {
            this.visite.setCrEchographie(null);
        }
        if (visite != null) {
            visite.setCrEchographie(this);
        }
        this.visite = visite;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof CrEchographie)) {
            return false;
        }
        return id != null && id.equals(((CrEchographie) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "CrEchographie{" +
            "id=" + getId() +
            ", aspectFoie='" + getAspectFoie() + "'" +
            ", observationFoie='" + getObservationFoie() + "'" +
            ", aspectVesicule='" + getAspectVesicule() + "'" +
            ", observationVesicule='" + getObservationVesicule() + "'" +
            ", aspectTrocVoieVeine='" + getAspectTrocVoieVeine() + "'" +
            ", observationTrocVoieVeine='" + getObservationTrocVoieVeine() + "'" +
            ", aspectReins='" + getAspectReins() + "'" +
            ", observationReins='" + getObservationReins() + "'" +
            ", aspectRate='" + getAspectRate() + "'" +
            ", observationRate='" + getObservationRate() + "'" +
            ", aspectPancreas='" + getAspectPancreas() + "'" +
            ", observationPancreas='" + getObservationPancreas() + "'" +
            ", autreObservation='" + getAutreObservation() + "'" +
            "}";
    }
}

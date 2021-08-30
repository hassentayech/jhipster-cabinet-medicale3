package com.forticas.cabinet3.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.forticas.cabinet3.domain.enumeration.EtatActuel;
import com.forticas.cabinet3.domain.enumeration.ModeFacturation;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Type;

/**
 * A CasTraiter.
 */
@Entity
@Table(name = "cas_traiter")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class CasTraiter implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @NotNull
    @Column(name = "cas", nullable = false)
    private String cas;

    @NotNull
    @Column(name = "depuis", nullable = false)
    private LocalDate depuis;

    @Lob
    @Type(type = "org.hibernate.type.TextType")
    @Column(name = "histoire")
    private String histoire;

    @Lob
    @Type(type = "org.hibernate.type.TextType")
    @Column(name = "remarques")
    private String remarques;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "etat_actuel", nullable = false)
    private EtatActuel etatActuel;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "mode_facturation", nullable = false)
    private ModeFacturation modeFacturation;

    @Column(name = "prix_forfaitaire")
    private Integer prixForfaitaire;

    @OneToMany(mappedBy = "casTraiter")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "certificat", "crEchographie", "ordonnances", "reglements", "casTraiter" }, allowSetters = true)
    private Set<Visite> visites = new HashSet<>();

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

    public CasTraiter id(Long id) {
        this.id = id;
        return this;
    }

    public String getCas() {
        return this.cas;
    }

    public CasTraiter cas(String cas) {
        this.cas = cas;
        return this;
    }

    public void setCas(String cas) {
        this.cas = cas;
    }

    public LocalDate getDepuis() {
        return this.depuis;
    }

    public CasTraiter depuis(LocalDate depuis) {
        this.depuis = depuis;
        return this;
    }

    public void setDepuis(LocalDate depuis) {
        this.depuis = depuis;
    }

    public String getHistoire() {
        return this.histoire;
    }

    public CasTraiter histoire(String histoire) {
        this.histoire = histoire;
        return this;
    }

    public void setHistoire(String histoire) {
        this.histoire = histoire;
    }

    public String getRemarques() {
        return this.remarques;
    }

    public CasTraiter remarques(String remarques) {
        this.remarques = remarques;
        return this;
    }

    public void setRemarques(String remarques) {
        this.remarques = remarques;
    }

    public EtatActuel getEtatActuel() {
        return this.etatActuel;
    }

    public CasTraiter etatActuel(EtatActuel etatActuel) {
        this.etatActuel = etatActuel;
        return this;
    }

    public void setEtatActuel(EtatActuel etatActuel) {
        this.etatActuel = etatActuel;
    }

    public ModeFacturation getModeFacturation() {
        return this.modeFacturation;
    }

    public CasTraiter modeFacturation(ModeFacturation modeFacturation) {
        this.modeFacturation = modeFacturation;
        return this;
    }

    public void setModeFacturation(ModeFacturation modeFacturation) {
        this.modeFacturation = modeFacturation;
    }

    public Integer getPrixForfaitaire() {
        return this.prixForfaitaire;
    }

    public CasTraiter prixForfaitaire(Integer prixForfaitaire) {
        this.prixForfaitaire = prixForfaitaire;
        return this;
    }

    public void setPrixForfaitaire(Integer prixForfaitaire) {
        this.prixForfaitaire = prixForfaitaire;
    }

    public Set<Visite> getVisites() {
        return this.visites;
    }

    public CasTraiter visites(Set<Visite> visites) {
        this.setVisites(visites);
        return this;
    }

    public CasTraiter addVisite(Visite visite) {
        this.visites.add(visite);
        visite.setCasTraiter(this);
        return this;
    }

    public CasTraiter removeVisite(Visite visite) {
        this.visites.remove(visite);
        visite.setCasTraiter(null);
        return this;
    }

    public void setVisites(Set<Visite> visites) {
        if (this.visites != null) {
            this.visites.forEach(i -> i.setCasTraiter(null));
        }
        if (visites != null) {
            visites.forEach(i -> i.setCasTraiter(this));
        }
        this.visites = visites;
    }

    public Patient getPatient() {
        return this.patient;
    }

    public CasTraiter patient(Patient patient) {
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
        if (!(o instanceof CasTraiter)) {
            return false;
        }
        return id != null && id.equals(((CasTraiter) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "CasTraiter{" +
            "id=" + getId() +
            ", cas='" + getCas() + "'" +
            ", depuis='" + getDepuis() + "'" +
            ", histoire='" + getHistoire() + "'" +
            ", remarques='" + getRemarques() + "'" +
            ", etatActuel='" + getEtatActuel() + "'" +
            ", modeFacturation='" + getModeFacturation() + "'" +
            ", prixForfaitaire=" + getPrixForfaitaire() +
            "}";
    }
}

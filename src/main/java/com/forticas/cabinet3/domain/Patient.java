package com.forticas.cabinet3.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.forticas.cabinet3.domain.enumeration.EtatCivil;
import com.forticas.cabinet3.domain.enumeration.Sexe;
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
 * A Patient.
 */
@Entity
@Table(name = "patient")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Patient implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @NotNull
    @Column(name = "reference", nullable = false, unique = true)
    private String reference;

    @NotNull
    @Size(min = 3)
    @Column(name = "nom", nullable = false)
    private String nom;

    @NotNull
    @Size(min = 3)
    @Column(name = "prenom", nullable = false)
    private String prenom;

    @NotNull
    @Column(name = "naissance", nullable = false)
    private LocalDate naissance;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "sexe", nullable = false)
    private Sexe sexe;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "etat_civil", nullable = false)
    private EtatCivil etatCivil;

    @Column(name = "fonction")
    private String fonction;

    @Column(name = "email")
    private String email;

    @NotNull
    @Column(name = "tel", nullable = false)
    private String tel;

    @NotNull
    @Column(name = "tel_fixe", nullable = false)
    private String telFixe;

    @Column(name = "adresse")
    private String adresse;

    @Lob
    @Type(type = "org.hibernate.type.TextType")
    @Column(name = "remarque")
    private String remarque;

    @OneToMany(mappedBy = "patient")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "patient" }, allowSetters = true)
    private Set<RendezVous> rendezVous = new HashSet<>();

    @OneToMany(mappedBy = "patient")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "visites", "patient" }, allowSetters = true)
    private Set<CasTraiter> casTraiters = new HashSet<>();

    @OneToMany(mappedBy = "patient")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "patient" }, allowSetters = true)
    private Set<Constante> constantes = new HashSet<>();

    @OneToMany(mappedBy = "patient")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "patient" }, allowSetters = true)
    private Set<Antecedent> antecedents = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Patient id(Long id) {
        this.id = id;
        return this;
    }

    public String getReference() {
        return this.reference;
    }

    public Patient reference(String reference) {
        this.reference = reference;
        return this;
    }

    public void setReference(String reference) {
        this.reference = reference;
    }

    public String getNom() {
        return this.nom;
    }

    public Patient nom(String nom) {
        this.nom = nom;
        return this;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getPrenom() {
        return this.prenom;
    }

    public Patient prenom(String prenom) {
        this.prenom = prenom;
        return this;
    }

    public void setPrenom(String prenom) {
        this.prenom = prenom;
    }

    public LocalDate getNaissance() {
        return this.naissance;
    }

    public Patient naissance(LocalDate naissance) {
        this.naissance = naissance;
        return this;
    }

    public void setNaissance(LocalDate naissance) {
        this.naissance = naissance;
    }

    public Sexe getSexe() {
        return this.sexe;
    }

    public Patient sexe(Sexe sexe) {
        this.sexe = sexe;
        return this;
    }

    public void setSexe(Sexe sexe) {
        this.sexe = sexe;
    }

    public EtatCivil getEtatCivil() {
        return this.etatCivil;
    }

    public Patient etatCivil(EtatCivil etatCivil) {
        this.etatCivil = etatCivil;
        return this;
    }

    public void setEtatCivil(EtatCivil etatCivil) {
        this.etatCivil = etatCivil;
    }

    public String getFonction() {
        return this.fonction;
    }

    public Patient fonction(String fonction) {
        this.fonction = fonction;
        return this;
    }

    public void setFonction(String fonction) {
        this.fonction = fonction;
    }

    public String getEmail() {
        return this.email;
    }

    public Patient email(String email) {
        this.email = email;
        return this;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getTel() {
        return this.tel;
    }

    public Patient tel(String tel) {
        this.tel = tel;
        return this;
    }

    public void setTel(String tel) {
        this.tel = tel;
    }

    public String getTelFixe() {
        return this.telFixe;
    }

    public Patient telFixe(String telFixe) {
        this.telFixe = telFixe;
        return this;
    }

    public void setTelFixe(String telFixe) {
        this.telFixe = telFixe;
    }

    public String getAdresse() {
        return this.adresse;
    }

    public Patient adresse(String adresse) {
        this.adresse = adresse;
        return this;
    }

    public void setAdresse(String adresse) {
        this.adresse = adresse;
    }

    public String getRemarque() {
        return this.remarque;
    }

    public Patient remarque(String remarque) {
        this.remarque = remarque;
        return this;
    }

    public void setRemarque(String remarque) {
        this.remarque = remarque;
    }

    public Set<RendezVous> getRendezVous() {
        return this.rendezVous;
    }

    public Patient rendezVous(Set<RendezVous> rendezVous) {
        this.setRendezVous(rendezVous);
        return this;
    }

    public Patient addRendezVous(RendezVous rendezVous) {
        this.rendezVous.add(rendezVous);
        rendezVous.setPatient(this);
        return this;
    }

    public Patient removeRendezVous(RendezVous rendezVous) {
        this.rendezVous.remove(rendezVous);
        rendezVous.setPatient(null);
        return this;
    }

    public void setRendezVous(Set<RendezVous> rendezVous) {
        if (this.rendezVous != null) {
            this.rendezVous.forEach(i -> i.setPatient(null));
        }
        if (rendezVous != null) {
            rendezVous.forEach(i -> i.setPatient(this));
        }
        this.rendezVous = rendezVous;
    }

    public Set<CasTraiter> getCasTraiters() {
        return this.casTraiters;
    }

    public Patient casTraiters(Set<CasTraiter> casTraiters) {
        this.setCasTraiters(casTraiters);
        return this;
    }

    public Patient addCasTraiter(CasTraiter casTraiter) {
        this.casTraiters.add(casTraiter);
        casTraiter.setPatient(this);
        return this;
    }

    public Patient removeCasTraiter(CasTraiter casTraiter) {
        this.casTraiters.remove(casTraiter);
        casTraiter.setPatient(null);
        return this;
    }

    public void setCasTraiters(Set<CasTraiter> casTraiters) {
        if (this.casTraiters != null) {
            this.casTraiters.forEach(i -> i.setPatient(null));
        }
        if (casTraiters != null) {
            casTraiters.forEach(i -> i.setPatient(this));
        }
        this.casTraiters = casTraiters;
    }

    public Set<Constante> getConstantes() {
        return this.constantes;
    }

    public Patient constantes(Set<Constante> constantes) {
        this.setConstantes(constantes);
        return this;
    }

    public Patient addConstante(Constante constante) {
        this.constantes.add(constante);
        constante.setPatient(this);
        return this;
    }

    public Patient removeConstante(Constante constante) {
        this.constantes.remove(constante);
        constante.setPatient(null);
        return this;
    }

    public void setConstantes(Set<Constante> constantes) {
        if (this.constantes != null) {
            this.constantes.forEach(i -> i.setPatient(null));
        }
        if (constantes != null) {
            constantes.forEach(i -> i.setPatient(this));
        }
        this.constantes = constantes;
    }

    public Set<Antecedent> getAntecedents() {
        return this.antecedents;
    }

    public Patient antecedents(Set<Antecedent> antecedents) {
        this.setAntecedents(antecedents);
        return this;
    }

    public Patient addAntecedent(Antecedent antecedent) {
        this.antecedents.add(antecedent);
        antecedent.setPatient(this);
        return this;
    }

    public Patient removeAntecedent(Antecedent antecedent) {
        this.antecedents.remove(antecedent);
        antecedent.setPatient(null);
        return this;
    }

    public void setAntecedents(Set<Antecedent> antecedents) {
        if (this.antecedents != null) {
            this.antecedents.forEach(i -> i.setPatient(null));
        }
        if (antecedents != null) {
            antecedents.forEach(i -> i.setPatient(this));
        }
        this.antecedents = antecedents;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Patient)) {
            return false;
        }
        return id != null && id.equals(((Patient) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Patient{" +
            "id=" + getId() +
            ", reference='" + getReference() + "'" +
            ", nom='" + getNom() + "'" +
            ", prenom='" + getPrenom() + "'" +
            ", naissance='" + getNaissance() + "'" +
            ", sexe='" + getSexe() + "'" +
            ", etatCivil='" + getEtatCivil() + "'" +
            ", fonction='" + getFonction() + "'" +
            ", email='" + getEmail() + "'" +
            ", tel='" + getTel() + "'" +
            ", telFixe='" + getTelFixe() + "'" +
            ", adresse='" + getAdresse() + "'" +
            ", remarque='" + getRemarque() + "'" +
            "}";
    }
}

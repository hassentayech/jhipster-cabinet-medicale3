package com.forticas.cabinet3.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
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
 * A Visite.
 */
@Entity
@Table(name = "visite")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Visite implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @Column(name = "control")
    private Boolean control;

    @Column(name = "date")
    private LocalDate date;

    @Column(name = "motif")
    private String motif;

    @Lob
    @Type(type = "org.hibernate.type.TextType")
    @Column(name = "interrogatoire")
    private String interrogatoire;

    @Lob
    @Type(type = "org.hibernate.type.TextType")
    @Column(name = "examen")
    private String examen;

    @Lob
    @Type(type = "org.hibernate.type.TextType")
    @Column(name = "conclusion")
    private String conclusion;

    @NotNull
    @Column(name = "honoraire", nullable = false)
    private Integer honoraire;

    @JsonIgnoreProperties(value = { "visite" }, allowSetters = true)
    @OneToOne
    @JoinColumn(unique = true)
    private Certificat certificat;

    @JsonIgnoreProperties(value = { "visite" }, allowSetters = true)
    @OneToOne
    @JoinColumn(unique = true)
    private CrEchographie crEchographie;

    @OneToMany(mappedBy = "visite")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "prescriptions", "visite" }, allowSetters = true)
    private Set<Ordonnance> ordonnances = new HashSet<>();

    @OneToMany(mappedBy = "visite")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "visite" }, allowSetters = true)
    private Set<Reglement> reglements = new HashSet<>();

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties(value = { "visites", "patient" }, allowSetters = true)
    private CasTraiter casTraiter;

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Visite id(Long id) {
        this.id = id;
        return this;
    }

    public Boolean getControl() {
        return this.control;
    }

    public Visite control(Boolean control) {
        this.control = control;
        return this;
    }

    public void setControl(Boolean control) {
        this.control = control;
    }

    public LocalDate getDate() {
        return this.date;
    }

    public Visite date(LocalDate date) {
        this.date = date;
        return this;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public String getMotif() {
        return this.motif;
    }

    public Visite motif(String motif) {
        this.motif = motif;
        return this;
    }

    public void setMotif(String motif) {
        this.motif = motif;
    }

    public String getInterrogatoire() {
        return this.interrogatoire;
    }

    public Visite interrogatoire(String interrogatoire) {
        this.interrogatoire = interrogatoire;
        return this;
    }

    public void setInterrogatoire(String interrogatoire) {
        this.interrogatoire = interrogatoire;
    }

    public String getExamen() {
        return this.examen;
    }

    public Visite examen(String examen) {
        this.examen = examen;
        return this;
    }

    public void setExamen(String examen) {
        this.examen = examen;
    }

    public String getConclusion() {
        return this.conclusion;
    }

    public Visite conclusion(String conclusion) {
        this.conclusion = conclusion;
        return this;
    }

    public void setConclusion(String conclusion) {
        this.conclusion = conclusion;
    }

    public Integer getHonoraire() {
        return this.honoraire;
    }

    public Visite honoraire(Integer honoraire) {
        this.honoraire = honoraire;
        return this;
    }

    public void setHonoraire(Integer honoraire) {
        this.honoraire = honoraire;
    }

    public Certificat getCertificat() {
        return this.certificat;
    }

    public Visite certificat(Certificat certificat) {
        this.setCertificat(certificat);
        return this;
    }

    public void setCertificat(Certificat certificat) {
        this.certificat = certificat;
    }

    public CrEchographie getCrEchographie() {
        return this.crEchographie;
    }

    public Visite crEchographie(CrEchographie crEchographie) {
        this.setCrEchographie(crEchographie);
        return this;
    }

    public void setCrEchographie(CrEchographie crEchographie) {
        this.crEchographie = crEchographie;
    }

    public Set<Ordonnance> getOrdonnances() {
        return this.ordonnances;
    }

    public Visite ordonnances(Set<Ordonnance> ordonnances) {
        this.setOrdonnances(ordonnances);
        return this;
    }

    public Visite addOrdonnance(Ordonnance ordonnance) {
        this.ordonnances.add(ordonnance);
        ordonnance.setVisite(this);
        return this;
    }

    public Visite removeOrdonnance(Ordonnance ordonnance) {
        this.ordonnances.remove(ordonnance);
        ordonnance.setVisite(null);
        return this;
    }

    public void setOrdonnances(Set<Ordonnance> ordonnances) {
        if (this.ordonnances != null) {
            this.ordonnances.forEach(i -> i.setVisite(null));
        }
        if (ordonnances != null) {
            ordonnances.forEach(i -> i.setVisite(this));
        }
        this.ordonnances = ordonnances;
    }

    public Set<Reglement> getReglements() {
        return this.reglements;
    }

    public Visite reglements(Set<Reglement> reglements) {
        this.setReglements(reglements);
        return this;
    }

    public Visite addReglement(Reglement reglement) {
        this.reglements.add(reglement);
        reglement.setVisite(this);
        return this;
    }

    public Visite removeReglement(Reglement reglement) {
        this.reglements.remove(reglement);
        reglement.setVisite(null);
        return this;
    }

    public void setReglements(Set<Reglement> reglements) {
        if (this.reglements != null) {
            this.reglements.forEach(i -> i.setVisite(null));
        }
        if (reglements != null) {
            reglements.forEach(i -> i.setVisite(this));
        }
        this.reglements = reglements;
    }

    public CasTraiter getCasTraiter() {
        return this.casTraiter;
    }

    public Visite casTraiter(CasTraiter casTraiter) {
        this.setCasTraiter(casTraiter);
        return this;
    }

    public void setCasTraiter(CasTraiter casTraiter) {
        this.casTraiter = casTraiter;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Visite)) {
            return false;
        }
        return id != null && id.equals(((Visite) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Visite{" +
            "id=" + getId() +
            ", control='" + getControl() + "'" +
            ", date='" + getDate() + "'" +
            ", motif='" + getMotif() + "'" +
            ", interrogatoire='" + getInterrogatoire() + "'" +
            ", examen='" + getExamen() + "'" +
            ", conclusion='" + getConclusion() + "'" +
            ", honoraire=" + getHonoraire() +
            "}";
    }
}

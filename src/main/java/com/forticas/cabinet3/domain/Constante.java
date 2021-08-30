package com.forticas.cabinet3.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.time.ZonedDateTime;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Constante.
 */
@Entity
@Table(name = "constante")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Constante implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @NotNull
    @Column(name = "date", nullable = false)
    private ZonedDateTime date;

    @Column(name = "poid")
    private Integer poid;

    @Column(name = "taille")
    private Integer taille;

    @Column(name = "pas")
    private Integer pas;

    @Column(name = "pad")
    private Integer pad;

    @Column(name = "pouls")
    private Integer pouls;

    @Column(name = "temp")
    private Integer temp;

    @Column(name = "glycemie")
    private Integer glycemie;

    @Column(name = "cholesterol")
    private Integer cholesterol;

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

    public Constante id(Long id) {
        this.id = id;
        return this;
    }

    public ZonedDateTime getDate() {
        return this.date;
    }

    public Constante date(ZonedDateTime date) {
        this.date = date;
        return this;
    }

    public void setDate(ZonedDateTime date) {
        this.date = date;
    }

    public Integer getPoid() {
        return this.poid;
    }

    public Constante poid(Integer poid) {
        this.poid = poid;
        return this;
    }

    public void setPoid(Integer poid) {
        this.poid = poid;
    }

    public Integer getTaille() {
        return this.taille;
    }

    public Constante taille(Integer taille) {
        this.taille = taille;
        return this;
    }

    public void setTaille(Integer taille) {
        this.taille = taille;
    }

    public Integer getPas() {
        return this.pas;
    }

    public Constante pas(Integer pas) {
        this.pas = pas;
        return this;
    }

    public void setPas(Integer pas) {
        this.pas = pas;
    }

    public Integer getPad() {
        return this.pad;
    }

    public Constante pad(Integer pad) {
        this.pad = pad;
        return this;
    }

    public void setPad(Integer pad) {
        this.pad = pad;
    }

    public Integer getPouls() {
        return this.pouls;
    }

    public Constante pouls(Integer pouls) {
        this.pouls = pouls;
        return this;
    }

    public void setPouls(Integer pouls) {
        this.pouls = pouls;
    }

    public Integer getTemp() {
        return this.temp;
    }

    public Constante temp(Integer temp) {
        this.temp = temp;
        return this;
    }

    public void setTemp(Integer temp) {
        this.temp = temp;
    }

    public Integer getGlycemie() {
        return this.glycemie;
    }

    public Constante glycemie(Integer glycemie) {
        this.glycemie = glycemie;
        return this;
    }

    public void setGlycemie(Integer glycemie) {
        this.glycemie = glycemie;
    }

    public Integer getCholesterol() {
        return this.cholesterol;
    }

    public Constante cholesterol(Integer cholesterol) {
        this.cholesterol = cholesterol;
        return this;
    }

    public void setCholesterol(Integer cholesterol) {
        this.cholesterol = cholesterol;
    }

    public Patient getPatient() {
        return this.patient;
    }

    public Constante patient(Patient patient) {
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
        if (!(o instanceof Constante)) {
            return false;
        }
        return id != null && id.equals(((Constante) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Constante{" +
            "id=" + getId() +
            ", date='" + getDate() + "'" +
            ", poid=" + getPoid() +
            ", taille=" + getTaille() +
            ", pas=" + getPas() +
            ", pad=" + getPad() +
            ", pouls=" + getPouls() +
            ", temp=" + getTemp() +
            ", glycemie=" + getGlycemie() +
            ", cholesterol=" + getCholesterol() +
            "}";
    }
}

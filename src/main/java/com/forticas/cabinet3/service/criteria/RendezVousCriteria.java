package com.forticas.cabinet3.service.criteria;

import java.io.Serializable;
import java.util.Objects;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.BooleanFilter;
import tech.jhipster.service.filter.DoubleFilter;
import tech.jhipster.service.filter.Filter;
import tech.jhipster.service.filter.FloatFilter;
import tech.jhipster.service.filter.IntegerFilter;
import tech.jhipster.service.filter.LocalDateFilter;
import tech.jhipster.service.filter.LongFilter;
import tech.jhipster.service.filter.StringFilter;

/**
 * Criteria class for the {@link com.forticas.cabinet3.domain.RendezVous} entity. This class is used
 * in {@link com.forticas.cabinet3.web.rest.RendezVousResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /rendez-vous?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class RendezVousCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private LocalDateFilter date;

    private StringFilter trancheHoraire;

    private StringFilter nbrTranche;

    private StringFilter motif;

    private BooleanFilter present;

    private LongFilter patientId;

    public RendezVousCriteria() {}

    public RendezVousCriteria(RendezVousCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.date = other.date == null ? null : other.date.copy();
        this.trancheHoraire = other.trancheHoraire == null ? null : other.trancheHoraire.copy();
        this.nbrTranche = other.nbrTranche == null ? null : other.nbrTranche.copy();
        this.motif = other.motif == null ? null : other.motif.copy();
        this.present = other.present == null ? null : other.present.copy();
        this.patientId = other.patientId == null ? null : other.patientId.copy();
    }

    @Override
    public RendezVousCriteria copy() {
        return new RendezVousCriteria(this);
    }

    public LongFilter getId() {
        return id;
    }

    public LongFilter id() {
        if (id == null) {
            id = new LongFilter();
        }
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public LocalDateFilter getDate() {
        return date;
    }

    public LocalDateFilter date() {
        if (date == null) {
            date = new LocalDateFilter();
        }
        return date;
    }

    public void setDate(LocalDateFilter date) {
        this.date = date;
    }

    public StringFilter getTrancheHoraire() {
        return trancheHoraire;
    }

    public StringFilter trancheHoraire() {
        if (trancheHoraire == null) {
            trancheHoraire = new StringFilter();
        }
        return trancheHoraire;
    }

    public void setTrancheHoraire(StringFilter trancheHoraire) {
        this.trancheHoraire = trancheHoraire;
    }

    public StringFilter getNbrTranche() {
        return nbrTranche;
    }

    public StringFilter nbrTranche() {
        if (nbrTranche == null) {
            nbrTranche = new StringFilter();
        }
        return nbrTranche;
    }

    public void setNbrTranche(StringFilter nbrTranche) {
        this.nbrTranche = nbrTranche;
    }

    public StringFilter getMotif() {
        return motif;
    }

    public StringFilter motif() {
        if (motif == null) {
            motif = new StringFilter();
        }
        return motif;
    }

    public void setMotif(StringFilter motif) {
        this.motif = motif;
    }

    public BooleanFilter getPresent() {
        return present;
    }

    public BooleanFilter present() {
        if (present == null) {
            present = new BooleanFilter();
        }
        return present;
    }

    public void setPresent(BooleanFilter present) {
        this.present = present;
    }

    public LongFilter getPatientId() {
        return patientId;
    }

    public LongFilter patientId() {
        if (patientId == null) {
            patientId = new LongFilter();
        }
        return patientId;
    }

    public void setPatientId(LongFilter patientId) {
        this.patientId = patientId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final RendezVousCriteria that = (RendezVousCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(date, that.date) &&
            Objects.equals(trancheHoraire, that.trancheHoraire) &&
            Objects.equals(nbrTranche, that.nbrTranche) &&
            Objects.equals(motif, that.motif) &&
            Objects.equals(present, that.present) &&
            Objects.equals(patientId, that.patientId)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, date, trancheHoraire, nbrTranche, motif, present, patientId);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "RendezVousCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (date != null ? "date=" + date + ", " : "") +
            (trancheHoraire != null ? "trancheHoraire=" + trancheHoraire + ", " : "") +
            (nbrTranche != null ? "nbrTranche=" + nbrTranche + ", " : "") +
            (motif != null ? "motif=" + motif + ", " : "") +
            (present != null ? "present=" + present + ", " : "") +
            (patientId != null ? "patientId=" + patientId + ", " : "") +
            "}";
    }
}

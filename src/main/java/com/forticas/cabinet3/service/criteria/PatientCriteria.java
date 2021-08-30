package com.forticas.cabinet3.service.criteria;

import com.forticas.cabinet3.domain.enumeration.EtatCivil;
import com.forticas.cabinet3.domain.enumeration.Sexe;
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
 * Criteria class for the {@link com.forticas.cabinet3.domain.Patient} entity. This class is used
 * in {@link com.forticas.cabinet3.web.rest.PatientResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /patients?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class PatientCriteria implements Serializable, Criteria {

    /**
     * Class for filtering Sexe
     */
    public static class SexeFilter extends Filter<Sexe> {

        public SexeFilter() {}

        public SexeFilter(SexeFilter filter) {
            super(filter);
        }

        @Override
        public SexeFilter copy() {
            return new SexeFilter(this);
        }
    }

    /**
     * Class for filtering EtatCivil
     */
    public static class EtatCivilFilter extends Filter<EtatCivil> {

        public EtatCivilFilter() {}

        public EtatCivilFilter(EtatCivilFilter filter) {
            super(filter);
        }

        @Override
        public EtatCivilFilter copy() {
            return new EtatCivilFilter(this);
        }
    }

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter reference;

    private StringFilter nom;

    private StringFilter prenom;

    private LocalDateFilter naissance;

    private SexeFilter sexe;

    private EtatCivilFilter etatCivil;

    private StringFilter fonction;

    private StringFilter email;

    private StringFilter tel;

    private StringFilter telFixe;

    private StringFilter adresse;

    private LongFilter rendezVousId;

    private LongFilter casTraiterId;

    private LongFilter constanteId;

    private LongFilter antecedentId;

    public PatientCriteria() {}

    public PatientCriteria(PatientCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.reference = other.reference == null ? null : other.reference.copy();
        this.nom = other.nom == null ? null : other.nom.copy();
        this.prenom = other.prenom == null ? null : other.prenom.copy();
        this.naissance = other.naissance == null ? null : other.naissance.copy();
        this.sexe = other.sexe == null ? null : other.sexe.copy();
        this.etatCivil = other.etatCivil == null ? null : other.etatCivil.copy();
        this.fonction = other.fonction == null ? null : other.fonction.copy();
        this.email = other.email == null ? null : other.email.copy();
        this.tel = other.tel == null ? null : other.tel.copy();
        this.telFixe = other.telFixe == null ? null : other.telFixe.copy();
        this.adresse = other.adresse == null ? null : other.adresse.copy();
        this.rendezVousId = other.rendezVousId == null ? null : other.rendezVousId.copy();
        this.casTraiterId = other.casTraiterId == null ? null : other.casTraiterId.copy();
        this.constanteId = other.constanteId == null ? null : other.constanteId.copy();
        this.antecedentId = other.antecedentId == null ? null : other.antecedentId.copy();
    }

    @Override
    public PatientCriteria copy() {
        return new PatientCriteria(this);
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

    public StringFilter getReference() {
        return reference;
    }

    public StringFilter reference() {
        if (reference == null) {
            reference = new StringFilter();
        }
        return reference;
    }

    public void setReference(StringFilter reference) {
        this.reference = reference;
    }

    public StringFilter getNom() {
        return nom;
    }

    public StringFilter nom() {
        if (nom == null) {
            nom = new StringFilter();
        }
        return nom;
    }

    public void setNom(StringFilter nom) {
        this.nom = nom;
    }

    public StringFilter getPrenom() {
        return prenom;
    }

    public StringFilter prenom() {
        if (prenom == null) {
            prenom = new StringFilter();
        }
        return prenom;
    }

    public void setPrenom(StringFilter prenom) {
        this.prenom = prenom;
    }

    public LocalDateFilter getNaissance() {
        return naissance;
    }

    public LocalDateFilter naissance() {
        if (naissance == null) {
            naissance = new LocalDateFilter();
        }
        return naissance;
    }

    public void setNaissance(LocalDateFilter naissance) {
        this.naissance = naissance;
    }

    public SexeFilter getSexe() {
        return sexe;
    }

    public SexeFilter sexe() {
        if (sexe == null) {
            sexe = new SexeFilter();
        }
        return sexe;
    }

    public void setSexe(SexeFilter sexe) {
        this.sexe = sexe;
    }

    public EtatCivilFilter getEtatCivil() {
        return etatCivil;
    }

    public EtatCivilFilter etatCivil() {
        if (etatCivil == null) {
            etatCivil = new EtatCivilFilter();
        }
        return etatCivil;
    }

    public void setEtatCivil(EtatCivilFilter etatCivil) {
        this.etatCivil = etatCivil;
    }

    public StringFilter getFonction() {
        return fonction;
    }

    public StringFilter fonction() {
        if (fonction == null) {
            fonction = new StringFilter();
        }
        return fonction;
    }

    public void setFonction(StringFilter fonction) {
        this.fonction = fonction;
    }

    public StringFilter getEmail() {
        return email;
    }

    public StringFilter email() {
        if (email == null) {
            email = new StringFilter();
        }
        return email;
    }

    public void setEmail(StringFilter email) {
        this.email = email;
    }

    public StringFilter getTel() {
        return tel;
    }

    public StringFilter tel() {
        if (tel == null) {
            tel = new StringFilter();
        }
        return tel;
    }

    public void setTel(StringFilter tel) {
        this.tel = tel;
    }

    public StringFilter getTelFixe() {
        return telFixe;
    }

    public StringFilter telFixe() {
        if (telFixe == null) {
            telFixe = new StringFilter();
        }
        return telFixe;
    }

    public void setTelFixe(StringFilter telFixe) {
        this.telFixe = telFixe;
    }

    public StringFilter getAdresse() {
        return adresse;
    }

    public StringFilter adresse() {
        if (adresse == null) {
            adresse = new StringFilter();
        }
        return adresse;
    }

    public void setAdresse(StringFilter adresse) {
        this.adresse = adresse;
    }

    public LongFilter getRendezVousId() {
        return rendezVousId;
    }

    public LongFilter rendezVousId() {
        if (rendezVousId == null) {
            rendezVousId = new LongFilter();
        }
        return rendezVousId;
    }

    public void setRendezVousId(LongFilter rendezVousId) {
        this.rendezVousId = rendezVousId;
    }

    public LongFilter getCasTraiterId() {
        return casTraiterId;
    }

    public LongFilter casTraiterId() {
        if (casTraiterId == null) {
            casTraiterId = new LongFilter();
        }
        return casTraiterId;
    }

    public void setCasTraiterId(LongFilter casTraiterId) {
        this.casTraiterId = casTraiterId;
    }

    public LongFilter getConstanteId() {
        return constanteId;
    }

    public LongFilter constanteId() {
        if (constanteId == null) {
            constanteId = new LongFilter();
        }
        return constanteId;
    }

    public void setConstanteId(LongFilter constanteId) {
        this.constanteId = constanteId;
    }

    public LongFilter getAntecedentId() {
        return antecedentId;
    }

    public LongFilter antecedentId() {
        if (antecedentId == null) {
            antecedentId = new LongFilter();
        }
        return antecedentId;
    }

    public void setAntecedentId(LongFilter antecedentId) {
        this.antecedentId = antecedentId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final PatientCriteria that = (PatientCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(reference, that.reference) &&
            Objects.equals(nom, that.nom) &&
            Objects.equals(prenom, that.prenom) &&
            Objects.equals(naissance, that.naissance) &&
            Objects.equals(sexe, that.sexe) &&
            Objects.equals(etatCivil, that.etatCivil) &&
            Objects.equals(fonction, that.fonction) &&
            Objects.equals(email, that.email) &&
            Objects.equals(tel, that.tel) &&
            Objects.equals(telFixe, that.telFixe) &&
            Objects.equals(adresse, that.adresse) &&
            Objects.equals(rendezVousId, that.rendezVousId) &&
            Objects.equals(casTraiterId, that.casTraiterId) &&
            Objects.equals(constanteId, that.constanteId) &&
            Objects.equals(antecedentId, that.antecedentId)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(
            id,
            reference,
            nom,
            prenom,
            naissance,
            sexe,
            etatCivil,
            fonction,
            email,
            tel,
            telFixe,
            adresse,
            rendezVousId,
            casTraiterId,
            constanteId,
            antecedentId
        );
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "PatientCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (reference != null ? "reference=" + reference + ", " : "") +
            (nom != null ? "nom=" + nom + ", " : "") +
            (prenom != null ? "prenom=" + prenom + ", " : "") +
            (naissance != null ? "naissance=" + naissance + ", " : "") +
            (sexe != null ? "sexe=" + sexe + ", " : "") +
            (etatCivil != null ? "etatCivil=" + etatCivil + ", " : "") +
            (fonction != null ? "fonction=" + fonction + ", " : "") +
            (email != null ? "email=" + email + ", " : "") +
            (tel != null ? "tel=" + tel + ", " : "") +
            (telFixe != null ? "telFixe=" + telFixe + ", " : "") +
            (adresse != null ? "adresse=" + adresse + ", " : "") +
            (rendezVousId != null ? "rendezVousId=" + rendezVousId + ", " : "") +
            (casTraiterId != null ? "casTraiterId=" + casTraiterId + ", " : "") +
            (constanteId != null ? "constanteId=" + constanteId + ", " : "") +
            (antecedentId != null ? "antecedentId=" + antecedentId + ", " : "") +
            "}";
    }
}

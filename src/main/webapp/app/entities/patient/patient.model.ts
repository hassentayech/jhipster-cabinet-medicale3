import * as dayjs from 'dayjs';
import { IRendezVous } from 'app/entities/rendez-vous/rendez-vous.model';
import { ICasTraiter } from 'app/entities/cas-traiter/cas-traiter.model';
import { IConstante } from 'app/entities/constante/constante.model';
import { IAntecedent } from 'app/entities/antecedent/antecedent.model';
import { Sexe } from 'app/entities/enumerations/sexe.model';
import { EtatCivil } from 'app/entities/enumerations/etat-civil.model';

export interface IPatient {
  id?: number;
  reference?: string;
  nom?: string;
  prenom?: string;
  naissance?: dayjs.Dayjs;
  sexe?: Sexe;
  etatCivil?: EtatCivil;
  fonction?: string | null;
  email?: string | null;
  tel?: string;
  telFixe?: string;
  adresse?: string | null;
  remarque?: string | null;
  rendezVous?: IRendezVous[] | null;
  casTraiters?: ICasTraiter[] | null;
  constantes?: IConstante[] | null;
  antecedents?: IAntecedent[] | null;
}

export class Patient implements IPatient {
  constructor(
    public id?: number,
    public reference?: string,
    public nom?: string,
    public prenom?: string,
    public naissance?: dayjs.Dayjs,
    public sexe?: Sexe,
    public etatCivil?: EtatCivil,
    public fonction?: string | null,
    public email?: string | null,
    public tel?: string,
    public telFixe?: string,
    public adresse?: string | null,
    public remarque?: string | null,
    public rendezVous?: IRendezVous[] | null,
    public casTraiters?: ICasTraiter[] | null,
    public constantes?: IConstante[] | null,
    public antecedents?: IAntecedent[] | null
  ) {}
}

export function getPatientIdentifier(patient: IPatient): number | undefined {
  return patient.id;
}

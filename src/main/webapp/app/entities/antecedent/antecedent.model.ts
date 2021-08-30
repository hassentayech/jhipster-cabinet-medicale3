import { IPatient } from 'app/entities/patient/patient.model';
import { TypeAntecedent } from 'app/entities/enumerations/type-antecedent.model';

export interface IAntecedent {
  id?: number;
  type?: TypeAntecedent;
  periode?: string;
  antecedent?: string;
  traitement?: string | null;
  patient?: IPatient;
}

export class Antecedent implements IAntecedent {
  constructor(
    public id?: number,
    public type?: TypeAntecedent,
    public periode?: string,
    public antecedent?: string,
    public traitement?: string | null,
    public patient?: IPatient
  ) {}
}

export function getAntecedentIdentifier(antecedent: IAntecedent): number | undefined {
  return antecedent.id;
}

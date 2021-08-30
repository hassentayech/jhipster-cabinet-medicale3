import { IPrescription } from 'app/entities/prescription/prescription.model';
import { IVisite } from 'app/entities/visite/visite.model';

export interface IOrdonnance {
  id?: number;
  prescriptions?: IPrescription[] | null;
  visite?: IVisite;
}

export class Ordonnance implements IOrdonnance {
  constructor(public id?: number, public prescriptions?: IPrescription[] | null, public visite?: IVisite) {}
}

export function getOrdonnanceIdentifier(ordonnance: IOrdonnance): number | undefined {
  return ordonnance.id;
}

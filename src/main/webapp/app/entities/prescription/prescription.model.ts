import { IOrdonnance } from 'app/entities/ordonnance/ordonnance.model';

export interface IPrescription {
  id?: number;
  prescription?: string;
  prise?: string | null;
  ordonnance?: IOrdonnance;
}

export class Prescription implements IPrescription {
  constructor(public id?: number, public prescription?: string, public prise?: string | null, public ordonnance?: IOrdonnance) {}
}

export function getPrescriptionIdentifier(prescription: IPrescription): number | undefined {
  return prescription.id;
}

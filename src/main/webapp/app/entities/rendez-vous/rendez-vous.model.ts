import * as dayjs from 'dayjs';
import { IPatient } from 'app/entities/patient/patient.model';

export interface IRendezVous {
  id?: number;
  date?: dayjs.Dayjs;
  trancheHoraire?: string;
  nbrTranche?: string;
  motif?: string | null;
  present?: boolean | null;
  patient?: IPatient;
}

export class RendezVous implements IRendezVous {
  constructor(
    public id?: number,
    public date?: dayjs.Dayjs,
    public trancheHoraire?: string,
    public nbrTranche?: string,
    public motif?: string | null,
    public present?: boolean | null,
    public patient?: IPatient
  ) {
    this.present = this.present ?? false;
  }
}

export function getRendezVousIdentifier(rendezVous: IRendezVous): number | undefined {
  return rendezVous.id;
}

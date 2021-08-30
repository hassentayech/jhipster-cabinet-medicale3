import * as dayjs from 'dayjs';
import { IPatient } from 'app/entities/patient/patient.model';

export interface IConstante {
  id?: number;
  date?: dayjs.Dayjs;
  poid?: number | null;
  taille?: number | null;
  pas?: number | null;
  pad?: number | null;
  pouls?: number | null;
  temp?: number | null;
  glycemie?: number | null;
  cholesterol?: number | null;
  patient?: IPatient;
}

export class Constante implements IConstante {
  constructor(
    public id?: number,
    public date?: dayjs.Dayjs,
    public poid?: number | null,
    public taille?: number | null,
    public pas?: number | null,
    public pad?: number | null,
    public pouls?: number | null,
    public temp?: number | null,
    public glycemie?: number | null,
    public cholesterol?: number | null,
    public patient?: IPatient
  ) {}
}

export function getConstanteIdentifier(constante: IConstante): number | undefined {
  return constante.id;
}

import * as dayjs from 'dayjs';
import { IVisite } from 'app/entities/visite/visite.model';
import { TypePayement } from 'app/entities/enumerations/type-payement.model';

export interface IReglement {
  id?: number;
  date?: dayjs.Dayjs;
  valeur?: number;
  typePayement?: TypePayement;
  remarque?: string | null;
  visite?: IVisite;
}

export class Reglement implements IReglement {
  constructor(
    public id?: number,
    public date?: dayjs.Dayjs,
    public valeur?: number,
    public typePayement?: TypePayement,
    public remarque?: string | null,
    public visite?: IVisite
  ) {}
}

export function getReglementIdentifier(reglement: IReglement): number | undefined {
  return reglement.id;
}

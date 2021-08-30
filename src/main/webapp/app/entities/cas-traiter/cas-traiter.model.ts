import * as dayjs from 'dayjs';
import { IVisite } from 'app/entities/visite/visite.model';
import { IPatient } from 'app/entities/patient/patient.model';
import { EtatActuel } from 'app/entities/enumerations/etat-actuel.model';
import { ModeFacturation } from 'app/entities/enumerations/mode-facturation.model';

export interface ICasTraiter {
  id?: number;
  cas?: string;
  depuis?: dayjs.Dayjs;
  histoire?: string | null;
  remarques?: string | null;
  etatActuel?: EtatActuel;
  modeFacturation?: ModeFacturation;
  prixForfaitaire?: number | null;
  visites?: IVisite[] | null;
  patient?: IPatient;
}

export class CasTraiter implements ICasTraiter {
  constructor(
    public id?: number,
    public cas?: string,
    public depuis?: dayjs.Dayjs,
    public histoire?: string | null,
    public remarques?: string | null,
    public etatActuel?: EtatActuel,
    public modeFacturation?: ModeFacturation,
    public prixForfaitaire?: number | null,
    public visites?: IVisite[] | null,
    public patient?: IPatient
  ) {}
}

export function getCasTraiterIdentifier(casTraiter: ICasTraiter): number | undefined {
  return casTraiter.id;
}

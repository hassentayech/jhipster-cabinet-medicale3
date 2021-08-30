import * as dayjs from 'dayjs';
import { ICertificat } from 'app/entities/certificat/certificat.model';
import { ICrEchographie } from 'app/entities/cr-echographie/cr-echographie.model';
import { IOrdonnance } from 'app/entities/ordonnance/ordonnance.model';
import { IReglement } from 'app/entities/reglement/reglement.model';
import { ICasTraiter } from 'app/entities/cas-traiter/cas-traiter.model';

export interface IVisite {
  id?: number;
  control?: boolean | null;
  date?: dayjs.Dayjs | null;
  motif?: string | null;
  interrogatoire?: string | null;
  examen?: string | null;
  conclusion?: string | null;
  honoraire?: number;
  certificat?: ICertificat | null;
  crEchographie?: ICrEchographie | null;
  ordonnances?: IOrdonnance[] | null;
  reglements?: IReglement[] | null;
  casTraiter?: ICasTraiter;
}

export class Visite implements IVisite {
  constructor(
    public id?: number,
    public control?: boolean | null,
    public date?: dayjs.Dayjs | null,
    public motif?: string | null,
    public interrogatoire?: string | null,
    public examen?: string | null,
    public conclusion?: string | null,
    public honoraire?: number,
    public certificat?: ICertificat | null,
    public crEchographie?: ICrEchographie | null,
    public ordonnances?: IOrdonnance[] | null,
    public reglements?: IReglement[] | null,
    public casTraiter?: ICasTraiter
  ) {
    this.control = this.control ?? false;
  }
}

export function getVisiteIdentifier(visite: IVisite): number | undefined {
  return visite.id;
}

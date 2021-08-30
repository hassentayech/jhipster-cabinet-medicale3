import { IVisite } from 'app/entities/visite/visite.model';

export interface ICertificat {
  id?: number;
  nbrJours?: number;
  description?: string | null;
  visite?: IVisite;
}

export class Certificat implements ICertificat {
  constructor(public id?: number, public nbrJours?: number, public description?: string | null, public visite?: IVisite) {}
}

export function getCertificatIdentifier(certificat: ICertificat): number | undefined {
  return certificat.id;
}

import { IVisite } from 'app/entities/visite/visite.model';
import { EchoAspect } from 'app/entities/enumerations/echo-aspect.model';

export interface ICrEchographie {
  id?: number;
  aspectFoie?: EchoAspect | null;
  observationFoie?: string | null;
  aspectVesicule?: EchoAspect | null;
  observationVesicule?: string | null;
  aspectTrocVoieVeine?: EchoAspect | null;
  observationTrocVoieVeine?: string | null;
  aspectReins?: EchoAspect | null;
  observationReins?: string | null;
  aspectRate?: EchoAspect | null;
  observationRate?: string | null;
  aspectPancreas?: EchoAspect | null;
  observationPancreas?: string | null;
  autreObservation?: string | null;
  visite?: IVisite;
}

export class CrEchographie implements ICrEchographie {
  constructor(
    public id?: number,
    public aspectFoie?: EchoAspect | null,
    public observationFoie?: string | null,
    public aspectVesicule?: EchoAspect | null,
    public observationVesicule?: string | null,
    public aspectTrocVoieVeine?: EchoAspect | null,
    public observationTrocVoieVeine?: string | null,
    public aspectReins?: EchoAspect | null,
    public observationReins?: string | null,
    public aspectRate?: EchoAspect | null,
    public observationRate?: string | null,
    public aspectPancreas?: EchoAspect | null,
    public observationPancreas?: string | null,
    public autreObservation?: string | null,
    public visite?: IVisite
  ) {}
}

export function getCrEchographieIdentifier(crEchographie: ICrEchographie): number | undefined {
  return crEchographie.id;
}

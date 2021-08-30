import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import * as dayjs from 'dayjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IReglement, getReglementIdentifier } from '../reglement.model';

export type EntityResponseType = HttpResponse<IReglement>;
export type EntityArrayResponseType = HttpResponse<IReglement[]>;

@Injectable({ providedIn: 'root' })
export class ReglementService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/reglements');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(reglement: IReglement): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(reglement);
    return this.http
      .post<IReglement>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  update(reglement: IReglement): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(reglement);
    return this.http
      .put<IReglement>(`${this.resourceUrl}/${getReglementIdentifier(reglement) as number}`, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  partialUpdate(reglement: IReglement): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(reglement);
    return this.http
      .patch<IReglement>(`${this.resourceUrl}/${getReglementIdentifier(reglement) as number}`, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<IReglement>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<IReglement[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addReglementToCollectionIfMissing(
    reglementCollection: IReglement[],
    ...reglementsToCheck: (IReglement | null | undefined)[]
  ): IReglement[] {
    const reglements: IReglement[] = reglementsToCheck.filter(isPresent);
    if (reglements.length > 0) {
      const reglementCollectionIdentifiers = reglementCollection.map(reglementItem => getReglementIdentifier(reglementItem)!);
      const reglementsToAdd = reglements.filter(reglementItem => {
        const reglementIdentifier = getReglementIdentifier(reglementItem);
        if (reglementIdentifier == null || reglementCollectionIdentifiers.includes(reglementIdentifier)) {
          return false;
        }
        reglementCollectionIdentifiers.push(reglementIdentifier);
        return true;
      });
      return [...reglementsToAdd, ...reglementCollection];
    }
    return reglementCollection;
  }

  protected convertDateFromClient(reglement: IReglement): IReglement {
    return Object.assign({}, reglement, {
      date: reglement.date?.isValid() ? reglement.date.toJSON() : undefined,
    });
  }

  protected convertDateFromServer(res: EntityResponseType): EntityResponseType {
    if (res.body) {
      res.body.date = res.body.date ? dayjs(res.body.date) : undefined;
    }
    return res;
  }

  protected convertDateArrayFromServer(res: EntityArrayResponseType): EntityArrayResponseType {
    if (res.body) {
      res.body.forEach((reglement: IReglement) => {
        reglement.date = reglement.date ? dayjs(reglement.date) : undefined;
      });
    }
    return res;
  }
}

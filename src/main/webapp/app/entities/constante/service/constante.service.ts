import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import * as dayjs from 'dayjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IConstante, getConstanteIdentifier } from '../constante.model';

export type EntityResponseType = HttpResponse<IConstante>;
export type EntityArrayResponseType = HttpResponse<IConstante[]>;

@Injectable({ providedIn: 'root' })
export class ConstanteService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/constantes');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(constante: IConstante): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(constante);
    return this.http
      .post<IConstante>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  update(constante: IConstante): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(constante);
    return this.http
      .put<IConstante>(`${this.resourceUrl}/${getConstanteIdentifier(constante) as number}`, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  partialUpdate(constante: IConstante): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(constante);
    return this.http
      .patch<IConstante>(`${this.resourceUrl}/${getConstanteIdentifier(constante) as number}`, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<IConstante>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<IConstante[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addConstanteToCollectionIfMissing(
    constanteCollection: IConstante[],
    ...constantesToCheck: (IConstante | null | undefined)[]
  ): IConstante[] {
    const constantes: IConstante[] = constantesToCheck.filter(isPresent);
    if (constantes.length > 0) {
      const constanteCollectionIdentifiers = constanteCollection.map(constanteItem => getConstanteIdentifier(constanteItem)!);
      const constantesToAdd = constantes.filter(constanteItem => {
        const constanteIdentifier = getConstanteIdentifier(constanteItem);
        if (constanteIdentifier == null || constanteCollectionIdentifiers.includes(constanteIdentifier)) {
          return false;
        }
        constanteCollectionIdentifiers.push(constanteIdentifier);
        return true;
      });
      return [...constantesToAdd, ...constanteCollection];
    }
    return constanteCollection;
  }

  protected convertDateFromClient(constante: IConstante): IConstante {
    return Object.assign({}, constante, {
      date: constante.date?.isValid() ? constante.date.toJSON() : undefined,
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
      res.body.forEach((constante: IConstante) => {
        constante.date = constante.date ? dayjs(constante.date) : undefined;
      });
    }
    return res;
  }
}

import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import * as dayjs from 'dayjs';

import { isPresent } from 'app/core/util/operators';
import { DATE_FORMAT } from 'app/config/input.constants';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { ICasTraiter, getCasTraiterIdentifier } from '../cas-traiter.model';

export type EntityResponseType = HttpResponse<ICasTraiter>;
export type EntityArrayResponseType = HttpResponse<ICasTraiter[]>;

@Injectable({ providedIn: 'root' })
export class CasTraiterService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/cas-traiters');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(casTraiter: ICasTraiter): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(casTraiter);
    return this.http
      .post<ICasTraiter>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  update(casTraiter: ICasTraiter): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(casTraiter);
    return this.http
      .put<ICasTraiter>(`${this.resourceUrl}/${getCasTraiterIdentifier(casTraiter) as number}`, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  partialUpdate(casTraiter: ICasTraiter): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(casTraiter);
    return this.http
      .patch<ICasTraiter>(`${this.resourceUrl}/${getCasTraiterIdentifier(casTraiter) as number}`, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<ICasTraiter>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<ICasTraiter[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addCasTraiterToCollectionIfMissing(
    casTraiterCollection: ICasTraiter[],
    ...casTraitersToCheck: (ICasTraiter | null | undefined)[]
  ): ICasTraiter[] {
    const casTraiters: ICasTraiter[] = casTraitersToCheck.filter(isPresent);
    if (casTraiters.length > 0) {
      const casTraiterCollectionIdentifiers = casTraiterCollection.map(casTraiterItem => getCasTraiterIdentifier(casTraiterItem)!);
      const casTraitersToAdd = casTraiters.filter(casTraiterItem => {
        const casTraiterIdentifier = getCasTraiterIdentifier(casTraiterItem);
        if (casTraiterIdentifier == null || casTraiterCollectionIdentifiers.includes(casTraiterIdentifier)) {
          return false;
        }
        casTraiterCollectionIdentifiers.push(casTraiterIdentifier);
        return true;
      });
      return [...casTraitersToAdd, ...casTraiterCollection];
    }
    return casTraiterCollection;
  }

  protected convertDateFromClient(casTraiter: ICasTraiter): ICasTraiter {
    return Object.assign({}, casTraiter, {
      depuis: casTraiter.depuis?.isValid() ? casTraiter.depuis.format(DATE_FORMAT) : undefined,
    });
  }

  protected convertDateFromServer(res: EntityResponseType): EntityResponseType {
    if (res.body) {
      res.body.depuis = res.body.depuis ? dayjs(res.body.depuis) : undefined;
    }
    return res;
  }

  protected convertDateArrayFromServer(res: EntityArrayResponseType): EntityArrayResponseType {
    if (res.body) {
      res.body.forEach((casTraiter: ICasTraiter) => {
        casTraiter.depuis = casTraiter.depuis ? dayjs(casTraiter.depuis) : undefined;
      });
    }
    return res;
  }
}

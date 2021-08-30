import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IOrdonnance, getOrdonnanceIdentifier } from '../ordonnance.model';

export type EntityResponseType = HttpResponse<IOrdonnance>;
export type EntityArrayResponseType = HttpResponse<IOrdonnance[]>;

@Injectable({ providedIn: 'root' })
export class OrdonnanceService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/ordonnances');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(ordonnance: IOrdonnance): Observable<EntityResponseType> {
    return this.http.post<IOrdonnance>(this.resourceUrl, ordonnance, { observe: 'response' });
  }

  update(ordonnance: IOrdonnance): Observable<EntityResponseType> {
    return this.http.put<IOrdonnance>(`${this.resourceUrl}/${getOrdonnanceIdentifier(ordonnance) as number}`, ordonnance, {
      observe: 'response',
    });
  }

  partialUpdate(ordonnance: IOrdonnance): Observable<EntityResponseType> {
    return this.http.patch<IOrdonnance>(`${this.resourceUrl}/${getOrdonnanceIdentifier(ordonnance) as number}`, ordonnance, {
      observe: 'response',
    });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IOrdonnance>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IOrdonnance[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addOrdonnanceToCollectionIfMissing(
    ordonnanceCollection: IOrdonnance[],
    ...ordonnancesToCheck: (IOrdonnance | null | undefined)[]
  ): IOrdonnance[] {
    const ordonnances: IOrdonnance[] = ordonnancesToCheck.filter(isPresent);
    if (ordonnances.length > 0) {
      const ordonnanceCollectionIdentifiers = ordonnanceCollection.map(ordonnanceItem => getOrdonnanceIdentifier(ordonnanceItem)!);
      const ordonnancesToAdd = ordonnances.filter(ordonnanceItem => {
        const ordonnanceIdentifier = getOrdonnanceIdentifier(ordonnanceItem);
        if (ordonnanceIdentifier == null || ordonnanceCollectionIdentifiers.includes(ordonnanceIdentifier)) {
          return false;
        }
        ordonnanceCollectionIdentifiers.push(ordonnanceIdentifier);
        return true;
      });
      return [...ordonnancesToAdd, ...ordonnanceCollection];
    }
    return ordonnanceCollection;
  }
}

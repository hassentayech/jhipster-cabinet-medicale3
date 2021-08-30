import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { ICrEchographie, getCrEchographieIdentifier } from '../cr-echographie.model';

export type EntityResponseType = HttpResponse<ICrEchographie>;
export type EntityArrayResponseType = HttpResponse<ICrEchographie[]>;

@Injectable({ providedIn: 'root' })
export class CrEchographieService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/cr-echographies');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(crEchographie: ICrEchographie): Observable<EntityResponseType> {
    return this.http.post<ICrEchographie>(this.resourceUrl, crEchographie, { observe: 'response' });
  }

  update(crEchographie: ICrEchographie): Observable<EntityResponseType> {
    return this.http.put<ICrEchographie>(`${this.resourceUrl}/${getCrEchographieIdentifier(crEchographie) as number}`, crEchographie, {
      observe: 'response',
    });
  }

  partialUpdate(crEchographie: ICrEchographie): Observable<EntityResponseType> {
    return this.http.patch<ICrEchographie>(`${this.resourceUrl}/${getCrEchographieIdentifier(crEchographie) as number}`, crEchographie, {
      observe: 'response',
    });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<ICrEchographie>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<ICrEchographie[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addCrEchographieToCollectionIfMissing(
    crEchographieCollection: ICrEchographie[],
    ...crEchographiesToCheck: (ICrEchographie | null | undefined)[]
  ): ICrEchographie[] {
    const crEchographies: ICrEchographie[] = crEchographiesToCheck.filter(isPresent);
    if (crEchographies.length > 0) {
      const crEchographieCollectionIdentifiers = crEchographieCollection.map(
        crEchographieItem => getCrEchographieIdentifier(crEchographieItem)!
      );
      const crEchographiesToAdd = crEchographies.filter(crEchographieItem => {
        const crEchographieIdentifier = getCrEchographieIdentifier(crEchographieItem);
        if (crEchographieIdentifier == null || crEchographieCollectionIdentifiers.includes(crEchographieIdentifier)) {
          return false;
        }
        crEchographieCollectionIdentifiers.push(crEchographieIdentifier);
        return true;
      });
      return [...crEchographiesToAdd, ...crEchographieCollection];
    }
    return crEchographieCollection;
  }
}

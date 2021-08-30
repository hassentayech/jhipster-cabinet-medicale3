import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IAntecedent, getAntecedentIdentifier } from '../antecedent.model';

export type EntityResponseType = HttpResponse<IAntecedent>;
export type EntityArrayResponseType = HttpResponse<IAntecedent[]>;

@Injectable({ providedIn: 'root' })
export class AntecedentService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/antecedents');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(antecedent: IAntecedent): Observable<EntityResponseType> {
    return this.http.post<IAntecedent>(this.resourceUrl, antecedent, { observe: 'response' });
  }

  update(antecedent: IAntecedent): Observable<EntityResponseType> {
    return this.http.put<IAntecedent>(`${this.resourceUrl}/${getAntecedentIdentifier(antecedent) as number}`, antecedent, {
      observe: 'response',
    });
  }

  partialUpdate(antecedent: IAntecedent): Observable<EntityResponseType> {
    return this.http.patch<IAntecedent>(`${this.resourceUrl}/${getAntecedentIdentifier(antecedent) as number}`, antecedent, {
      observe: 'response',
    });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IAntecedent>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IAntecedent[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addAntecedentToCollectionIfMissing(
    antecedentCollection: IAntecedent[],
    ...antecedentsToCheck: (IAntecedent | null | undefined)[]
  ): IAntecedent[] {
    const antecedents: IAntecedent[] = antecedentsToCheck.filter(isPresent);
    if (antecedents.length > 0) {
      const antecedentCollectionIdentifiers = antecedentCollection.map(antecedentItem => getAntecedentIdentifier(antecedentItem)!);
      const antecedentsToAdd = antecedents.filter(antecedentItem => {
        const antecedentIdentifier = getAntecedentIdentifier(antecedentItem);
        if (antecedentIdentifier == null || antecedentCollectionIdentifiers.includes(antecedentIdentifier)) {
          return false;
        }
        antecedentCollectionIdentifiers.push(antecedentIdentifier);
        return true;
      });
      return [...antecedentsToAdd, ...antecedentCollection];
    }
    return antecedentCollection;
  }
}

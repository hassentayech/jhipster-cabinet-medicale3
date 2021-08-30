import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { ICertificat, getCertificatIdentifier } from '../certificat.model';

export type EntityResponseType = HttpResponse<ICertificat>;
export type EntityArrayResponseType = HttpResponse<ICertificat[]>;

@Injectable({ providedIn: 'root' })
export class CertificatService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/certificats');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(certificat: ICertificat): Observable<EntityResponseType> {
    return this.http.post<ICertificat>(this.resourceUrl, certificat, { observe: 'response' });
  }

  update(certificat: ICertificat): Observable<EntityResponseType> {
    return this.http.put<ICertificat>(`${this.resourceUrl}/${getCertificatIdentifier(certificat) as number}`, certificat, {
      observe: 'response',
    });
  }

  partialUpdate(certificat: ICertificat): Observable<EntityResponseType> {
    return this.http.patch<ICertificat>(`${this.resourceUrl}/${getCertificatIdentifier(certificat) as number}`, certificat, {
      observe: 'response',
    });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<ICertificat>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<ICertificat[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addCertificatToCollectionIfMissing(
    certificatCollection: ICertificat[],
    ...certificatsToCheck: (ICertificat | null | undefined)[]
  ): ICertificat[] {
    const certificats: ICertificat[] = certificatsToCheck.filter(isPresent);
    if (certificats.length > 0) {
      const certificatCollectionIdentifiers = certificatCollection.map(certificatItem => getCertificatIdentifier(certificatItem)!);
      const certificatsToAdd = certificats.filter(certificatItem => {
        const certificatIdentifier = getCertificatIdentifier(certificatItem);
        if (certificatIdentifier == null || certificatCollectionIdentifiers.includes(certificatIdentifier)) {
          return false;
        }
        certificatCollectionIdentifiers.push(certificatIdentifier);
        return true;
      });
      return [...certificatsToAdd, ...certificatCollection];
    }
    return certificatCollection;
  }
}

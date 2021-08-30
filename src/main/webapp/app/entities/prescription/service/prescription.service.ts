import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IPrescription, getPrescriptionIdentifier } from '../prescription.model';

export type EntityResponseType = HttpResponse<IPrescription>;
export type EntityArrayResponseType = HttpResponse<IPrescription[]>;

@Injectable({ providedIn: 'root' })
export class PrescriptionService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/prescriptions');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(prescription: IPrescription): Observable<EntityResponseType> {
    return this.http.post<IPrescription>(this.resourceUrl, prescription, { observe: 'response' });
  }

  update(prescription: IPrescription): Observable<EntityResponseType> {
    return this.http.put<IPrescription>(`${this.resourceUrl}/${getPrescriptionIdentifier(prescription) as number}`, prescription, {
      observe: 'response',
    });
  }

  partialUpdate(prescription: IPrescription): Observable<EntityResponseType> {
    return this.http.patch<IPrescription>(`${this.resourceUrl}/${getPrescriptionIdentifier(prescription) as number}`, prescription, {
      observe: 'response',
    });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IPrescription>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IPrescription[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addPrescriptionToCollectionIfMissing(
    prescriptionCollection: IPrescription[],
    ...prescriptionsToCheck: (IPrescription | null | undefined)[]
  ): IPrescription[] {
    const prescriptions: IPrescription[] = prescriptionsToCheck.filter(isPresent);
    if (prescriptions.length > 0) {
      const prescriptionCollectionIdentifiers = prescriptionCollection.map(
        prescriptionItem => getPrescriptionIdentifier(prescriptionItem)!
      );
      const prescriptionsToAdd = prescriptions.filter(prescriptionItem => {
        const prescriptionIdentifier = getPrescriptionIdentifier(prescriptionItem);
        if (prescriptionIdentifier == null || prescriptionCollectionIdentifiers.includes(prescriptionIdentifier)) {
          return false;
        }
        prescriptionCollectionIdentifiers.push(prescriptionIdentifier);
        return true;
      });
      return [...prescriptionsToAdd, ...prescriptionCollection];
    }
    return prescriptionCollection;
  }
}

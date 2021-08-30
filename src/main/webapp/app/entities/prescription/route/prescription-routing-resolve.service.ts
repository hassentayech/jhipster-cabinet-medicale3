import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IPrescription, Prescription } from '../prescription.model';
import { PrescriptionService } from '../service/prescription.service';

@Injectable({ providedIn: 'root' })
export class PrescriptionRoutingResolveService implements Resolve<IPrescription> {
  constructor(protected service: PrescriptionService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IPrescription> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((prescription: HttpResponse<Prescription>) => {
          if (prescription.body) {
            return of(prescription.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new Prescription());
  }
}

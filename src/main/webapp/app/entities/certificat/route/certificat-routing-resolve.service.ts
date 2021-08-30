import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { ICertificat, Certificat } from '../certificat.model';
import { CertificatService } from '../service/certificat.service';

@Injectable({ providedIn: 'root' })
export class CertificatRoutingResolveService implements Resolve<ICertificat> {
  constructor(protected service: CertificatService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<ICertificat> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((certificat: HttpResponse<Certificat>) => {
          if (certificat.body) {
            return of(certificat.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new Certificat());
  }
}

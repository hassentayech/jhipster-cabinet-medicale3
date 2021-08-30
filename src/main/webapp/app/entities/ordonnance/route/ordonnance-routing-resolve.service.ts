import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IOrdonnance, Ordonnance } from '../ordonnance.model';
import { OrdonnanceService } from '../service/ordonnance.service';

@Injectable({ providedIn: 'root' })
export class OrdonnanceRoutingResolveService implements Resolve<IOrdonnance> {
  constructor(protected service: OrdonnanceService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IOrdonnance> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((ordonnance: HttpResponse<Ordonnance>) => {
          if (ordonnance.body) {
            return of(ordonnance.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new Ordonnance());
  }
}

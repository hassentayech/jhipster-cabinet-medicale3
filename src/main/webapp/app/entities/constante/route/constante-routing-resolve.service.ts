import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IConstante, Constante } from '../constante.model';
import { ConstanteService } from '../service/constante.service';

@Injectable({ providedIn: 'root' })
export class ConstanteRoutingResolveService implements Resolve<IConstante> {
  constructor(protected service: ConstanteService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IConstante> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((constante: HttpResponse<Constante>) => {
          if (constante.body) {
            return of(constante.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new Constante());
  }
}

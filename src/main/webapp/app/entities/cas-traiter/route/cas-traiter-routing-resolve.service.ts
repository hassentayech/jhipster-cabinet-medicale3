import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { ICasTraiter, CasTraiter } from '../cas-traiter.model';
import { CasTraiterService } from '../service/cas-traiter.service';

@Injectable({ providedIn: 'root' })
export class CasTraiterRoutingResolveService implements Resolve<ICasTraiter> {
  constructor(protected service: CasTraiterService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<ICasTraiter> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((casTraiter: HttpResponse<CasTraiter>) => {
          if (casTraiter.body) {
            return of(casTraiter.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new CasTraiter());
  }
}

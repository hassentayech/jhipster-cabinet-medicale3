import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { ICrEchographie, CrEchographie } from '../cr-echographie.model';
import { CrEchographieService } from '../service/cr-echographie.service';

@Injectable({ providedIn: 'root' })
export class CrEchographieRoutingResolveService implements Resolve<ICrEchographie> {
  constructor(protected service: CrEchographieService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<ICrEchographie> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((crEchographie: HttpResponse<CrEchographie>) => {
          if (crEchographie.body) {
            return of(crEchographie.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new CrEchographie());
  }
}

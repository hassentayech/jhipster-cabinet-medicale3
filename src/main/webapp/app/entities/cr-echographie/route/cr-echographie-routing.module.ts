import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { CrEchographieComponent } from '../list/cr-echographie.component';
import { CrEchographieDetailComponent } from '../detail/cr-echographie-detail.component';
import { CrEchographieUpdateComponent } from '../update/cr-echographie-update.component';
import { CrEchographieRoutingResolveService } from './cr-echographie-routing-resolve.service';

const crEchographieRoute: Routes = [
  {
    path: '',
    component: CrEchographieComponent,
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: CrEchographieDetailComponent,
    resolve: {
      crEchographie: CrEchographieRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: CrEchographieUpdateComponent,
    resolve: {
      crEchographie: CrEchographieRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: CrEchographieUpdateComponent,
    resolve: {
      crEchographie: CrEchographieRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(crEchographieRoute)],
  exports: [RouterModule],
})
export class CrEchographieRoutingModule {}

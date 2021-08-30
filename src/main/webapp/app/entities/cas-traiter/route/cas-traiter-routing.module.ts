import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { CasTraiterComponent } from '../list/cas-traiter.component';
import { CasTraiterDetailComponent } from '../detail/cas-traiter-detail.component';
import { CasTraiterUpdateComponent } from '../update/cas-traiter-update.component';
import { CasTraiterRoutingResolveService } from './cas-traiter-routing-resolve.service';

const casTraiterRoute: Routes = [
  {
    path: '',
    component: CasTraiterComponent,
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: CasTraiterDetailComponent,
    resolve: {
      casTraiter: CasTraiterRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: CasTraiterUpdateComponent,
    resolve: {
      casTraiter: CasTraiterRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: CasTraiterUpdateComponent,
    resolve: {
      casTraiter: CasTraiterRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(casTraiterRoute)],
  exports: [RouterModule],
})
export class CasTraiterRoutingModule {}

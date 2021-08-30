import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { CertificatComponent } from '../list/certificat.component';
import { CertificatDetailComponent } from '../detail/certificat-detail.component';
import { CertificatUpdateComponent } from '../update/certificat-update.component';
import { CertificatRoutingResolveService } from './certificat-routing-resolve.service';

const certificatRoute: Routes = [
  {
    path: '',
    component: CertificatComponent,
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: CertificatDetailComponent,
    resolve: {
      certificat: CertificatRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: CertificatUpdateComponent,
    resolve: {
      certificat: CertificatRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: CertificatUpdateComponent,
    resolve: {
      certificat: CertificatRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(certificatRoute)],
  exports: [RouterModule],
})
export class CertificatRoutingModule {}

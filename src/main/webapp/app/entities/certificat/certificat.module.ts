import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { CertificatComponent } from './list/certificat.component';
import { CertificatDetailComponent } from './detail/certificat-detail.component';
import { CertificatUpdateComponent } from './update/certificat-update.component';
import { CertificatDeleteDialogComponent } from './delete/certificat-delete-dialog.component';
import { CertificatRoutingModule } from './route/certificat-routing.module';

@NgModule({
  imports: [SharedModule, CertificatRoutingModule],
  declarations: [CertificatComponent, CertificatDetailComponent, CertificatUpdateComponent, CertificatDeleteDialogComponent],
  entryComponents: [CertificatDeleteDialogComponent],
})
export class CertificatModule {}

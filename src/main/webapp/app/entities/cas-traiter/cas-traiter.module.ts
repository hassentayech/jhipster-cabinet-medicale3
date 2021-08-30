import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { CasTraiterComponent } from './list/cas-traiter.component';
import { CasTraiterDetailComponent } from './detail/cas-traiter-detail.component';
import { CasTraiterUpdateComponent } from './update/cas-traiter-update.component';
import { CasTraiterDeleteDialogComponent } from './delete/cas-traiter-delete-dialog.component';
import { CasTraiterRoutingModule } from './route/cas-traiter-routing.module';

@NgModule({
  imports: [SharedModule, CasTraiterRoutingModule],
  declarations: [CasTraiterComponent, CasTraiterDetailComponent, CasTraiterUpdateComponent, CasTraiterDeleteDialogComponent],
  entryComponents: [CasTraiterDeleteDialogComponent],
})
export class CasTraiterModule {}

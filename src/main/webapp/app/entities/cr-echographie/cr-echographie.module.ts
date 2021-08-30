import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { CrEchographieComponent } from './list/cr-echographie.component';
import { CrEchographieDetailComponent } from './detail/cr-echographie-detail.component';
import { CrEchographieUpdateComponent } from './update/cr-echographie-update.component';
import { CrEchographieDeleteDialogComponent } from './delete/cr-echographie-delete-dialog.component';
import { CrEchographieRoutingModule } from './route/cr-echographie-routing.module';

@NgModule({
  imports: [SharedModule, CrEchographieRoutingModule],
  declarations: [CrEchographieComponent, CrEchographieDetailComponent, CrEchographieUpdateComponent, CrEchographieDeleteDialogComponent],
  entryComponents: [CrEchographieDeleteDialogComponent],
})
export class CrEchographieModule {}

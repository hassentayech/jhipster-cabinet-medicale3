import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { ICrEchographie } from '../cr-echographie.model';
import { CrEchographieService } from '../service/cr-echographie.service';

@Component({
  templateUrl: './cr-echographie-delete-dialog.component.html',
})
export class CrEchographieDeleteDialogComponent {
  crEchographie?: ICrEchographie;

  constructor(protected crEchographieService: CrEchographieService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.crEchographieService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}

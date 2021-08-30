import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IReglement } from '../reglement.model';
import { ReglementService } from '../service/reglement.service';

@Component({
  templateUrl: './reglement-delete-dialog.component.html',
})
export class ReglementDeleteDialogComponent {
  reglement?: IReglement;

  constructor(protected reglementService: ReglementService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.reglementService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}

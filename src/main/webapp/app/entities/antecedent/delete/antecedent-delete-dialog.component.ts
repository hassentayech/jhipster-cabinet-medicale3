import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IAntecedent } from '../antecedent.model';
import { AntecedentService } from '../service/antecedent.service';

@Component({
  templateUrl: './antecedent-delete-dialog.component.html',
})
export class AntecedentDeleteDialogComponent {
  antecedent?: IAntecedent;

  constructor(protected antecedentService: AntecedentService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.antecedentService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}

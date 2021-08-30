import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IConstante } from '../constante.model';
import { ConstanteService } from '../service/constante.service';

@Component({
  templateUrl: './constante-delete-dialog.component.html',
})
export class ConstanteDeleteDialogComponent {
  constante?: IConstante;

  constructor(protected constanteService: ConstanteService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.constanteService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}

import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { ICasTraiter } from '../cas-traiter.model';
import { CasTraiterService } from '../service/cas-traiter.service';

@Component({
  templateUrl: './cas-traiter-delete-dialog.component.html',
})
export class CasTraiterDeleteDialogComponent {
  casTraiter?: ICasTraiter;

  constructor(protected casTraiterService: CasTraiterService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.casTraiterService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}

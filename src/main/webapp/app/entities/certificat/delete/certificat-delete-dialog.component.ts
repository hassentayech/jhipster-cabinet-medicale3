import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { ICertificat } from '../certificat.model';
import { CertificatService } from '../service/certificat.service';

@Component({
  templateUrl: './certificat-delete-dialog.component.html',
})
export class CertificatDeleteDialogComponent {
  certificat?: ICertificat;

  constructor(protected certificatService: CertificatService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.certificatService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}

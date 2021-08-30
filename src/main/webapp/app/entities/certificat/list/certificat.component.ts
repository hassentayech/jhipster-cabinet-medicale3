import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { ICertificat } from '../certificat.model';
import { CertificatService } from '../service/certificat.service';
import { CertificatDeleteDialogComponent } from '../delete/certificat-delete-dialog.component';

@Component({
  selector: 'jhi-certificat',
  templateUrl: './certificat.component.html',
})
export class CertificatComponent implements OnInit {
  certificats?: ICertificat[];
  isLoading = false;

  constructor(protected certificatService: CertificatService, protected modalService: NgbModal) {}

  loadAll(): void {
    this.isLoading = true;

    this.certificatService.query().subscribe(
      (res: HttpResponse<ICertificat[]>) => {
        this.isLoading = false;
        this.certificats = res.body ?? [];
      },
      () => {
        this.isLoading = false;
      }
    );
  }

  ngOnInit(): void {
    this.loadAll();
  }

  trackId(index: number, item: ICertificat): number {
    return item.id!;
  }

  delete(certificat: ICertificat): void {
    const modalRef = this.modalService.open(CertificatDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.certificat = certificat;
    // unsubscribe not needed because closed completes on modal close
    modalRef.closed.subscribe(reason => {
      if (reason === 'deleted') {
        this.loadAll();
      }
    });
  }
}

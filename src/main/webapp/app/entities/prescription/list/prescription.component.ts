import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { IPrescription } from '../prescription.model';
import { PrescriptionService } from '../service/prescription.service';
import { PrescriptionDeleteDialogComponent } from '../delete/prescription-delete-dialog.component';

@Component({
  selector: 'jhi-prescription',
  templateUrl: './prescription.component.html',
})
export class PrescriptionComponent implements OnInit {
  prescriptions?: IPrescription[];
  isLoading = false;

  constructor(protected prescriptionService: PrescriptionService, protected modalService: NgbModal) {}

  loadAll(): void {
    this.isLoading = true;

    this.prescriptionService.query().subscribe(
      (res: HttpResponse<IPrescription[]>) => {
        this.isLoading = false;
        this.prescriptions = res.body ?? [];
      },
      () => {
        this.isLoading = false;
      }
    );
  }

  ngOnInit(): void {
    this.loadAll();
  }

  trackId(index: number, item: IPrescription): number {
    return item.id!;
  }

  delete(prescription: IPrescription): void {
    const modalRef = this.modalService.open(PrescriptionDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.prescription = prescription;
    // unsubscribe not needed because closed completes on modal close
    modalRef.closed.subscribe(reason => {
      if (reason === 'deleted') {
        this.loadAll();
      }
    });
  }
}

import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { IOrdonnance } from '../ordonnance.model';
import { OrdonnanceService } from '../service/ordonnance.service';
import { OrdonnanceDeleteDialogComponent } from '../delete/ordonnance-delete-dialog.component';

@Component({
  selector: 'jhi-ordonnance',
  templateUrl: './ordonnance.component.html',
})
export class OrdonnanceComponent implements OnInit {
  ordonnances?: IOrdonnance[];
  isLoading = false;

  constructor(protected ordonnanceService: OrdonnanceService, protected modalService: NgbModal) {}

  loadAll(): void {
    this.isLoading = true;

    this.ordonnanceService.query().subscribe(
      (res: HttpResponse<IOrdonnance[]>) => {
        this.isLoading = false;
        this.ordonnances = res.body ?? [];
      },
      () => {
        this.isLoading = false;
      }
    );
  }

  ngOnInit(): void {
    this.loadAll();
  }

  trackId(index: number, item: IOrdonnance): number {
    return item.id!;
  }

  delete(ordonnance: IOrdonnance): void {
    const modalRef = this.modalService.open(OrdonnanceDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.ordonnance = ordonnance;
    // unsubscribe not needed because closed completes on modal close
    modalRef.closed.subscribe(reason => {
      if (reason === 'deleted') {
        this.loadAll();
      }
    });
  }
}

import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { IReglement } from '../reglement.model';
import { ReglementService } from '../service/reglement.service';
import { ReglementDeleteDialogComponent } from '../delete/reglement-delete-dialog.component';

@Component({
  selector: 'jhi-reglement',
  templateUrl: './reglement.component.html',
})
export class ReglementComponent implements OnInit {
  reglements?: IReglement[];
  isLoading = false;

  constructor(protected reglementService: ReglementService, protected modalService: NgbModal) {}

  loadAll(): void {
    this.isLoading = true;

    this.reglementService.query().subscribe(
      (res: HttpResponse<IReglement[]>) => {
        this.isLoading = false;
        this.reglements = res.body ?? [];
      },
      () => {
        this.isLoading = false;
      }
    );
  }

  ngOnInit(): void {
    this.loadAll();
  }

  trackId(index: number, item: IReglement): number {
    return item.id!;
  }

  delete(reglement: IReglement): void {
    const modalRef = this.modalService.open(ReglementDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.reglement = reglement;
    // unsubscribe not needed because closed completes on modal close
    modalRef.closed.subscribe(reason => {
      if (reason === 'deleted') {
        this.loadAll();
      }
    });
  }
}

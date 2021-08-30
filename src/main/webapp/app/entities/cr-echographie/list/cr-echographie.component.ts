import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { ICrEchographie } from '../cr-echographie.model';
import { CrEchographieService } from '../service/cr-echographie.service';
import { CrEchographieDeleteDialogComponent } from '../delete/cr-echographie-delete-dialog.component';

@Component({
  selector: 'jhi-cr-echographie',
  templateUrl: './cr-echographie.component.html',
})
export class CrEchographieComponent implements OnInit {
  crEchographies?: ICrEchographie[];
  isLoading = false;

  constructor(protected crEchographieService: CrEchographieService, protected modalService: NgbModal) {}

  loadAll(): void {
    this.isLoading = true;

    this.crEchographieService.query().subscribe(
      (res: HttpResponse<ICrEchographie[]>) => {
        this.isLoading = false;
        this.crEchographies = res.body ?? [];
      },
      () => {
        this.isLoading = false;
      }
    );
  }

  ngOnInit(): void {
    this.loadAll();
  }

  trackId(index: number, item: ICrEchographie): number {
    return item.id!;
  }

  delete(crEchographie: ICrEchographie): void {
    const modalRef = this.modalService.open(CrEchographieDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.crEchographie = crEchographie;
    // unsubscribe not needed because closed completes on modal close
    modalRef.closed.subscribe(reason => {
      if (reason === 'deleted') {
        this.loadAll();
      }
    });
  }
}

import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { IAntecedent } from '../antecedent.model';
import { AntecedentService } from '../service/antecedent.service';
import { AntecedentDeleteDialogComponent } from '../delete/antecedent-delete-dialog.component';
import { DataUtils } from 'app/core/util/data-util.service';

@Component({
  selector: 'jhi-antecedent',
  templateUrl: './antecedent.component.html',
})
export class AntecedentComponent implements OnInit {
  antecedents?: IAntecedent[];
  isLoading = false;

  constructor(protected antecedentService: AntecedentService, protected dataUtils: DataUtils, protected modalService: NgbModal) {}

  loadAll(): void {
    this.isLoading = true;

    this.antecedentService.query().subscribe(
      (res: HttpResponse<IAntecedent[]>) => {
        this.isLoading = false;
        this.antecedents = res.body ?? [];
      },
      () => {
        this.isLoading = false;
      }
    );
  }

  ngOnInit(): void {
    this.loadAll();
  }

  trackId(index: number, item: IAntecedent): number {
    return item.id!;
  }

  byteSize(base64String: string): string {
    return this.dataUtils.byteSize(base64String);
  }

  openFile(base64String: string, contentType: string | null | undefined): void {
    return this.dataUtils.openFile(base64String, contentType);
  }

  delete(antecedent: IAntecedent): void {
    const modalRef = this.modalService.open(AntecedentDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.antecedent = antecedent;
    // unsubscribe not needed because closed completes on modal close
    modalRef.closed.subscribe(reason => {
      if (reason === 'deleted') {
        this.loadAll();
      }
    });
  }
}

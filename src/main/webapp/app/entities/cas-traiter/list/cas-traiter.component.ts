import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { ICasTraiter } from '../cas-traiter.model';
import { CasTraiterService } from '../service/cas-traiter.service';
import { CasTraiterDeleteDialogComponent } from '../delete/cas-traiter-delete-dialog.component';
import { DataUtils } from 'app/core/util/data-util.service';

@Component({
  selector: 'jhi-cas-traiter',
  templateUrl: './cas-traiter.component.html',
})
export class CasTraiterComponent implements OnInit {
  casTraiters?: ICasTraiter[];
  isLoading = false;

  constructor(protected casTraiterService: CasTraiterService, protected dataUtils: DataUtils, protected modalService: NgbModal) {}

  loadAll(): void {
    this.isLoading = true;

    this.casTraiterService.query().subscribe(
      (res: HttpResponse<ICasTraiter[]>) => {
        this.isLoading = false;
        this.casTraiters = res.body ?? [];
      },
      () => {
        this.isLoading = false;
      }
    );
  }

  ngOnInit(): void {
    this.loadAll();
  }

  trackId(index: number, item: ICasTraiter): number {
    return item.id!;
  }

  byteSize(base64String: string): string {
    return this.dataUtils.byteSize(base64String);
  }

  openFile(base64String: string, contentType: string | null | undefined): void {
    return this.dataUtils.openFile(base64String, contentType);
  }

  delete(casTraiter: ICasTraiter): void {
    const modalRef = this.modalService.open(CasTraiterDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.casTraiter = casTraiter;
    // unsubscribe not needed because closed completes on modal close
    modalRef.closed.subscribe(reason => {
      if (reason === 'deleted') {
        this.loadAll();
      }
    });
  }
}

import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { IVisite } from '../visite.model';
import { VisiteService } from '../service/visite.service';
import { VisiteDeleteDialogComponent } from '../delete/visite-delete-dialog.component';
import { DataUtils } from 'app/core/util/data-util.service';

@Component({
  selector: 'jhi-visite',
  templateUrl: './visite.component.html',
})
export class VisiteComponent implements OnInit {
  visites?: IVisite[];
  isLoading = false;

  constructor(protected visiteService: VisiteService, protected dataUtils: DataUtils, protected modalService: NgbModal) {}

  loadAll(): void {
    this.isLoading = true;

    this.visiteService.query().subscribe(
      (res: HttpResponse<IVisite[]>) => {
        this.isLoading = false;
        this.visites = res.body ?? [];
      },
      () => {
        this.isLoading = false;
      }
    );
  }

  ngOnInit(): void {
    this.loadAll();
  }

  trackId(index: number, item: IVisite): number {
    return item.id!;
  }

  byteSize(base64String: string): string {
    return this.dataUtils.byteSize(base64String);
  }

  openFile(base64String: string, contentType: string | null | undefined): void {
    return this.dataUtils.openFile(base64String, contentType);
  }

  delete(visite: IVisite): void {
    const modalRef = this.modalService.open(VisiteDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.visite = visite;
    // unsubscribe not needed because closed completes on modal close
    modalRef.closed.subscribe(reason => {
      if (reason === 'deleted') {
        this.loadAll();
      }
    });
  }
}

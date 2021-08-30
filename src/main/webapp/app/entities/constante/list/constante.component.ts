import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { IConstante } from '../constante.model';
import { ConstanteService } from '../service/constante.service';
import { ConstanteDeleteDialogComponent } from '../delete/constante-delete-dialog.component';

@Component({
  selector: 'jhi-constante',
  templateUrl: './constante.component.html',
})
export class ConstanteComponent implements OnInit {
  constantes?: IConstante[];
  isLoading = false;

  constructor(protected constanteService: ConstanteService, protected modalService: NgbModal) {}

  loadAll(): void {
    this.isLoading = true;

    this.constanteService.query().subscribe(
      (res: HttpResponse<IConstante[]>) => {
        this.isLoading = false;
        this.constantes = res.body ?? [];
      },
      () => {
        this.isLoading = false;
      }
    );
  }

  ngOnInit(): void {
    this.loadAll();
  }

  trackId(index: number, item: IConstante): number {
    return item.id!;
  }

  delete(constante: IConstante): void {
    const modalRef = this.modalService.open(ConstanteDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.constante = constante;
    // unsubscribe not needed because closed completes on modal close
    modalRef.closed.subscribe(reason => {
      if (reason === 'deleted') {
        this.loadAll();
      }
    });
  }
}

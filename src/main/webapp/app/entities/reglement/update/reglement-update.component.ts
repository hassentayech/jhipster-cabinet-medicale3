import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import * as dayjs from 'dayjs';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';

import { IReglement, Reglement } from '../reglement.model';
import { ReglementService } from '../service/reglement.service';
import { IVisite } from 'app/entities/visite/visite.model';
import { VisiteService } from 'app/entities/visite/service/visite.service';

@Component({
  selector: 'jhi-reglement-update',
  templateUrl: './reglement-update.component.html',
})
export class ReglementUpdateComponent implements OnInit {
  isSaving = false;

  visitesSharedCollection: IVisite[] = [];

  editForm = this.fb.group({
    id: [],
    date: [null, [Validators.required]],
    valeur: [null, [Validators.required]],
    typePayement: [null, [Validators.required]],
    remarque: [],
    visite: [null, Validators.required],
  });

  constructor(
    protected reglementService: ReglementService,
    protected visiteService: VisiteService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ reglement }) => {
      if (reglement.id === undefined) {
        const today = dayjs().startOf('day');
        reglement.date = today;
      }

      this.updateForm(reglement);

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const reglement = this.createFromForm();
    if (reglement.id !== undefined) {
      this.subscribeToSaveResponse(this.reglementService.update(reglement));
    } else {
      this.subscribeToSaveResponse(this.reglementService.create(reglement));
    }
  }

  trackVisiteById(index: number, item: IVisite): number {
    return item.id!;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IReglement>>): void {
    result.pipe(finalize(() => this.onSaveFinalize())).subscribe(
      () => this.onSaveSuccess(),
      () => this.onSaveError()
    );
  }

  protected onSaveSuccess(): void {
    this.previousState();
  }

  protected onSaveError(): void {
    // Api for inheritance.
  }

  protected onSaveFinalize(): void {
    this.isSaving = false;
  }

  protected updateForm(reglement: IReglement): void {
    this.editForm.patchValue({
      id: reglement.id,
      date: reglement.date ? reglement.date.format(DATE_TIME_FORMAT) : null,
      valeur: reglement.valeur,
      typePayement: reglement.typePayement,
      remarque: reglement.remarque,
      visite: reglement.visite,
    });

    this.visitesSharedCollection = this.visiteService.addVisiteToCollectionIfMissing(this.visitesSharedCollection, reglement.visite);
  }

  protected loadRelationshipsOptions(): void {
    this.visiteService
      .query()
      .pipe(map((res: HttpResponse<IVisite[]>) => res.body ?? []))
      .pipe(map((visites: IVisite[]) => this.visiteService.addVisiteToCollectionIfMissing(visites, this.editForm.get('visite')!.value)))
      .subscribe((visites: IVisite[]) => (this.visitesSharedCollection = visites));
  }

  protected createFromForm(): IReglement {
    return {
      ...new Reglement(),
      id: this.editForm.get(['id'])!.value,
      date: this.editForm.get(['date'])!.value ? dayjs(this.editForm.get(['date'])!.value, DATE_TIME_FORMAT) : undefined,
      valeur: this.editForm.get(['valeur'])!.value,
      typePayement: this.editForm.get(['typePayement'])!.value,
      remarque: this.editForm.get(['remarque'])!.value,
      visite: this.editForm.get(['visite'])!.value,
    };
  }
}

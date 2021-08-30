import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { IOrdonnance, Ordonnance } from '../ordonnance.model';
import { OrdonnanceService } from '../service/ordonnance.service';
import { IVisite } from 'app/entities/visite/visite.model';
import { VisiteService } from 'app/entities/visite/service/visite.service';

@Component({
  selector: 'jhi-ordonnance-update',
  templateUrl: './ordonnance-update.component.html',
})
export class OrdonnanceUpdateComponent implements OnInit {
  isSaving = false;

  visitesSharedCollection: IVisite[] = [];

  editForm = this.fb.group({
    id: [],
    visite: [null, Validators.required],
  });

  constructor(
    protected ordonnanceService: OrdonnanceService,
    protected visiteService: VisiteService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ ordonnance }) => {
      this.updateForm(ordonnance);

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const ordonnance = this.createFromForm();
    if (ordonnance.id !== undefined) {
      this.subscribeToSaveResponse(this.ordonnanceService.update(ordonnance));
    } else {
      this.subscribeToSaveResponse(this.ordonnanceService.create(ordonnance));
    }
  }

  trackVisiteById(index: number, item: IVisite): number {
    return item.id!;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IOrdonnance>>): void {
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

  protected updateForm(ordonnance: IOrdonnance): void {
    this.editForm.patchValue({
      id: ordonnance.id,
      visite: ordonnance.visite,
    });

    this.visitesSharedCollection = this.visiteService.addVisiteToCollectionIfMissing(this.visitesSharedCollection, ordonnance.visite);
  }

  protected loadRelationshipsOptions(): void {
    this.visiteService
      .query()
      .pipe(map((res: HttpResponse<IVisite[]>) => res.body ?? []))
      .pipe(map((visites: IVisite[]) => this.visiteService.addVisiteToCollectionIfMissing(visites, this.editForm.get('visite')!.value)))
      .subscribe((visites: IVisite[]) => (this.visitesSharedCollection = visites));
  }

  protected createFromForm(): IOrdonnance {
    return {
      ...new Ordonnance(),
      id: this.editForm.get(['id'])!.value,
      visite: this.editForm.get(['visite'])!.value,
    };
  }
}

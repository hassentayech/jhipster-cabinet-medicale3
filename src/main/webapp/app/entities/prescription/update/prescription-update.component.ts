import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { IPrescription, Prescription } from '../prescription.model';
import { PrescriptionService } from '../service/prescription.service';
import { IOrdonnance } from 'app/entities/ordonnance/ordonnance.model';
import { OrdonnanceService } from 'app/entities/ordonnance/service/ordonnance.service';

@Component({
  selector: 'jhi-prescription-update',
  templateUrl: './prescription-update.component.html',
})
export class PrescriptionUpdateComponent implements OnInit {
  isSaving = false;

  ordonnancesSharedCollection: IOrdonnance[] = [];

  editForm = this.fb.group({
    id: [],
    prescription: [null, [Validators.required]],
    prise: [],
    ordonnance: [null, Validators.required],
  });

  constructor(
    protected prescriptionService: PrescriptionService,
    protected ordonnanceService: OrdonnanceService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ prescription }) => {
      this.updateForm(prescription);

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const prescription = this.createFromForm();
    if (prescription.id !== undefined) {
      this.subscribeToSaveResponse(this.prescriptionService.update(prescription));
    } else {
      this.subscribeToSaveResponse(this.prescriptionService.create(prescription));
    }
  }

  trackOrdonnanceById(index: number, item: IOrdonnance): number {
    return item.id!;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IPrescription>>): void {
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

  protected updateForm(prescription: IPrescription): void {
    this.editForm.patchValue({
      id: prescription.id,
      prescription: prescription.prescription,
      prise: prescription.prise,
      ordonnance: prescription.ordonnance,
    });

    this.ordonnancesSharedCollection = this.ordonnanceService.addOrdonnanceToCollectionIfMissing(
      this.ordonnancesSharedCollection,
      prescription.ordonnance
    );
  }

  protected loadRelationshipsOptions(): void {
    this.ordonnanceService
      .query()
      .pipe(map((res: HttpResponse<IOrdonnance[]>) => res.body ?? []))
      .pipe(
        map((ordonnances: IOrdonnance[]) =>
          this.ordonnanceService.addOrdonnanceToCollectionIfMissing(ordonnances, this.editForm.get('ordonnance')!.value)
        )
      )
      .subscribe((ordonnances: IOrdonnance[]) => (this.ordonnancesSharedCollection = ordonnances));
  }

  protected createFromForm(): IPrescription {
    return {
      ...new Prescription(),
      id: this.editForm.get(['id'])!.value,
      prescription: this.editForm.get(['prescription'])!.value,
      prise: this.editForm.get(['prise'])!.value,
      ordonnance: this.editForm.get(['ordonnance'])!.value,
    };
  }
}

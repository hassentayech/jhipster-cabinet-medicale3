import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import * as dayjs from 'dayjs';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';

import { IConstante, Constante } from '../constante.model';
import { ConstanteService } from '../service/constante.service';
import { IPatient } from 'app/entities/patient/patient.model';
import { PatientService } from 'app/entities/patient/service/patient.service';

@Component({
  selector: 'jhi-constante-update',
  templateUrl: './constante-update.component.html',
})
export class ConstanteUpdateComponent implements OnInit {
  isSaving = false;

  patientsSharedCollection: IPatient[] = [];

  editForm = this.fb.group({
    id: [],
    date: [null, [Validators.required]],
    poid: [],
    taille: [],
    pas: [],
    pad: [],
    pouls: [],
    temp: [],
    glycemie: [],
    cholesterol: [],
    patient: [null, Validators.required],
  });

  constructor(
    protected constanteService: ConstanteService,
    protected patientService: PatientService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ constante }) => {
      if (constante.id === undefined) {
        const today = dayjs().startOf('day');
        constante.date = today;
      }

      this.updateForm(constante);

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const constante = this.createFromForm();
    if (constante.id !== undefined) {
      this.subscribeToSaveResponse(this.constanteService.update(constante));
    } else {
      this.subscribeToSaveResponse(this.constanteService.create(constante));
    }
  }

  trackPatientById(index: number, item: IPatient): number {
    return item.id!;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IConstante>>): void {
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

  protected updateForm(constante: IConstante): void {
    this.editForm.patchValue({
      id: constante.id,
      date: constante.date ? constante.date.format(DATE_TIME_FORMAT) : null,
      poid: constante.poid,
      taille: constante.taille,
      pas: constante.pas,
      pad: constante.pad,
      pouls: constante.pouls,
      temp: constante.temp,
      glycemie: constante.glycemie,
      cholesterol: constante.cholesterol,
      patient: constante.patient,
    });

    this.patientsSharedCollection = this.patientService.addPatientToCollectionIfMissing(this.patientsSharedCollection, constante.patient);
  }

  protected loadRelationshipsOptions(): void {
    this.patientService
      .query()
      .pipe(map((res: HttpResponse<IPatient[]>) => res.body ?? []))
      .pipe(
        map((patients: IPatient[]) => this.patientService.addPatientToCollectionIfMissing(patients, this.editForm.get('patient')!.value))
      )
      .subscribe((patients: IPatient[]) => (this.patientsSharedCollection = patients));
  }

  protected createFromForm(): IConstante {
    return {
      ...new Constante(),
      id: this.editForm.get(['id'])!.value,
      date: this.editForm.get(['date'])!.value ? dayjs(this.editForm.get(['date'])!.value, DATE_TIME_FORMAT) : undefined,
      poid: this.editForm.get(['poid'])!.value,
      taille: this.editForm.get(['taille'])!.value,
      pas: this.editForm.get(['pas'])!.value,
      pad: this.editForm.get(['pad'])!.value,
      pouls: this.editForm.get(['pouls'])!.value,
      temp: this.editForm.get(['temp'])!.value,
      glycemie: this.editForm.get(['glycemie'])!.value,
      cholesterol: this.editForm.get(['cholesterol'])!.value,
      patient: this.editForm.get(['patient'])!.value,
    };
  }
}

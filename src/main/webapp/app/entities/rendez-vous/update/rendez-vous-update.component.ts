import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { IRendezVous, RendezVous } from '../rendez-vous.model';
import { RendezVousService } from '../service/rendez-vous.service';
import { IPatient } from 'app/entities/patient/patient.model';
import { PatientService } from 'app/entities/patient/service/patient.service';

@Component({
  selector: 'jhi-rendez-vous-update',
  templateUrl: './rendez-vous-update.component.html',
})
export class RendezVousUpdateComponent implements OnInit {
  isSaving = false;

  patientsSharedCollection: IPatient[] = [];

  editForm = this.fb.group({
    id: [],
    date: [null, [Validators.required]],
    trancheHoraire: [null, [Validators.required]],
    nbrTranche: [null, [Validators.required]],
    motif: [],
    present: [],
    patient: [null, Validators.required],
  });

  constructor(
    protected rendezVousService: RendezVousService,
    protected patientService: PatientService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ rendezVous }) => {
      this.updateForm(rendezVous);

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const rendezVous = this.createFromForm();
    if (rendezVous.id !== undefined) {
      this.subscribeToSaveResponse(this.rendezVousService.update(rendezVous));
    } else {
      this.subscribeToSaveResponse(this.rendezVousService.create(rendezVous));
    }
  }

  trackPatientById(index: number, item: IPatient): number {
    return item.id!;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IRendezVous>>): void {
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

  protected updateForm(rendezVous: IRendezVous): void {
    this.editForm.patchValue({
      id: rendezVous.id,
      date: rendezVous.date,
      trancheHoraire: rendezVous.trancheHoraire,
      nbrTranche: rendezVous.nbrTranche,
      motif: rendezVous.motif,
      present: rendezVous.present,
      patient: rendezVous.patient,
    });

    this.patientsSharedCollection = this.patientService.addPatientToCollectionIfMissing(this.patientsSharedCollection, rendezVous.patient);
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

  protected createFromForm(): IRendezVous {
    return {
      ...new RendezVous(),
      id: this.editForm.get(['id'])!.value,
      date: this.editForm.get(['date'])!.value,
      trancheHoraire: this.editForm.get(['trancheHoraire'])!.value,
      nbrTranche: this.editForm.get(['nbrTranche'])!.value,
      motif: this.editForm.get(['motif'])!.value,
      present: this.editForm.get(['present'])!.value,
      patient: this.editForm.get(['patient'])!.value,
    };
  }
}

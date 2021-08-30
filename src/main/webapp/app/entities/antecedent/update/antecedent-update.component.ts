import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { IAntecedent, Antecedent } from '../antecedent.model';
import { AntecedentService } from '../service/antecedent.service';
import { AlertError } from 'app/shared/alert/alert-error.model';
import { EventManager, EventWithContent } from 'app/core/util/event-manager.service';
import { DataUtils, FileLoadError } from 'app/core/util/data-util.service';
import { IPatient } from 'app/entities/patient/patient.model';
import { PatientService } from 'app/entities/patient/service/patient.service';

@Component({
  selector: 'jhi-antecedent-update',
  templateUrl: './antecedent-update.component.html',
})
export class AntecedentUpdateComponent implements OnInit {
  isSaving = false;

  patientsSharedCollection: IPatient[] = [];

  editForm = this.fb.group({
    id: [],
    type: [null, [Validators.required]],
    periode: [null, [Validators.required]],
    antecedent: [null, [Validators.required]],
    traitement: [],
    patient: [null, Validators.required],
  });

  constructor(
    protected dataUtils: DataUtils,
    protected eventManager: EventManager,
    protected antecedentService: AntecedentService,
    protected patientService: PatientService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ antecedent }) => {
      this.updateForm(antecedent);

      this.loadRelationshipsOptions();
    });
  }

  byteSize(base64String: string): string {
    return this.dataUtils.byteSize(base64String);
  }

  openFile(base64String: string, contentType: string | null | undefined): void {
    this.dataUtils.openFile(base64String, contentType);
  }

  setFileData(event: Event, field: string, isImage: boolean): void {
    this.dataUtils.loadFileToForm(event, this.editForm, field, isImage).subscribe({
      error: (err: FileLoadError) =>
        this.eventManager.broadcast(new EventWithContent<AlertError>('jhipsterCabinetMedicale3App.error', { message: err.message })),
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const antecedent = this.createFromForm();
    if (antecedent.id !== undefined) {
      this.subscribeToSaveResponse(this.antecedentService.update(antecedent));
    } else {
      this.subscribeToSaveResponse(this.antecedentService.create(antecedent));
    }
  }

  trackPatientById(index: number, item: IPatient): number {
    return item.id!;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IAntecedent>>): void {
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

  protected updateForm(antecedent: IAntecedent): void {
    this.editForm.patchValue({
      id: antecedent.id,
      type: antecedent.type,
      periode: antecedent.periode,
      antecedent: antecedent.antecedent,
      traitement: antecedent.traitement,
      patient: antecedent.patient,
    });

    this.patientsSharedCollection = this.patientService.addPatientToCollectionIfMissing(this.patientsSharedCollection, antecedent.patient);
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

  protected createFromForm(): IAntecedent {
    return {
      ...new Antecedent(),
      id: this.editForm.get(['id'])!.value,
      type: this.editForm.get(['type'])!.value,
      periode: this.editForm.get(['periode'])!.value,
      antecedent: this.editForm.get(['antecedent'])!.value,
      traitement: this.editForm.get(['traitement'])!.value,
      patient: this.editForm.get(['patient'])!.value,
    };
  }
}

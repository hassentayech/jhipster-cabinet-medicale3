import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { ICasTraiter, CasTraiter } from '../cas-traiter.model';
import { CasTraiterService } from '../service/cas-traiter.service';
import { AlertError } from 'app/shared/alert/alert-error.model';
import { EventManager, EventWithContent } from 'app/core/util/event-manager.service';
import { DataUtils, FileLoadError } from 'app/core/util/data-util.service';
import { IPatient } from 'app/entities/patient/patient.model';
import { PatientService } from 'app/entities/patient/service/patient.service';

@Component({
  selector: 'jhi-cas-traiter-update',
  templateUrl: './cas-traiter-update.component.html',
})
export class CasTraiterUpdateComponent implements OnInit {
  isSaving = false;

  patientsSharedCollection: IPatient[] = [];

  editForm = this.fb.group({
    id: [],
    cas: [null, [Validators.required]],
    depuis: [null, [Validators.required]],
    histoire: [],
    remarques: [],
    etatActuel: [null, [Validators.required]],
    modeFacturation: [null, [Validators.required]],
    prixForfaitaire: [],
    patient: [null, Validators.required],
  });

  constructor(
    protected dataUtils: DataUtils,
    protected eventManager: EventManager,
    protected casTraiterService: CasTraiterService,
    protected patientService: PatientService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ casTraiter }) => {
      this.updateForm(casTraiter);

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
    const casTraiter = this.createFromForm();
    if (casTraiter.id !== undefined) {
      this.subscribeToSaveResponse(this.casTraiterService.update(casTraiter));
    } else {
      this.subscribeToSaveResponse(this.casTraiterService.create(casTraiter));
    }
  }

  trackPatientById(index: number, item: IPatient): number {
    return item.id!;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ICasTraiter>>): void {
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

  protected updateForm(casTraiter: ICasTraiter): void {
    this.editForm.patchValue({
      id: casTraiter.id,
      cas: casTraiter.cas,
      depuis: casTraiter.depuis,
      histoire: casTraiter.histoire,
      remarques: casTraiter.remarques,
      etatActuel: casTraiter.etatActuel,
      modeFacturation: casTraiter.modeFacturation,
      prixForfaitaire: casTraiter.prixForfaitaire,
      patient: casTraiter.patient,
    });

    this.patientsSharedCollection = this.patientService.addPatientToCollectionIfMissing(this.patientsSharedCollection, casTraiter.patient);
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

  protected createFromForm(): ICasTraiter {
    return {
      ...new CasTraiter(),
      id: this.editForm.get(['id'])!.value,
      cas: this.editForm.get(['cas'])!.value,
      depuis: this.editForm.get(['depuis'])!.value,
      histoire: this.editForm.get(['histoire'])!.value,
      remarques: this.editForm.get(['remarques'])!.value,
      etatActuel: this.editForm.get(['etatActuel'])!.value,
      modeFacturation: this.editForm.get(['modeFacturation'])!.value,
      prixForfaitaire: this.editForm.get(['prixForfaitaire'])!.value,
      patient: this.editForm.get(['patient'])!.value,
    };
  }
}

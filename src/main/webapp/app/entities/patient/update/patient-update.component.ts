import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import { IPatient, Patient } from '../patient.model';
import { PatientService } from '../service/patient.service';
import { AlertError } from 'app/shared/alert/alert-error.model';
import { EventManager, EventWithContent } from 'app/core/util/event-manager.service';
import { DataUtils, FileLoadError } from 'app/core/util/data-util.service';

@Component({
  selector: 'jhi-patient-update',
  templateUrl: './patient-update.component.html',
})
export class PatientUpdateComponent implements OnInit {
  isSaving = false;

  editForm = this.fb.group({
    id: [],
    reference: [null, [Validators.required]],
    nom: [null, [Validators.required, Validators.minLength(3)]],
    prenom: [null, [Validators.required, Validators.minLength(3)]],
    naissance: [null, [Validators.required]],
    sexe: [null, [Validators.required]],
    etatCivil: [null, [Validators.required]],
    fonction: [],
    email: [],
    tel: [null, [Validators.required]],
    telFixe: [null, [Validators.required]],
    adresse: [],
    remarque: [],
  });

  constructor(
    protected dataUtils: DataUtils,
    protected eventManager: EventManager,
    protected patientService: PatientService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ patient }) => {
      this.updateForm(patient);
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
    const patient = this.createFromForm();
    if (patient.id !== undefined) {
      this.subscribeToSaveResponse(this.patientService.update(patient));
    } else {
      this.subscribeToSaveResponse(this.patientService.create(patient));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IPatient>>): void {
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

  protected updateForm(patient: IPatient): void {
    this.editForm.patchValue({
      id: patient.id,
      reference: patient.reference,
      nom: patient.nom,
      prenom: patient.prenom,
      naissance: patient.naissance,
      sexe: patient.sexe,
      etatCivil: patient.etatCivil,
      fonction: patient.fonction,
      email: patient.email,
      tel: patient.tel,
      telFixe: patient.telFixe,
      adresse: patient.adresse,
      remarque: patient.remarque,
    });
  }

  protected createFromForm(): IPatient {
    return {
      ...new Patient(),
      id: this.editForm.get(['id'])!.value,
      reference: this.editForm.get(['reference'])!.value,
      nom: this.editForm.get(['nom'])!.value,
      prenom: this.editForm.get(['prenom'])!.value,
      naissance: this.editForm.get(['naissance'])!.value,
      sexe: this.editForm.get(['sexe'])!.value,
      etatCivil: this.editForm.get(['etatCivil'])!.value,
      fonction: this.editForm.get(['fonction'])!.value,
      email: this.editForm.get(['email'])!.value,
      tel: this.editForm.get(['tel'])!.value,
      telFixe: this.editForm.get(['telFixe'])!.value,
      adresse: this.editForm.get(['adresse'])!.value,
      remarque: this.editForm.get(['remarque'])!.value,
    };
  }
}

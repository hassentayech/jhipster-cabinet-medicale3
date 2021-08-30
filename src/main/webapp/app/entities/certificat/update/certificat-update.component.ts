import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import { ICertificat, Certificat } from '../certificat.model';
import { CertificatService } from '../service/certificat.service';

@Component({
  selector: 'jhi-certificat-update',
  templateUrl: './certificat-update.component.html',
})
export class CertificatUpdateComponent implements OnInit {
  isSaving = false;

  editForm = this.fb.group({
    id: [],
    nbrJours: [null, [Validators.required]],
    description: [],
  });

  constructor(protected certificatService: CertificatService, protected activatedRoute: ActivatedRoute, protected fb: FormBuilder) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ certificat }) => {
      this.updateForm(certificat);
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const certificat = this.createFromForm();
    if (certificat.id !== undefined) {
      this.subscribeToSaveResponse(this.certificatService.update(certificat));
    } else {
      this.subscribeToSaveResponse(this.certificatService.create(certificat));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ICertificat>>): void {
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

  protected updateForm(certificat: ICertificat): void {
    this.editForm.patchValue({
      id: certificat.id,
      nbrJours: certificat.nbrJours,
      description: certificat.description,
    });
  }

  protected createFromForm(): ICertificat {
    return {
      ...new Certificat(),
      id: this.editForm.get(['id'])!.value,
      nbrJours: this.editForm.get(['nbrJours'])!.value,
      description: this.editForm.get(['description'])!.value,
    };
  }
}

import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import { ICrEchographie, CrEchographie } from '../cr-echographie.model';
import { CrEchographieService } from '../service/cr-echographie.service';

@Component({
  selector: 'jhi-cr-echographie-update',
  templateUrl: './cr-echographie-update.component.html',
})
export class CrEchographieUpdateComponent implements OnInit {
  isSaving = false;

  editForm = this.fb.group({
    id: [],
    aspectFoie: [],
    observationFoie: [],
    aspectVesicule: [],
    observationVesicule: [],
    aspectTrocVoieVeine: [],
    observationTrocVoieVeine: [],
    aspectReins: [],
    observationReins: [],
    aspectRate: [],
    observationRate: [],
    aspectPancreas: [],
    observationPancreas: [],
    autreObservation: [],
  });

  constructor(protected crEchographieService: CrEchographieService, protected activatedRoute: ActivatedRoute, protected fb: FormBuilder) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ crEchographie }) => {
      this.updateForm(crEchographie);
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const crEchographie = this.createFromForm();
    if (crEchographie.id !== undefined) {
      this.subscribeToSaveResponse(this.crEchographieService.update(crEchographie));
    } else {
      this.subscribeToSaveResponse(this.crEchographieService.create(crEchographie));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ICrEchographie>>): void {
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

  protected updateForm(crEchographie: ICrEchographie): void {
    this.editForm.patchValue({
      id: crEchographie.id,
      aspectFoie: crEchographie.aspectFoie,
      observationFoie: crEchographie.observationFoie,
      aspectVesicule: crEchographie.aspectVesicule,
      observationVesicule: crEchographie.observationVesicule,
      aspectTrocVoieVeine: crEchographie.aspectTrocVoieVeine,
      observationTrocVoieVeine: crEchographie.observationTrocVoieVeine,
      aspectReins: crEchographie.aspectReins,
      observationReins: crEchographie.observationReins,
      aspectRate: crEchographie.aspectRate,
      observationRate: crEchographie.observationRate,
      aspectPancreas: crEchographie.aspectPancreas,
      observationPancreas: crEchographie.observationPancreas,
      autreObservation: crEchographie.autreObservation,
    });
  }

  protected createFromForm(): ICrEchographie {
    return {
      ...new CrEchographie(),
      id: this.editForm.get(['id'])!.value,
      aspectFoie: this.editForm.get(['aspectFoie'])!.value,
      observationFoie: this.editForm.get(['observationFoie'])!.value,
      aspectVesicule: this.editForm.get(['aspectVesicule'])!.value,
      observationVesicule: this.editForm.get(['observationVesicule'])!.value,
      aspectTrocVoieVeine: this.editForm.get(['aspectTrocVoieVeine'])!.value,
      observationTrocVoieVeine: this.editForm.get(['observationTrocVoieVeine'])!.value,
      aspectReins: this.editForm.get(['aspectReins'])!.value,
      observationReins: this.editForm.get(['observationReins'])!.value,
      aspectRate: this.editForm.get(['aspectRate'])!.value,
      observationRate: this.editForm.get(['observationRate'])!.value,
      aspectPancreas: this.editForm.get(['aspectPancreas'])!.value,
      observationPancreas: this.editForm.get(['observationPancreas'])!.value,
      autreObservation: this.editForm.get(['autreObservation'])!.value,
    };
  }
}

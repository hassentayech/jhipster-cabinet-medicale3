import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { IVisite, Visite } from '../visite.model';
import { VisiteService } from '../service/visite.service';
import { AlertError } from 'app/shared/alert/alert-error.model';
import { EventManager, EventWithContent } from 'app/core/util/event-manager.service';
import { DataUtils, FileLoadError } from 'app/core/util/data-util.service';
import { ICertificat } from 'app/entities/certificat/certificat.model';
import { CertificatService } from 'app/entities/certificat/service/certificat.service';
import { ICrEchographie } from 'app/entities/cr-echographie/cr-echographie.model';
import { CrEchographieService } from 'app/entities/cr-echographie/service/cr-echographie.service';
import { ICasTraiter } from 'app/entities/cas-traiter/cas-traiter.model';
import { CasTraiterService } from 'app/entities/cas-traiter/service/cas-traiter.service';

@Component({
  selector: 'jhi-visite-update',
  templateUrl: './visite-update.component.html',
})
export class VisiteUpdateComponent implements OnInit {
  isSaving = false;

  certificatsCollection: ICertificat[] = [];
  crEchographiesCollection: ICrEchographie[] = [];
  casTraitersSharedCollection: ICasTraiter[] = [];

  editForm = this.fb.group({
    id: [],
    control: [],
    date: [],
    motif: [],
    interrogatoire: [],
    examen: [],
    conclusion: [],
    honoraire: [null, [Validators.required]],
    certificat: [],
    crEchographie: [],
    casTraiter: [null, Validators.required],
  });

  constructor(
    protected dataUtils: DataUtils,
    protected eventManager: EventManager,
    protected visiteService: VisiteService,
    protected certificatService: CertificatService,
    protected crEchographieService: CrEchographieService,
    protected casTraiterService: CasTraiterService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ visite }) => {
      this.updateForm(visite);

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
    const visite = this.createFromForm();
    if (visite.id !== undefined) {
      this.subscribeToSaveResponse(this.visiteService.update(visite));
    } else {
      this.subscribeToSaveResponse(this.visiteService.create(visite));
    }
  }

  trackCertificatById(index: number, item: ICertificat): number {
    return item.id!;
  }

  trackCrEchographieById(index: number, item: ICrEchographie): number {
    return item.id!;
  }

  trackCasTraiterById(index: number, item: ICasTraiter): number {
    return item.id!;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IVisite>>): void {
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

  protected updateForm(visite: IVisite): void {
    this.editForm.patchValue({
      id: visite.id,
      control: visite.control,
      date: visite.date,
      motif: visite.motif,
      interrogatoire: visite.interrogatoire,
      examen: visite.examen,
      conclusion: visite.conclusion,
      honoraire: visite.honoraire,
      certificat: visite.certificat,
      crEchographie: visite.crEchographie,
      casTraiter: visite.casTraiter,
    });

    this.certificatsCollection = this.certificatService.addCertificatToCollectionIfMissing(this.certificatsCollection, visite.certificat);
    this.crEchographiesCollection = this.crEchographieService.addCrEchographieToCollectionIfMissing(
      this.crEchographiesCollection,
      visite.crEchographie
    );
    this.casTraitersSharedCollection = this.casTraiterService.addCasTraiterToCollectionIfMissing(
      this.casTraitersSharedCollection,
      visite.casTraiter
    );
  }

  protected loadRelationshipsOptions(): void {
    this.certificatService
      .query({ filter: 'visite-is-null' })
      .pipe(map((res: HttpResponse<ICertificat[]>) => res.body ?? []))
      .pipe(
        map((certificats: ICertificat[]) =>
          this.certificatService.addCertificatToCollectionIfMissing(certificats, this.editForm.get('certificat')!.value)
        )
      )
      .subscribe((certificats: ICertificat[]) => (this.certificatsCollection = certificats));

    this.crEchographieService
      .query({ filter: 'visite-is-null' })
      .pipe(map((res: HttpResponse<ICrEchographie[]>) => res.body ?? []))
      .pipe(
        map((crEchographies: ICrEchographie[]) =>
          this.crEchographieService.addCrEchographieToCollectionIfMissing(crEchographies, this.editForm.get('crEchographie')!.value)
        )
      )
      .subscribe((crEchographies: ICrEchographie[]) => (this.crEchographiesCollection = crEchographies));

    this.casTraiterService
      .query()
      .pipe(map((res: HttpResponse<ICasTraiter[]>) => res.body ?? []))
      .pipe(
        map((casTraiters: ICasTraiter[]) =>
          this.casTraiterService.addCasTraiterToCollectionIfMissing(casTraiters, this.editForm.get('casTraiter')!.value)
        )
      )
      .subscribe((casTraiters: ICasTraiter[]) => (this.casTraitersSharedCollection = casTraiters));
  }

  protected createFromForm(): IVisite {
    return {
      ...new Visite(),
      id: this.editForm.get(['id'])!.value,
      control: this.editForm.get(['control'])!.value,
      date: this.editForm.get(['date'])!.value,
      motif: this.editForm.get(['motif'])!.value,
      interrogatoire: this.editForm.get(['interrogatoire'])!.value,
      examen: this.editForm.get(['examen'])!.value,
      conclusion: this.editForm.get(['conclusion'])!.value,
      honoraire: this.editForm.get(['honoraire'])!.value,
      certificat: this.editForm.get(['certificat'])!.value,
      crEchographie: this.editForm.get(['crEchographie'])!.value,
      casTraiter: this.editForm.get(['casTraiter'])!.value,
    };
  }
}

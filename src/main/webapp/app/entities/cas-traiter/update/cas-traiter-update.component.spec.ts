jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { CasTraiterService } from '../service/cas-traiter.service';
import { ICasTraiter, CasTraiter } from '../cas-traiter.model';
import { IPatient } from 'app/entities/patient/patient.model';
import { PatientService } from 'app/entities/patient/service/patient.service';

import { CasTraiterUpdateComponent } from './cas-traiter-update.component';

describe('Component Tests', () => {
  describe('CasTraiter Management Update Component', () => {
    let comp: CasTraiterUpdateComponent;
    let fixture: ComponentFixture<CasTraiterUpdateComponent>;
    let activatedRoute: ActivatedRoute;
    let casTraiterService: CasTraiterService;
    let patientService: PatientService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [CasTraiterUpdateComponent],
        providers: [FormBuilder, ActivatedRoute],
      })
        .overrideTemplate(CasTraiterUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(CasTraiterUpdateComponent);
      activatedRoute = TestBed.inject(ActivatedRoute);
      casTraiterService = TestBed.inject(CasTraiterService);
      patientService = TestBed.inject(PatientService);

      comp = fixture.componentInstance;
    });

    describe('ngOnInit', () => {
      it('Should call Patient query and add missing value', () => {
        const casTraiter: ICasTraiter = { id: 456 };
        const patient: IPatient = { id: 21048 };
        casTraiter.patient = patient;

        const patientCollection: IPatient[] = [{ id: 51901 }];
        jest.spyOn(patientService, 'query').mockReturnValue(of(new HttpResponse({ body: patientCollection })));
        const additionalPatients = [patient];
        const expectedCollection: IPatient[] = [...additionalPatients, ...patientCollection];
        jest.spyOn(patientService, 'addPatientToCollectionIfMissing').mockReturnValue(expectedCollection);

        activatedRoute.data = of({ casTraiter });
        comp.ngOnInit();

        expect(patientService.query).toHaveBeenCalled();
        expect(patientService.addPatientToCollectionIfMissing).toHaveBeenCalledWith(patientCollection, ...additionalPatients);
        expect(comp.patientsSharedCollection).toEqual(expectedCollection);
      });

      it('Should update editForm', () => {
        const casTraiter: ICasTraiter = { id: 456 };
        const patient: IPatient = { id: 14488 };
        casTraiter.patient = patient;

        activatedRoute.data = of({ casTraiter });
        comp.ngOnInit();

        expect(comp.editForm.value).toEqual(expect.objectContaining(casTraiter));
        expect(comp.patientsSharedCollection).toContain(patient);
      });
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<CasTraiter>>();
        const casTraiter = { id: 123 };
        jest.spyOn(casTraiterService, 'update').mockReturnValue(saveSubject);
        jest.spyOn(comp, 'previousState');
        activatedRoute.data = of({ casTraiter });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: casTraiter }));
        saveSubject.complete();

        // THEN
        expect(comp.previousState).toHaveBeenCalled();
        expect(casTraiterService.update).toHaveBeenCalledWith(casTraiter);
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<CasTraiter>>();
        const casTraiter = new CasTraiter();
        jest.spyOn(casTraiterService, 'create').mockReturnValue(saveSubject);
        jest.spyOn(comp, 'previousState');
        activatedRoute.data = of({ casTraiter });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: casTraiter }));
        saveSubject.complete();

        // THEN
        expect(casTraiterService.create).toHaveBeenCalledWith(casTraiter);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).toHaveBeenCalled();
      });

      it('Should set isSaving to false on error', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<CasTraiter>>();
        const casTraiter = { id: 123 };
        jest.spyOn(casTraiterService, 'update').mockReturnValue(saveSubject);
        jest.spyOn(comp, 'previousState');
        activatedRoute.data = of({ casTraiter });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.error('This is an error!');

        // THEN
        expect(casTraiterService.update).toHaveBeenCalledWith(casTraiter);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).not.toHaveBeenCalled();
      });
    });

    describe('Tracking relationships identifiers', () => {
      describe('trackPatientById', () => {
        it('Should return tracked Patient primary key', () => {
          const entity = { id: 123 };
          const trackResult = comp.trackPatientById(0, entity);
          expect(trackResult).toEqual(entity.id);
        });
      });
    });
  });
});

jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { RendezVousService } from '../service/rendez-vous.service';
import { IRendezVous, RendezVous } from '../rendez-vous.model';
import { IPatient } from 'app/entities/patient/patient.model';
import { PatientService } from 'app/entities/patient/service/patient.service';

import { RendezVousUpdateComponent } from './rendez-vous-update.component';

describe('Component Tests', () => {
  describe('RendezVous Management Update Component', () => {
    let comp: RendezVousUpdateComponent;
    let fixture: ComponentFixture<RendezVousUpdateComponent>;
    let activatedRoute: ActivatedRoute;
    let rendezVousService: RendezVousService;
    let patientService: PatientService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [RendezVousUpdateComponent],
        providers: [FormBuilder, ActivatedRoute],
      })
        .overrideTemplate(RendezVousUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(RendezVousUpdateComponent);
      activatedRoute = TestBed.inject(ActivatedRoute);
      rendezVousService = TestBed.inject(RendezVousService);
      patientService = TestBed.inject(PatientService);

      comp = fixture.componentInstance;
    });

    describe('ngOnInit', () => {
      it('Should call Patient query and add missing value', () => {
        const rendezVous: IRendezVous = { id: 456 };
        const patient: IPatient = { id: 28756 };
        rendezVous.patient = patient;

        const patientCollection: IPatient[] = [{ id: 50982 }];
        jest.spyOn(patientService, 'query').mockReturnValue(of(new HttpResponse({ body: patientCollection })));
        const additionalPatients = [patient];
        const expectedCollection: IPatient[] = [...additionalPatients, ...patientCollection];
        jest.spyOn(patientService, 'addPatientToCollectionIfMissing').mockReturnValue(expectedCollection);

        activatedRoute.data = of({ rendezVous });
        comp.ngOnInit();

        expect(patientService.query).toHaveBeenCalled();
        expect(patientService.addPatientToCollectionIfMissing).toHaveBeenCalledWith(patientCollection, ...additionalPatients);
        expect(comp.patientsSharedCollection).toEqual(expectedCollection);
      });

      it('Should update editForm', () => {
        const rendezVous: IRendezVous = { id: 456 };
        const patient: IPatient = { id: 15939 };
        rendezVous.patient = patient;

        activatedRoute.data = of({ rendezVous });
        comp.ngOnInit();

        expect(comp.editForm.value).toEqual(expect.objectContaining(rendezVous));
        expect(comp.patientsSharedCollection).toContain(patient);
      });
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<RendezVous>>();
        const rendezVous = { id: 123 };
        jest.spyOn(rendezVousService, 'update').mockReturnValue(saveSubject);
        jest.spyOn(comp, 'previousState');
        activatedRoute.data = of({ rendezVous });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: rendezVous }));
        saveSubject.complete();

        // THEN
        expect(comp.previousState).toHaveBeenCalled();
        expect(rendezVousService.update).toHaveBeenCalledWith(rendezVous);
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<RendezVous>>();
        const rendezVous = new RendezVous();
        jest.spyOn(rendezVousService, 'create').mockReturnValue(saveSubject);
        jest.spyOn(comp, 'previousState');
        activatedRoute.data = of({ rendezVous });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: rendezVous }));
        saveSubject.complete();

        // THEN
        expect(rendezVousService.create).toHaveBeenCalledWith(rendezVous);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).toHaveBeenCalled();
      });

      it('Should set isSaving to false on error', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<RendezVous>>();
        const rendezVous = { id: 123 };
        jest.spyOn(rendezVousService, 'update').mockReturnValue(saveSubject);
        jest.spyOn(comp, 'previousState');
        activatedRoute.data = of({ rendezVous });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.error('This is an error!');

        // THEN
        expect(rendezVousService.update).toHaveBeenCalledWith(rendezVous);
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

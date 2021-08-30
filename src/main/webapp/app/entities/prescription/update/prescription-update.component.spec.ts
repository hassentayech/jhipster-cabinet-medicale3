jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { PrescriptionService } from '../service/prescription.service';
import { IPrescription, Prescription } from '../prescription.model';
import { IOrdonnance } from 'app/entities/ordonnance/ordonnance.model';
import { OrdonnanceService } from 'app/entities/ordonnance/service/ordonnance.service';

import { PrescriptionUpdateComponent } from './prescription-update.component';

describe('Component Tests', () => {
  describe('Prescription Management Update Component', () => {
    let comp: PrescriptionUpdateComponent;
    let fixture: ComponentFixture<PrescriptionUpdateComponent>;
    let activatedRoute: ActivatedRoute;
    let prescriptionService: PrescriptionService;
    let ordonnanceService: OrdonnanceService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [PrescriptionUpdateComponent],
        providers: [FormBuilder, ActivatedRoute],
      })
        .overrideTemplate(PrescriptionUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(PrescriptionUpdateComponent);
      activatedRoute = TestBed.inject(ActivatedRoute);
      prescriptionService = TestBed.inject(PrescriptionService);
      ordonnanceService = TestBed.inject(OrdonnanceService);

      comp = fixture.componentInstance;
    });

    describe('ngOnInit', () => {
      it('Should call Ordonnance query and add missing value', () => {
        const prescription: IPrescription = { id: 456 };
        const ordonnance: IOrdonnance = { id: 29866 };
        prescription.ordonnance = ordonnance;

        const ordonnanceCollection: IOrdonnance[] = [{ id: 16432 }];
        jest.spyOn(ordonnanceService, 'query').mockReturnValue(of(new HttpResponse({ body: ordonnanceCollection })));
        const additionalOrdonnances = [ordonnance];
        const expectedCollection: IOrdonnance[] = [...additionalOrdonnances, ...ordonnanceCollection];
        jest.spyOn(ordonnanceService, 'addOrdonnanceToCollectionIfMissing').mockReturnValue(expectedCollection);

        activatedRoute.data = of({ prescription });
        comp.ngOnInit();

        expect(ordonnanceService.query).toHaveBeenCalled();
        expect(ordonnanceService.addOrdonnanceToCollectionIfMissing).toHaveBeenCalledWith(ordonnanceCollection, ...additionalOrdonnances);
        expect(comp.ordonnancesSharedCollection).toEqual(expectedCollection);
      });

      it('Should update editForm', () => {
        const prescription: IPrescription = { id: 456 };
        const ordonnance: IOrdonnance = { id: 52322 };
        prescription.ordonnance = ordonnance;

        activatedRoute.data = of({ prescription });
        comp.ngOnInit();

        expect(comp.editForm.value).toEqual(expect.objectContaining(prescription));
        expect(comp.ordonnancesSharedCollection).toContain(ordonnance);
      });
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<Prescription>>();
        const prescription = { id: 123 };
        jest.spyOn(prescriptionService, 'update').mockReturnValue(saveSubject);
        jest.spyOn(comp, 'previousState');
        activatedRoute.data = of({ prescription });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: prescription }));
        saveSubject.complete();

        // THEN
        expect(comp.previousState).toHaveBeenCalled();
        expect(prescriptionService.update).toHaveBeenCalledWith(prescription);
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<Prescription>>();
        const prescription = new Prescription();
        jest.spyOn(prescriptionService, 'create').mockReturnValue(saveSubject);
        jest.spyOn(comp, 'previousState');
        activatedRoute.data = of({ prescription });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: prescription }));
        saveSubject.complete();

        // THEN
        expect(prescriptionService.create).toHaveBeenCalledWith(prescription);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).toHaveBeenCalled();
      });

      it('Should set isSaving to false on error', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<Prescription>>();
        const prescription = { id: 123 };
        jest.spyOn(prescriptionService, 'update').mockReturnValue(saveSubject);
        jest.spyOn(comp, 'previousState');
        activatedRoute.data = of({ prescription });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.error('This is an error!');

        // THEN
        expect(prescriptionService.update).toHaveBeenCalledWith(prescription);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).not.toHaveBeenCalled();
      });
    });

    describe('Tracking relationships identifiers', () => {
      describe('trackOrdonnanceById', () => {
        it('Should return tracked Ordonnance primary key', () => {
          const entity = { id: 123 };
          const trackResult = comp.trackOrdonnanceById(0, entity);
          expect(trackResult).toEqual(entity.id);
        });
      });
    });
  });
});

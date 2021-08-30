jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { OrdonnanceService } from '../service/ordonnance.service';
import { IOrdonnance, Ordonnance } from '../ordonnance.model';
import { IVisite } from 'app/entities/visite/visite.model';
import { VisiteService } from 'app/entities/visite/service/visite.service';

import { OrdonnanceUpdateComponent } from './ordonnance-update.component';

describe('Component Tests', () => {
  describe('Ordonnance Management Update Component', () => {
    let comp: OrdonnanceUpdateComponent;
    let fixture: ComponentFixture<OrdonnanceUpdateComponent>;
    let activatedRoute: ActivatedRoute;
    let ordonnanceService: OrdonnanceService;
    let visiteService: VisiteService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [OrdonnanceUpdateComponent],
        providers: [FormBuilder, ActivatedRoute],
      })
        .overrideTemplate(OrdonnanceUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(OrdonnanceUpdateComponent);
      activatedRoute = TestBed.inject(ActivatedRoute);
      ordonnanceService = TestBed.inject(OrdonnanceService);
      visiteService = TestBed.inject(VisiteService);

      comp = fixture.componentInstance;
    });

    describe('ngOnInit', () => {
      it('Should call Visite query and add missing value', () => {
        const ordonnance: IOrdonnance = { id: 456 };
        const visite: IVisite = { id: 31932 };
        ordonnance.visite = visite;

        const visiteCollection: IVisite[] = [{ id: 7057 }];
        jest.spyOn(visiteService, 'query').mockReturnValue(of(new HttpResponse({ body: visiteCollection })));
        const additionalVisites = [visite];
        const expectedCollection: IVisite[] = [...additionalVisites, ...visiteCollection];
        jest.spyOn(visiteService, 'addVisiteToCollectionIfMissing').mockReturnValue(expectedCollection);

        activatedRoute.data = of({ ordonnance });
        comp.ngOnInit();

        expect(visiteService.query).toHaveBeenCalled();
        expect(visiteService.addVisiteToCollectionIfMissing).toHaveBeenCalledWith(visiteCollection, ...additionalVisites);
        expect(comp.visitesSharedCollection).toEqual(expectedCollection);
      });

      it('Should update editForm', () => {
        const ordonnance: IOrdonnance = { id: 456 };
        const visite: IVisite = { id: 10585 };
        ordonnance.visite = visite;

        activatedRoute.data = of({ ordonnance });
        comp.ngOnInit();

        expect(comp.editForm.value).toEqual(expect.objectContaining(ordonnance));
        expect(comp.visitesSharedCollection).toContain(visite);
      });
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<Ordonnance>>();
        const ordonnance = { id: 123 };
        jest.spyOn(ordonnanceService, 'update').mockReturnValue(saveSubject);
        jest.spyOn(comp, 'previousState');
        activatedRoute.data = of({ ordonnance });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: ordonnance }));
        saveSubject.complete();

        // THEN
        expect(comp.previousState).toHaveBeenCalled();
        expect(ordonnanceService.update).toHaveBeenCalledWith(ordonnance);
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<Ordonnance>>();
        const ordonnance = new Ordonnance();
        jest.spyOn(ordonnanceService, 'create').mockReturnValue(saveSubject);
        jest.spyOn(comp, 'previousState');
        activatedRoute.data = of({ ordonnance });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: ordonnance }));
        saveSubject.complete();

        // THEN
        expect(ordonnanceService.create).toHaveBeenCalledWith(ordonnance);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).toHaveBeenCalled();
      });

      it('Should set isSaving to false on error', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<Ordonnance>>();
        const ordonnance = { id: 123 };
        jest.spyOn(ordonnanceService, 'update').mockReturnValue(saveSubject);
        jest.spyOn(comp, 'previousState');
        activatedRoute.data = of({ ordonnance });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.error('This is an error!');

        // THEN
        expect(ordonnanceService.update).toHaveBeenCalledWith(ordonnance);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).not.toHaveBeenCalled();
      });
    });

    describe('Tracking relationships identifiers', () => {
      describe('trackVisiteById', () => {
        it('Should return tracked Visite primary key', () => {
          const entity = { id: 123 };
          const trackResult = comp.trackVisiteById(0, entity);
          expect(trackResult).toEqual(entity.id);
        });
      });
    });
  });
});

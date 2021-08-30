jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { ReglementService } from '../service/reglement.service';
import { IReglement, Reglement } from '../reglement.model';
import { IVisite } from 'app/entities/visite/visite.model';
import { VisiteService } from 'app/entities/visite/service/visite.service';

import { ReglementUpdateComponent } from './reglement-update.component';

describe('Component Tests', () => {
  describe('Reglement Management Update Component', () => {
    let comp: ReglementUpdateComponent;
    let fixture: ComponentFixture<ReglementUpdateComponent>;
    let activatedRoute: ActivatedRoute;
    let reglementService: ReglementService;
    let visiteService: VisiteService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [ReglementUpdateComponent],
        providers: [FormBuilder, ActivatedRoute],
      })
        .overrideTemplate(ReglementUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(ReglementUpdateComponent);
      activatedRoute = TestBed.inject(ActivatedRoute);
      reglementService = TestBed.inject(ReglementService);
      visiteService = TestBed.inject(VisiteService);

      comp = fixture.componentInstance;
    });

    describe('ngOnInit', () => {
      it('Should call Visite query and add missing value', () => {
        const reglement: IReglement = { id: 456 };
        const visite: IVisite = { id: 8399 };
        reglement.visite = visite;

        const visiteCollection: IVisite[] = [{ id: 95122 }];
        jest.spyOn(visiteService, 'query').mockReturnValue(of(new HttpResponse({ body: visiteCollection })));
        const additionalVisites = [visite];
        const expectedCollection: IVisite[] = [...additionalVisites, ...visiteCollection];
        jest.spyOn(visiteService, 'addVisiteToCollectionIfMissing').mockReturnValue(expectedCollection);

        activatedRoute.data = of({ reglement });
        comp.ngOnInit();

        expect(visiteService.query).toHaveBeenCalled();
        expect(visiteService.addVisiteToCollectionIfMissing).toHaveBeenCalledWith(visiteCollection, ...additionalVisites);
        expect(comp.visitesSharedCollection).toEqual(expectedCollection);
      });

      it('Should update editForm', () => {
        const reglement: IReglement = { id: 456 };
        const visite: IVisite = { id: 97751 };
        reglement.visite = visite;

        activatedRoute.data = of({ reglement });
        comp.ngOnInit();

        expect(comp.editForm.value).toEqual(expect.objectContaining(reglement));
        expect(comp.visitesSharedCollection).toContain(visite);
      });
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<Reglement>>();
        const reglement = { id: 123 };
        jest.spyOn(reglementService, 'update').mockReturnValue(saveSubject);
        jest.spyOn(comp, 'previousState');
        activatedRoute.data = of({ reglement });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: reglement }));
        saveSubject.complete();

        // THEN
        expect(comp.previousState).toHaveBeenCalled();
        expect(reglementService.update).toHaveBeenCalledWith(reglement);
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<Reglement>>();
        const reglement = new Reglement();
        jest.spyOn(reglementService, 'create').mockReturnValue(saveSubject);
        jest.spyOn(comp, 'previousState');
        activatedRoute.data = of({ reglement });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: reglement }));
        saveSubject.complete();

        // THEN
        expect(reglementService.create).toHaveBeenCalledWith(reglement);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).toHaveBeenCalled();
      });

      it('Should set isSaving to false on error', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<Reglement>>();
        const reglement = { id: 123 };
        jest.spyOn(reglementService, 'update').mockReturnValue(saveSubject);
        jest.spyOn(comp, 'previousState');
        activatedRoute.data = of({ reglement });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.error('This is an error!');

        // THEN
        expect(reglementService.update).toHaveBeenCalledWith(reglement);
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

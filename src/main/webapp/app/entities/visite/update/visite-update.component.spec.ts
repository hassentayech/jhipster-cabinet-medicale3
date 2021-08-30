jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { VisiteService } from '../service/visite.service';
import { IVisite, Visite } from '../visite.model';
import { ICertificat } from 'app/entities/certificat/certificat.model';
import { CertificatService } from 'app/entities/certificat/service/certificat.service';
import { ICrEchographie } from 'app/entities/cr-echographie/cr-echographie.model';
import { CrEchographieService } from 'app/entities/cr-echographie/service/cr-echographie.service';
import { ICasTraiter } from 'app/entities/cas-traiter/cas-traiter.model';
import { CasTraiterService } from 'app/entities/cas-traiter/service/cas-traiter.service';

import { VisiteUpdateComponent } from './visite-update.component';

describe('Component Tests', () => {
  describe('Visite Management Update Component', () => {
    let comp: VisiteUpdateComponent;
    let fixture: ComponentFixture<VisiteUpdateComponent>;
    let activatedRoute: ActivatedRoute;
    let visiteService: VisiteService;
    let certificatService: CertificatService;
    let crEchographieService: CrEchographieService;
    let casTraiterService: CasTraiterService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [VisiteUpdateComponent],
        providers: [FormBuilder, ActivatedRoute],
      })
        .overrideTemplate(VisiteUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(VisiteUpdateComponent);
      activatedRoute = TestBed.inject(ActivatedRoute);
      visiteService = TestBed.inject(VisiteService);
      certificatService = TestBed.inject(CertificatService);
      crEchographieService = TestBed.inject(CrEchographieService);
      casTraiterService = TestBed.inject(CasTraiterService);

      comp = fixture.componentInstance;
    });

    describe('ngOnInit', () => {
      it('Should call certificat query and add missing value', () => {
        const visite: IVisite = { id: 456 };
        const certificat: ICertificat = { id: 49358 };
        visite.certificat = certificat;

        const certificatCollection: ICertificat[] = [{ id: 48561 }];
        jest.spyOn(certificatService, 'query').mockReturnValue(of(new HttpResponse({ body: certificatCollection })));
        const expectedCollection: ICertificat[] = [certificat, ...certificatCollection];
        jest.spyOn(certificatService, 'addCertificatToCollectionIfMissing').mockReturnValue(expectedCollection);

        activatedRoute.data = of({ visite });
        comp.ngOnInit();

        expect(certificatService.query).toHaveBeenCalled();
        expect(certificatService.addCertificatToCollectionIfMissing).toHaveBeenCalledWith(certificatCollection, certificat);
        expect(comp.certificatsCollection).toEqual(expectedCollection);
      });

      it('Should call crEchographie query and add missing value', () => {
        const visite: IVisite = { id: 456 };
        const crEchographie: ICrEchographie = { id: 67481 };
        visite.crEchographie = crEchographie;

        const crEchographieCollection: ICrEchographie[] = [{ id: 3582 }];
        jest.spyOn(crEchographieService, 'query').mockReturnValue(of(new HttpResponse({ body: crEchographieCollection })));
        const expectedCollection: ICrEchographie[] = [crEchographie, ...crEchographieCollection];
        jest.spyOn(crEchographieService, 'addCrEchographieToCollectionIfMissing').mockReturnValue(expectedCollection);

        activatedRoute.data = of({ visite });
        comp.ngOnInit();

        expect(crEchographieService.query).toHaveBeenCalled();
        expect(crEchographieService.addCrEchographieToCollectionIfMissing).toHaveBeenCalledWith(crEchographieCollection, crEchographie);
        expect(comp.crEchographiesCollection).toEqual(expectedCollection);
      });

      it('Should call CasTraiter query and add missing value', () => {
        const visite: IVisite = { id: 456 };
        const casTraiter: ICasTraiter = { id: 12971 };
        visite.casTraiter = casTraiter;

        const casTraiterCollection: ICasTraiter[] = [{ id: 97891 }];
        jest.spyOn(casTraiterService, 'query').mockReturnValue(of(new HttpResponse({ body: casTraiterCollection })));
        const additionalCasTraiters = [casTraiter];
        const expectedCollection: ICasTraiter[] = [...additionalCasTraiters, ...casTraiterCollection];
        jest.spyOn(casTraiterService, 'addCasTraiterToCollectionIfMissing').mockReturnValue(expectedCollection);

        activatedRoute.data = of({ visite });
        comp.ngOnInit();

        expect(casTraiterService.query).toHaveBeenCalled();
        expect(casTraiterService.addCasTraiterToCollectionIfMissing).toHaveBeenCalledWith(casTraiterCollection, ...additionalCasTraiters);
        expect(comp.casTraitersSharedCollection).toEqual(expectedCollection);
      });

      it('Should update editForm', () => {
        const visite: IVisite = { id: 456 };
        const certificat: ICertificat = { id: 57958 };
        visite.certificat = certificat;
        const crEchographie: ICrEchographie = { id: 72575 };
        visite.crEchographie = crEchographie;
        const casTraiter: ICasTraiter = { id: 53240 };
        visite.casTraiter = casTraiter;

        activatedRoute.data = of({ visite });
        comp.ngOnInit();

        expect(comp.editForm.value).toEqual(expect.objectContaining(visite));
        expect(comp.certificatsCollection).toContain(certificat);
        expect(comp.crEchographiesCollection).toContain(crEchographie);
        expect(comp.casTraitersSharedCollection).toContain(casTraiter);
      });
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<Visite>>();
        const visite = { id: 123 };
        jest.spyOn(visiteService, 'update').mockReturnValue(saveSubject);
        jest.spyOn(comp, 'previousState');
        activatedRoute.data = of({ visite });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: visite }));
        saveSubject.complete();

        // THEN
        expect(comp.previousState).toHaveBeenCalled();
        expect(visiteService.update).toHaveBeenCalledWith(visite);
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<Visite>>();
        const visite = new Visite();
        jest.spyOn(visiteService, 'create').mockReturnValue(saveSubject);
        jest.spyOn(comp, 'previousState');
        activatedRoute.data = of({ visite });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: visite }));
        saveSubject.complete();

        // THEN
        expect(visiteService.create).toHaveBeenCalledWith(visite);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).toHaveBeenCalled();
      });

      it('Should set isSaving to false on error', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<Visite>>();
        const visite = { id: 123 };
        jest.spyOn(visiteService, 'update').mockReturnValue(saveSubject);
        jest.spyOn(comp, 'previousState');
        activatedRoute.data = of({ visite });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.error('This is an error!');

        // THEN
        expect(visiteService.update).toHaveBeenCalledWith(visite);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).not.toHaveBeenCalled();
      });
    });

    describe('Tracking relationships identifiers', () => {
      describe('trackCertificatById', () => {
        it('Should return tracked Certificat primary key', () => {
          const entity = { id: 123 };
          const trackResult = comp.trackCertificatById(0, entity);
          expect(trackResult).toEqual(entity.id);
        });
      });

      describe('trackCrEchographieById', () => {
        it('Should return tracked CrEchographie primary key', () => {
          const entity = { id: 123 };
          const trackResult = comp.trackCrEchographieById(0, entity);
          expect(trackResult).toEqual(entity.id);
        });
      });

      describe('trackCasTraiterById', () => {
        it('Should return tracked CasTraiter primary key', () => {
          const entity = { id: 123 };
          const trackResult = comp.trackCasTraiterById(0, entity);
          expect(trackResult).toEqual(entity.id);
        });
      });
    });
  });
});

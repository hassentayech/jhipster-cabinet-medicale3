jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { CertificatService } from '../service/certificat.service';
import { ICertificat, Certificat } from '../certificat.model';

import { CertificatUpdateComponent } from './certificat-update.component';

describe('Component Tests', () => {
  describe('Certificat Management Update Component', () => {
    let comp: CertificatUpdateComponent;
    let fixture: ComponentFixture<CertificatUpdateComponent>;
    let activatedRoute: ActivatedRoute;
    let certificatService: CertificatService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [CertificatUpdateComponent],
        providers: [FormBuilder, ActivatedRoute],
      })
        .overrideTemplate(CertificatUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(CertificatUpdateComponent);
      activatedRoute = TestBed.inject(ActivatedRoute);
      certificatService = TestBed.inject(CertificatService);

      comp = fixture.componentInstance;
    });

    describe('ngOnInit', () => {
      it('Should update editForm', () => {
        const certificat: ICertificat = { id: 456 };

        activatedRoute.data = of({ certificat });
        comp.ngOnInit();

        expect(comp.editForm.value).toEqual(expect.objectContaining(certificat));
      });
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<Certificat>>();
        const certificat = { id: 123 };
        jest.spyOn(certificatService, 'update').mockReturnValue(saveSubject);
        jest.spyOn(comp, 'previousState');
        activatedRoute.data = of({ certificat });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: certificat }));
        saveSubject.complete();

        // THEN
        expect(comp.previousState).toHaveBeenCalled();
        expect(certificatService.update).toHaveBeenCalledWith(certificat);
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<Certificat>>();
        const certificat = new Certificat();
        jest.spyOn(certificatService, 'create').mockReturnValue(saveSubject);
        jest.spyOn(comp, 'previousState');
        activatedRoute.data = of({ certificat });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: certificat }));
        saveSubject.complete();

        // THEN
        expect(certificatService.create).toHaveBeenCalledWith(certificat);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).toHaveBeenCalled();
      });

      it('Should set isSaving to false on error', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<Certificat>>();
        const certificat = { id: 123 };
        jest.spyOn(certificatService, 'update').mockReturnValue(saveSubject);
        jest.spyOn(comp, 'previousState');
        activatedRoute.data = of({ certificat });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.error('This is an error!');

        // THEN
        expect(certificatService.update).toHaveBeenCalledWith(certificat);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).not.toHaveBeenCalled();
      });
    });
  });
});

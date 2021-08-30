jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { CrEchographieService } from '../service/cr-echographie.service';
import { ICrEchographie, CrEchographie } from '../cr-echographie.model';

import { CrEchographieUpdateComponent } from './cr-echographie-update.component';

describe('Component Tests', () => {
  describe('CrEchographie Management Update Component', () => {
    let comp: CrEchographieUpdateComponent;
    let fixture: ComponentFixture<CrEchographieUpdateComponent>;
    let activatedRoute: ActivatedRoute;
    let crEchographieService: CrEchographieService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [CrEchographieUpdateComponent],
        providers: [FormBuilder, ActivatedRoute],
      })
        .overrideTemplate(CrEchographieUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(CrEchographieUpdateComponent);
      activatedRoute = TestBed.inject(ActivatedRoute);
      crEchographieService = TestBed.inject(CrEchographieService);

      comp = fixture.componentInstance;
    });

    describe('ngOnInit', () => {
      it('Should update editForm', () => {
        const crEchographie: ICrEchographie = { id: 456 };

        activatedRoute.data = of({ crEchographie });
        comp.ngOnInit();

        expect(comp.editForm.value).toEqual(expect.objectContaining(crEchographie));
      });
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<CrEchographie>>();
        const crEchographie = { id: 123 };
        jest.spyOn(crEchographieService, 'update').mockReturnValue(saveSubject);
        jest.spyOn(comp, 'previousState');
        activatedRoute.data = of({ crEchographie });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: crEchographie }));
        saveSubject.complete();

        // THEN
        expect(comp.previousState).toHaveBeenCalled();
        expect(crEchographieService.update).toHaveBeenCalledWith(crEchographie);
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<CrEchographie>>();
        const crEchographie = new CrEchographie();
        jest.spyOn(crEchographieService, 'create').mockReturnValue(saveSubject);
        jest.spyOn(comp, 'previousState');
        activatedRoute.data = of({ crEchographie });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: crEchographie }));
        saveSubject.complete();

        // THEN
        expect(crEchographieService.create).toHaveBeenCalledWith(crEchographie);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).toHaveBeenCalled();
      });

      it('Should set isSaving to false on error', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<CrEchographie>>();
        const crEchographie = { id: 123 };
        jest.spyOn(crEchographieService, 'update').mockReturnValue(saveSubject);
        jest.spyOn(comp, 'previousState');
        activatedRoute.data = of({ crEchographie });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.error('This is an error!');

        // THEN
        expect(crEchographieService.update).toHaveBeenCalledWith(crEchographie);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).not.toHaveBeenCalled();
      });
    });
  });
});

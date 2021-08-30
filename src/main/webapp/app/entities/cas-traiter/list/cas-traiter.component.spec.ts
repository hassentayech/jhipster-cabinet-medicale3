import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { of } from 'rxjs';

import { CasTraiterService } from '../service/cas-traiter.service';

import { CasTraiterComponent } from './cas-traiter.component';

describe('Component Tests', () => {
  describe('CasTraiter Management Component', () => {
    let comp: CasTraiterComponent;
    let fixture: ComponentFixture<CasTraiterComponent>;
    let service: CasTraiterService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [CasTraiterComponent],
      })
        .overrideTemplate(CasTraiterComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(CasTraiterComponent);
      comp = fixture.componentInstance;
      service = TestBed.inject(CasTraiterService);

      const headers = new HttpHeaders().append('link', 'link;link');
      jest.spyOn(service, 'query').mockReturnValue(
        of(
          new HttpResponse({
            body: [{ id: 123 }],
            headers,
          })
        )
      );
    });

    it('Should call load all on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(service.query).toHaveBeenCalled();
      expect(comp.casTraiters?.[0]).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});

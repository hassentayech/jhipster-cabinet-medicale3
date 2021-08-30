import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { of } from 'rxjs';

import { OrdonnanceService } from '../service/ordonnance.service';

import { OrdonnanceComponent } from './ordonnance.component';

describe('Component Tests', () => {
  describe('Ordonnance Management Component', () => {
    let comp: OrdonnanceComponent;
    let fixture: ComponentFixture<OrdonnanceComponent>;
    let service: OrdonnanceService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [OrdonnanceComponent],
      })
        .overrideTemplate(OrdonnanceComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(OrdonnanceComponent);
      comp = fixture.componentInstance;
      service = TestBed.inject(OrdonnanceService);

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
      expect(comp.ordonnances?.[0]).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});

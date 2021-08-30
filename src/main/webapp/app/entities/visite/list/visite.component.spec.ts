import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { of } from 'rxjs';

import { VisiteService } from '../service/visite.service';

import { VisiteComponent } from './visite.component';

describe('Component Tests', () => {
  describe('Visite Management Component', () => {
    let comp: VisiteComponent;
    let fixture: ComponentFixture<VisiteComponent>;
    let service: VisiteService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [VisiteComponent],
      })
        .overrideTemplate(VisiteComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(VisiteComponent);
      comp = fixture.componentInstance;
      service = TestBed.inject(VisiteService);

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
      expect(comp.visites?.[0]).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});

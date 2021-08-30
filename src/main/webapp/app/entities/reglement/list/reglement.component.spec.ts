import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { of } from 'rxjs';

import { ReglementService } from '../service/reglement.service';

import { ReglementComponent } from './reglement.component';

describe('Component Tests', () => {
  describe('Reglement Management Component', () => {
    let comp: ReglementComponent;
    let fixture: ComponentFixture<ReglementComponent>;
    let service: ReglementService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [ReglementComponent],
      })
        .overrideTemplate(ReglementComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(ReglementComponent);
      comp = fixture.componentInstance;
      service = TestBed.inject(ReglementService);

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
      expect(comp.reglements?.[0]).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});

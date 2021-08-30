import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { of } from 'rxjs';

import { PrescriptionService } from '../service/prescription.service';

import { PrescriptionComponent } from './prescription.component';

describe('Component Tests', () => {
  describe('Prescription Management Component', () => {
    let comp: PrescriptionComponent;
    let fixture: ComponentFixture<PrescriptionComponent>;
    let service: PrescriptionService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [PrescriptionComponent],
      })
        .overrideTemplate(PrescriptionComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(PrescriptionComponent);
      comp = fixture.componentInstance;
      service = TestBed.inject(PrescriptionService);

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
      expect(comp.prescriptions?.[0]).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});

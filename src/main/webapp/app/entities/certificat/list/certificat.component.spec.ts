import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { of } from 'rxjs';

import { CertificatService } from '../service/certificat.service';

import { CertificatComponent } from './certificat.component';

describe('Component Tests', () => {
  describe('Certificat Management Component', () => {
    let comp: CertificatComponent;
    let fixture: ComponentFixture<CertificatComponent>;
    let service: CertificatService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [CertificatComponent],
      })
        .overrideTemplate(CertificatComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(CertificatComponent);
      comp = fixture.componentInstance;
      service = TestBed.inject(CertificatService);

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
      expect(comp.certificats?.[0]).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});

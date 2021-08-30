import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { CertificatDetailComponent } from './certificat-detail.component';

describe('Component Tests', () => {
  describe('Certificat Management Detail Component', () => {
    let comp: CertificatDetailComponent;
    let fixture: ComponentFixture<CertificatDetailComponent>;

    beforeEach(() => {
      TestBed.configureTestingModule({
        declarations: [CertificatDetailComponent],
        providers: [
          {
            provide: ActivatedRoute,
            useValue: { data: of({ certificat: { id: 123 } }) },
          },
        ],
      })
        .overrideTemplate(CertificatDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(CertificatDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should load certificat on init', () => {
        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.certificat).toEqual(expect.objectContaining({ id: 123 }));
      });
    });
  });
});

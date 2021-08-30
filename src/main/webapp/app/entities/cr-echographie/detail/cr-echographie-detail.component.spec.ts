import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { CrEchographieDetailComponent } from './cr-echographie-detail.component';

describe('Component Tests', () => {
  describe('CrEchographie Management Detail Component', () => {
    let comp: CrEchographieDetailComponent;
    let fixture: ComponentFixture<CrEchographieDetailComponent>;

    beforeEach(() => {
      TestBed.configureTestingModule({
        declarations: [CrEchographieDetailComponent],
        providers: [
          {
            provide: ActivatedRoute,
            useValue: { data: of({ crEchographie: { id: 123 } }) },
          },
        ],
      })
        .overrideTemplate(CrEchographieDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(CrEchographieDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should load crEchographie on init', () => {
        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.crEchographie).toEqual(expect.objectContaining({ id: 123 }));
      });
    });
  });
});

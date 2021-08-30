import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { of } from 'rxjs';

import { CrEchographieService } from '../service/cr-echographie.service';

import { CrEchographieComponent } from './cr-echographie.component';

describe('Component Tests', () => {
  describe('CrEchographie Management Component', () => {
    let comp: CrEchographieComponent;
    let fixture: ComponentFixture<CrEchographieComponent>;
    let service: CrEchographieService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [CrEchographieComponent],
      })
        .overrideTemplate(CrEchographieComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(CrEchographieComponent);
      comp = fixture.componentInstance;
      service = TestBed.inject(CrEchographieService);

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
      expect(comp.crEchographies?.[0]).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});

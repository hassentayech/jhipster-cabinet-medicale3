import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { of } from 'rxjs';

import { AntecedentService } from '../service/antecedent.service';

import { AntecedentComponent } from './antecedent.component';

describe('Component Tests', () => {
  describe('Antecedent Management Component', () => {
    let comp: AntecedentComponent;
    let fixture: ComponentFixture<AntecedentComponent>;
    let service: AntecedentService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [AntecedentComponent],
      })
        .overrideTemplate(AntecedentComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(AntecedentComponent);
      comp = fixture.componentInstance;
      service = TestBed.inject(AntecedentService);

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
      expect(comp.antecedents?.[0]).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});

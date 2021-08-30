import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { of } from 'rxjs';

import { ConstanteService } from '../service/constante.service';

import { ConstanteComponent } from './constante.component';

describe('Component Tests', () => {
  describe('Constante Management Component', () => {
    let comp: ConstanteComponent;
    let fixture: ComponentFixture<ConstanteComponent>;
    let service: ConstanteService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [ConstanteComponent],
      })
        .overrideTemplate(ConstanteComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(ConstanteComponent);
      comp = fixture.componentInstance;
      service = TestBed.inject(ConstanteService);

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
      expect(comp.constantes?.[0]).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});

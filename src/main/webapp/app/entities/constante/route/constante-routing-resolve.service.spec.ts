jest.mock('@angular/router');

import { TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of } from 'rxjs';

import { IConstante, Constante } from '../constante.model';
import { ConstanteService } from '../service/constante.service';

import { ConstanteRoutingResolveService } from './constante-routing-resolve.service';

describe('Service Tests', () => {
  describe('Constante routing resolve service', () => {
    let mockRouter: Router;
    let mockActivatedRouteSnapshot: ActivatedRouteSnapshot;
    let routingResolveService: ConstanteRoutingResolveService;
    let service: ConstanteService;
    let resultConstante: IConstante | undefined;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        providers: [Router, ActivatedRouteSnapshot],
      });
      mockRouter = TestBed.inject(Router);
      mockActivatedRouteSnapshot = TestBed.inject(ActivatedRouteSnapshot);
      routingResolveService = TestBed.inject(ConstanteRoutingResolveService);
      service = TestBed.inject(ConstanteService);
      resultConstante = undefined;
    });

    describe('resolve', () => {
      it('should return IConstante returned by find', () => {
        // GIVEN
        service.find = jest.fn(id => of(new HttpResponse({ body: { id } })));
        mockActivatedRouteSnapshot.params = { id: 123 };

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultConstante = result;
        });

        // THEN
        expect(service.find).toBeCalledWith(123);
        expect(resultConstante).toEqual({ id: 123 });
      });

      it('should return new IConstante if id is not provided', () => {
        // GIVEN
        service.find = jest.fn();
        mockActivatedRouteSnapshot.params = {};

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultConstante = result;
        });

        // THEN
        expect(service.find).not.toBeCalled();
        expect(resultConstante).toEqual(new Constante());
      });

      it('should route to 404 page if data not found in server', () => {
        // GIVEN
        jest.spyOn(service, 'find').mockReturnValue(of(new HttpResponse({ body: null as unknown as Constante })));
        mockActivatedRouteSnapshot.params = { id: 123 };

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultConstante = result;
        });

        // THEN
        expect(service.find).toBeCalledWith(123);
        expect(resultConstante).toEqual(undefined);
        expect(mockRouter.navigate).toHaveBeenCalledWith(['404']);
      });
    });
  });
});

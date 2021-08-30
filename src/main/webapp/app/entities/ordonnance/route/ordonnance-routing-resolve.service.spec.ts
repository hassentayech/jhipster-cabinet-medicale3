jest.mock('@angular/router');

import { TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of } from 'rxjs';

import { IOrdonnance, Ordonnance } from '../ordonnance.model';
import { OrdonnanceService } from '../service/ordonnance.service';

import { OrdonnanceRoutingResolveService } from './ordonnance-routing-resolve.service';

describe('Service Tests', () => {
  describe('Ordonnance routing resolve service', () => {
    let mockRouter: Router;
    let mockActivatedRouteSnapshot: ActivatedRouteSnapshot;
    let routingResolveService: OrdonnanceRoutingResolveService;
    let service: OrdonnanceService;
    let resultOrdonnance: IOrdonnance | undefined;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        providers: [Router, ActivatedRouteSnapshot],
      });
      mockRouter = TestBed.inject(Router);
      mockActivatedRouteSnapshot = TestBed.inject(ActivatedRouteSnapshot);
      routingResolveService = TestBed.inject(OrdonnanceRoutingResolveService);
      service = TestBed.inject(OrdonnanceService);
      resultOrdonnance = undefined;
    });

    describe('resolve', () => {
      it('should return IOrdonnance returned by find', () => {
        // GIVEN
        service.find = jest.fn(id => of(new HttpResponse({ body: { id } })));
        mockActivatedRouteSnapshot.params = { id: 123 };

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultOrdonnance = result;
        });

        // THEN
        expect(service.find).toBeCalledWith(123);
        expect(resultOrdonnance).toEqual({ id: 123 });
      });

      it('should return new IOrdonnance if id is not provided', () => {
        // GIVEN
        service.find = jest.fn();
        mockActivatedRouteSnapshot.params = {};

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultOrdonnance = result;
        });

        // THEN
        expect(service.find).not.toBeCalled();
        expect(resultOrdonnance).toEqual(new Ordonnance());
      });

      it('should route to 404 page if data not found in server', () => {
        // GIVEN
        jest.spyOn(service, 'find').mockReturnValue(of(new HttpResponse({ body: null as unknown as Ordonnance })));
        mockActivatedRouteSnapshot.params = { id: 123 };

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultOrdonnance = result;
        });

        // THEN
        expect(service.find).toBeCalledWith(123);
        expect(resultOrdonnance).toEqual(undefined);
        expect(mockRouter.navigate).toHaveBeenCalledWith(['404']);
      });
    });
  });
});

jest.mock('@angular/router');

import { TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of } from 'rxjs';

import { IReglement, Reglement } from '../reglement.model';
import { ReglementService } from '../service/reglement.service';

import { ReglementRoutingResolveService } from './reglement-routing-resolve.service';

describe('Service Tests', () => {
  describe('Reglement routing resolve service', () => {
    let mockRouter: Router;
    let mockActivatedRouteSnapshot: ActivatedRouteSnapshot;
    let routingResolveService: ReglementRoutingResolveService;
    let service: ReglementService;
    let resultReglement: IReglement | undefined;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        providers: [Router, ActivatedRouteSnapshot],
      });
      mockRouter = TestBed.inject(Router);
      mockActivatedRouteSnapshot = TestBed.inject(ActivatedRouteSnapshot);
      routingResolveService = TestBed.inject(ReglementRoutingResolveService);
      service = TestBed.inject(ReglementService);
      resultReglement = undefined;
    });

    describe('resolve', () => {
      it('should return IReglement returned by find', () => {
        // GIVEN
        service.find = jest.fn(id => of(new HttpResponse({ body: { id } })));
        mockActivatedRouteSnapshot.params = { id: 123 };

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultReglement = result;
        });

        // THEN
        expect(service.find).toBeCalledWith(123);
        expect(resultReglement).toEqual({ id: 123 });
      });

      it('should return new IReglement if id is not provided', () => {
        // GIVEN
        service.find = jest.fn();
        mockActivatedRouteSnapshot.params = {};

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultReglement = result;
        });

        // THEN
        expect(service.find).not.toBeCalled();
        expect(resultReglement).toEqual(new Reglement());
      });

      it('should route to 404 page if data not found in server', () => {
        // GIVEN
        jest.spyOn(service, 'find').mockReturnValue(of(new HttpResponse({ body: null as unknown as Reglement })));
        mockActivatedRouteSnapshot.params = { id: 123 };

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultReglement = result;
        });

        // THEN
        expect(service.find).toBeCalledWith(123);
        expect(resultReglement).toEqual(undefined);
        expect(mockRouter.navigate).toHaveBeenCalledWith(['404']);
      });
    });
  });
});

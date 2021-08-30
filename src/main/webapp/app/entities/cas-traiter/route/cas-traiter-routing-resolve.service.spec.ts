jest.mock('@angular/router');

import { TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of } from 'rxjs';

import { ICasTraiter, CasTraiter } from '../cas-traiter.model';
import { CasTraiterService } from '../service/cas-traiter.service';

import { CasTraiterRoutingResolveService } from './cas-traiter-routing-resolve.service';

describe('Service Tests', () => {
  describe('CasTraiter routing resolve service', () => {
    let mockRouter: Router;
    let mockActivatedRouteSnapshot: ActivatedRouteSnapshot;
    let routingResolveService: CasTraiterRoutingResolveService;
    let service: CasTraiterService;
    let resultCasTraiter: ICasTraiter | undefined;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        providers: [Router, ActivatedRouteSnapshot],
      });
      mockRouter = TestBed.inject(Router);
      mockActivatedRouteSnapshot = TestBed.inject(ActivatedRouteSnapshot);
      routingResolveService = TestBed.inject(CasTraiterRoutingResolveService);
      service = TestBed.inject(CasTraiterService);
      resultCasTraiter = undefined;
    });

    describe('resolve', () => {
      it('should return ICasTraiter returned by find', () => {
        // GIVEN
        service.find = jest.fn(id => of(new HttpResponse({ body: { id } })));
        mockActivatedRouteSnapshot.params = { id: 123 };

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultCasTraiter = result;
        });

        // THEN
        expect(service.find).toBeCalledWith(123);
        expect(resultCasTraiter).toEqual({ id: 123 });
      });

      it('should return new ICasTraiter if id is not provided', () => {
        // GIVEN
        service.find = jest.fn();
        mockActivatedRouteSnapshot.params = {};

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultCasTraiter = result;
        });

        // THEN
        expect(service.find).not.toBeCalled();
        expect(resultCasTraiter).toEqual(new CasTraiter());
      });

      it('should route to 404 page if data not found in server', () => {
        // GIVEN
        jest.spyOn(service, 'find').mockReturnValue(of(new HttpResponse({ body: null as unknown as CasTraiter })));
        mockActivatedRouteSnapshot.params = { id: 123 };

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultCasTraiter = result;
        });

        // THEN
        expect(service.find).toBeCalledWith(123);
        expect(resultCasTraiter).toEqual(undefined);
        expect(mockRouter.navigate).toHaveBeenCalledWith(['404']);
      });
    });
  });
});

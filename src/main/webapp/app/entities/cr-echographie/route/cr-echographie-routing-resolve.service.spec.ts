jest.mock('@angular/router');

import { TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of } from 'rxjs';

import { ICrEchographie, CrEchographie } from '../cr-echographie.model';
import { CrEchographieService } from '../service/cr-echographie.service';

import { CrEchographieRoutingResolveService } from './cr-echographie-routing-resolve.service';

describe('Service Tests', () => {
  describe('CrEchographie routing resolve service', () => {
    let mockRouter: Router;
    let mockActivatedRouteSnapshot: ActivatedRouteSnapshot;
    let routingResolveService: CrEchographieRoutingResolveService;
    let service: CrEchographieService;
    let resultCrEchographie: ICrEchographie | undefined;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        providers: [Router, ActivatedRouteSnapshot],
      });
      mockRouter = TestBed.inject(Router);
      mockActivatedRouteSnapshot = TestBed.inject(ActivatedRouteSnapshot);
      routingResolveService = TestBed.inject(CrEchographieRoutingResolveService);
      service = TestBed.inject(CrEchographieService);
      resultCrEchographie = undefined;
    });

    describe('resolve', () => {
      it('should return ICrEchographie returned by find', () => {
        // GIVEN
        service.find = jest.fn(id => of(new HttpResponse({ body: { id } })));
        mockActivatedRouteSnapshot.params = { id: 123 };

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultCrEchographie = result;
        });

        // THEN
        expect(service.find).toBeCalledWith(123);
        expect(resultCrEchographie).toEqual({ id: 123 });
      });

      it('should return new ICrEchographie if id is not provided', () => {
        // GIVEN
        service.find = jest.fn();
        mockActivatedRouteSnapshot.params = {};

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultCrEchographie = result;
        });

        // THEN
        expect(service.find).not.toBeCalled();
        expect(resultCrEchographie).toEqual(new CrEchographie());
      });

      it('should route to 404 page if data not found in server', () => {
        // GIVEN
        jest.spyOn(service, 'find').mockReturnValue(of(new HttpResponse({ body: null as unknown as CrEchographie })));
        mockActivatedRouteSnapshot.params = { id: 123 };

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultCrEchographie = result;
        });

        // THEN
        expect(service.find).toBeCalledWith(123);
        expect(resultCrEchographie).toEqual(undefined);
        expect(mockRouter.navigate).toHaveBeenCalledWith(['404']);
      });
    });
  });
});

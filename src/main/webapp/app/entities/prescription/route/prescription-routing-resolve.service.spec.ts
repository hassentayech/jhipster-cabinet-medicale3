jest.mock('@angular/router');

import { TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of } from 'rxjs';

import { IPrescription, Prescription } from '../prescription.model';
import { PrescriptionService } from '../service/prescription.service';

import { PrescriptionRoutingResolveService } from './prescription-routing-resolve.service';

describe('Service Tests', () => {
  describe('Prescription routing resolve service', () => {
    let mockRouter: Router;
    let mockActivatedRouteSnapshot: ActivatedRouteSnapshot;
    let routingResolveService: PrescriptionRoutingResolveService;
    let service: PrescriptionService;
    let resultPrescription: IPrescription | undefined;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        providers: [Router, ActivatedRouteSnapshot],
      });
      mockRouter = TestBed.inject(Router);
      mockActivatedRouteSnapshot = TestBed.inject(ActivatedRouteSnapshot);
      routingResolveService = TestBed.inject(PrescriptionRoutingResolveService);
      service = TestBed.inject(PrescriptionService);
      resultPrescription = undefined;
    });

    describe('resolve', () => {
      it('should return IPrescription returned by find', () => {
        // GIVEN
        service.find = jest.fn(id => of(new HttpResponse({ body: { id } })));
        mockActivatedRouteSnapshot.params = { id: 123 };

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultPrescription = result;
        });

        // THEN
        expect(service.find).toBeCalledWith(123);
        expect(resultPrescription).toEqual({ id: 123 });
      });

      it('should return new IPrescription if id is not provided', () => {
        // GIVEN
        service.find = jest.fn();
        mockActivatedRouteSnapshot.params = {};

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultPrescription = result;
        });

        // THEN
        expect(service.find).not.toBeCalled();
        expect(resultPrescription).toEqual(new Prescription());
      });

      it('should route to 404 page if data not found in server', () => {
        // GIVEN
        jest.spyOn(service, 'find').mockReturnValue(of(new HttpResponse({ body: null as unknown as Prescription })));
        mockActivatedRouteSnapshot.params = { id: 123 };

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultPrescription = result;
        });

        // THEN
        expect(service.find).toBeCalledWith(123);
        expect(resultPrescription).toEqual(undefined);
        expect(mockRouter.navigate).toHaveBeenCalledWith(['404']);
      });
    });
  });
});

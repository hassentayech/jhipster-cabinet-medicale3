jest.mock('@angular/router');

import { TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of } from 'rxjs';

import { ICertificat, Certificat } from '../certificat.model';
import { CertificatService } from '../service/certificat.service';

import { CertificatRoutingResolveService } from './certificat-routing-resolve.service';

describe('Service Tests', () => {
  describe('Certificat routing resolve service', () => {
    let mockRouter: Router;
    let mockActivatedRouteSnapshot: ActivatedRouteSnapshot;
    let routingResolveService: CertificatRoutingResolveService;
    let service: CertificatService;
    let resultCertificat: ICertificat | undefined;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        providers: [Router, ActivatedRouteSnapshot],
      });
      mockRouter = TestBed.inject(Router);
      mockActivatedRouteSnapshot = TestBed.inject(ActivatedRouteSnapshot);
      routingResolveService = TestBed.inject(CertificatRoutingResolveService);
      service = TestBed.inject(CertificatService);
      resultCertificat = undefined;
    });

    describe('resolve', () => {
      it('should return ICertificat returned by find', () => {
        // GIVEN
        service.find = jest.fn(id => of(new HttpResponse({ body: { id } })));
        mockActivatedRouteSnapshot.params = { id: 123 };

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultCertificat = result;
        });

        // THEN
        expect(service.find).toBeCalledWith(123);
        expect(resultCertificat).toEqual({ id: 123 });
      });

      it('should return new ICertificat if id is not provided', () => {
        // GIVEN
        service.find = jest.fn();
        mockActivatedRouteSnapshot.params = {};

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultCertificat = result;
        });

        // THEN
        expect(service.find).not.toBeCalled();
        expect(resultCertificat).toEqual(new Certificat());
      });

      it('should route to 404 page if data not found in server', () => {
        // GIVEN
        jest.spyOn(service, 'find').mockReturnValue(of(new HttpResponse({ body: null as unknown as Certificat })));
        mockActivatedRouteSnapshot.params = { id: 123 };

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultCertificat = result;
        });

        // THEN
        expect(service.find).toBeCalledWith(123);
        expect(resultCertificat).toEqual(undefined);
        expect(mockRouter.navigate).toHaveBeenCalledWith(['404']);
      });
    });
  });
});

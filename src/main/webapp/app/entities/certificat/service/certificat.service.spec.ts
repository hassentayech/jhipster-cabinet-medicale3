import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { ICertificat, Certificat } from '../certificat.model';

import { CertificatService } from './certificat.service';

describe('Service Tests', () => {
  describe('Certificat Service', () => {
    let service: CertificatService;
    let httpMock: HttpTestingController;
    let elemDefault: ICertificat;
    let expectedResult: ICertificat | ICertificat[] | boolean | null;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
      });
      expectedResult = null;
      service = TestBed.inject(CertificatService);
      httpMock = TestBed.inject(HttpTestingController);

      elemDefault = {
        id: 0,
        nbrJours: 0,
        description: 'AAAAAAA',
      };
    });

    describe('Service methods', () => {
      it('should find an element', () => {
        const returnedFromService = Object.assign({}, elemDefault);

        service.find(123).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'GET' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(elemDefault);
      });

      it('should create a Certificat', () => {
        const returnedFromService = Object.assign(
          {
            id: 0,
          },
          elemDefault
        );

        const expected = Object.assign({}, returnedFromService);

        service.create(new Certificat()).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'POST' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should update a Certificat', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            nbrJours: 1,
            description: 'BBBBBB',
          },
          elemDefault
        );

        const expected = Object.assign({}, returnedFromService);

        service.update(expected).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PUT' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should partial update a Certificat', () => {
        const patchObject = Object.assign(
          {
            description: 'BBBBBB',
          },
          new Certificat()
        );

        const returnedFromService = Object.assign(patchObject, elemDefault);

        const expected = Object.assign({}, returnedFromService);

        service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PATCH' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should return a list of Certificat', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            nbrJours: 1,
            description: 'BBBBBB',
          },
          elemDefault
        );

        const expected = Object.assign({}, returnedFromService);

        service.query().subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'GET' });
        req.flush([returnedFromService]);
        httpMock.verify();
        expect(expectedResult).toContainEqual(expected);
      });

      it('should delete a Certificat', () => {
        service.delete(123).subscribe(resp => (expectedResult = resp.ok));

        const req = httpMock.expectOne({ method: 'DELETE' });
        req.flush({ status: 200 });
        expect(expectedResult);
      });

      describe('addCertificatToCollectionIfMissing', () => {
        it('should add a Certificat to an empty array', () => {
          const certificat: ICertificat = { id: 123 };
          expectedResult = service.addCertificatToCollectionIfMissing([], certificat);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(certificat);
        });

        it('should not add a Certificat to an array that contains it', () => {
          const certificat: ICertificat = { id: 123 };
          const certificatCollection: ICertificat[] = [
            {
              ...certificat,
            },
            { id: 456 },
          ];
          expectedResult = service.addCertificatToCollectionIfMissing(certificatCollection, certificat);
          expect(expectedResult).toHaveLength(2);
        });

        it("should add a Certificat to an array that doesn't contain it", () => {
          const certificat: ICertificat = { id: 123 };
          const certificatCollection: ICertificat[] = [{ id: 456 }];
          expectedResult = service.addCertificatToCollectionIfMissing(certificatCollection, certificat);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(certificat);
        });

        it('should add only unique Certificat to an array', () => {
          const certificatArray: ICertificat[] = [{ id: 123 }, { id: 456 }, { id: 14385 }];
          const certificatCollection: ICertificat[] = [{ id: 123 }];
          expectedResult = service.addCertificatToCollectionIfMissing(certificatCollection, ...certificatArray);
          expect(expectedResult).toHaveLength(3);
        });

        it('should accept varargs', () => {
          const certificat: ICertificat = { id: 123 };
          const certificat2: ICertificat = { id: 456 };
          expectedResult = service.addCertificatToCollectionIfMissing([], certificat, certificat2);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(certificat);
          expect(expectedResult).toContain(certificat2);
        });

        it('should accept null and undefined values', () => {
          const certificat: ICertificat = { id: 123 };
          expectedResult = service.addCertificatToCollectionIfMissing([], null, certificat, undefined);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(certificat);
        });

        it('should return initial array if no Certificat is added', () => {
          const certificatCollection: ICertificat[] = [{ id: 123 }];
          expectedResult = service.addCertificatToCollectionIfMissing(certificatCollection, undefined, null);
          expect(expectedResult).toEqual(certificatCollection);
        });
      });
    });

    afterEach(() => {
      httpMock.verify();
    });
  });
});

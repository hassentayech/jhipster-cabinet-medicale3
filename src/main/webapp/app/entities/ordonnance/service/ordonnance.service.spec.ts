import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { IOrdonnance, Ordonnance } from '../ordonnance.model';

import { OrdonnanceService } from './ordonnance.service';

describe('Service Tests', () => {
  describe('Ordonnance Service', () => {
    let service: OrdonnanceService;
    let httpMock: HttpTestingController;
    let elemDefault: IOrdonnance;
    let expectedResult: IOrdonnance | IOrdonnance[] | boolean | null;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
      });
      expectedResult = null;
      service = TestBed.inject(OrdonnanceService);
      httpMock = TestBed.inject(HttpTestingController);

      elemDefault = {
        id: 0,
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

      it('should create a Ordonnance', () => {
        const returnedFromService = Object.assign(
          {
            id: 0,
          },
          elemDefault
        );

        const expected = Object.assign({}, returnedFromService);

        service.create(new Ordonnance()).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'POST' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should update a Ordonnance', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
          },
          elemDefault
        );

        const expected = Object.assign({}, returnedFromService);

        service.update(expected).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PUT' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should partial update a Ordonnance', () => {
        const patchObject = Object.assign({}, new Ordonnance());

        const returnedFromService = Object.assign(patchObject, elemDefault);

        const expected = Object.assign({}, returnedFromService);

        service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PATCH' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should return a list of Ordonnance', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
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

      it('should delete a Ordonnance', () => {
        service.delete(123).subscribe(resp => (expectedResult = resp.ok));

        const req = httpMock.expectOne({ method: 'DELETE' });
        req.flush({ status: 200 });
        expect(expectedResult);
      });

      describe('addOrdonnanceToCollectionIfMissing', () => {
        it('should add a Ordonnance to an empty array', () => {
          const ordonnance: IOrdonnance = { id: 123 };
          expectedResult = service.addOrdonnanceToCollectionIfMissing([], ordonnance);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(ordonnance);
        });

        it('should not add a Ordonnance to an array that contains it', () => {
          const ordonnance: IOrdonnance = { id: 123 };
          const ordonnanceCollection: IOrdonnance[] = [
            {
              ...ordonnance,
            },
            { id: 456 },
          ];
          expectedResult = service.addOrdonnanceToCollectionIfMissing(ordonnanceCollection, ordonnance);
          expect(expectedResult).toHaveLength(2);
        });

        it("should add a Ordonnance to an array that doesn't contain it", () => {
          const ordonnance: IOrdonnance = { id: 123 };
          const ordonnanceCollection: IOrdonnance[] = [{ id: 456 }];
          expectedResult = service.addOrdonnanceToCollectionIfMissing(ordonnanceCollection, ordonnance);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(ordonnance);
        });

        it('should add only unique Ordonnance to an array', () => {
          const ordonnanceArray: IOrdonnance[] = [{ id: 123 }, { id: 456 }, { id: 32963 }];
          const ordonnanceCollection: IOrdonnance[] = [{ id: 123 }];
          expectedResult = service.addOrdonnanceToCollectionIfMissing(ordonnanceCollection, ...ordonnanceArray);
          expect(expectedResult).toHaveLength(3);
        });

        it('should accept varargs', () => {
          const ordonnance: IOrdonnance = { id: 123 };
          const ordonnance2: IOrdonnance = { id: 456 };
          expectedResult = service.addOrdonnanceToCollectionIfMissing([], ordonnance, ordonnance2);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(ordonnance);
          expect(expectedResult).toContain(ordonnance2);
        });

        it('should accept null and undefined values', () => {
          const ordonnance: IOrdonnance = { id: 123 };
          expectedResult = service.addOrdonnanceToCollectionIfMissing([], null, ordonnance, undefined);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(ordonnance);
        });

        it('should return initial array if no Ordonnance is added', () => {
          const ordonnanceCollection: IOrdonnance[] = [{ id: 123 }];
          expectedResult = service.addOrdonnanceToCollectionIfMissing(ordonnanceCollection, undefined, null);
          expect(expectedResult).toEqual(ordonnanceCollection);
        });
      });
    });

    afterEach(() => {
      httpMock.verify();
    });
  });
});

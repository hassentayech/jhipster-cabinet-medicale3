import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { TypeAntecedent } from 'app/entities/enumerations/type-antecedent.model';
import { IAntecedent, Antecedent } from '../antecedent.model';

import { AntecedentService } from './antecedent.service';

describe('Service Tests', () => {
  describe('Antecedent Service', () => {
    let service: AntecedentService;
    let httpMock: HttpTestingController;
    let elemDefault: IAntecedent;
    let expectedResult: IAntecedent | IAntecedent[] | boolean | null;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
      });
      expectedResult = null;
      service = TestBed.inject(AntecedentService);
      httpMock = TestBed.inject(HttpTestingController);

      elemDefault = {
        id: 0,
        type: TypeAntecedent.MEDICAL,
        periode: 'AAAAAAA',
        antecedent: 'AAAAAAA',
        traitement: 'AAAAAAA',
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

      it('should create a Antecedent', () => {
        const returnedFromService = Object.assign(
          {
            id: 0,
          },
          elemDefault
        );

        const expected = Object.assign({}, returnedFromService);

        service.create(new Antecedent()).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'POST' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should update a Antecedent', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            type: 'BBBBBB',
            periode: 'BBBBBB',
            antecedent: 'BBBBBB',
            traitement: 'BBBBBB',
          },
          elemDefault
        );

        const expected = Object.assign({}, returnedFromService);

        service.update(expected).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PUT' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should partial update a Antecedent', () => {
        const patchObject = Object.assign(
          {
            periode: 'BBBBBB',
          },
          new Antecedent()
        );

        const returnedFromService = Object.assign(patchObject, elemDefault);

        const expected = Object.assign({}, returnedFromService);

        service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PATCH' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should return a list of Antecedent', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            type: 'BBBBBB',
            periode: 'BBBBBB',
            antecedent: 'BBBBBB',
            traitement: 'BBBBBB',
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

      it('should delete a Antecedent', () => {
        service.delete(123).subscribe(resp => (expectedResult = resp.ok));

        const req = httpMock.expectOne({ method: 'DELETE' });
        req.flush({ status: 200 });
        expect(expectedResult);
      });

      describe('addAntecedentToCollectionIfMissing', () => {
        it('should add a Antecedent to an empty array', () => {
          const antecedent: IAntecedent = { id: 123 };
          expectedResult = service.addAntecedentToCollectionIfMissing([], antecedent);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(antecedent);
        });

        it('should not add a Antecedent to an array that contains it', () => {
          const antecedent: IAntecedent = { id: 123 };
          const antecedentCollection: IAntecedent[] = [
            {
              ...antecedent,
            },
            { id: 456 },
          ];
          expectedResult = service.addAntecedentToCollectionIfMissing(antecedentCollection, antecedent);
          expect(expectedResult).toHaveLength(2);
        });

        it("should add a Antecedent to an array that doesn't contain it", () => {
          const antecedent: IAntecedent = { id: 123 };
          const antecedentCollection: IAntecedent[] = [{ id: 456 }];
          expectedResult = service.addAntecedentToCollectionIfMissing(antecedentCollection, antecedent);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(antecedent);
        });

        it('should add only unique Antecedent to an array', () => {
          const antecedentArray: IAntecedent[] = [{ id: 123 }, { id: 456 }, { id: 74066 }];
          const antecedentCollection: IAntecedent[] = [{ id: 123 }];
          expectedResult = service.addAntecedentToCollectionIfMissing(antecedentCollection, ...antecedentArray);
          expect(expectedResult).toHaveLength(3);
        });

        it('should accept varargs', () => {
          const antecedent: IAntecedent = { id: 123 };
          const antecedent2: IAntecedent = { id: 456 };
          expectedResult = service.addAntecedentToCollectionIfMissing([], antecedent, antecedent2);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(antecedent);
          expect(expectedResult).toContain(antecedent2);
        });

        it('should accept null and undefined values', () => {
          const antecedent: IAntecedent = { id: 123 };
          expectedResult = service.addAntecedentToCollectionIfMissing([], null, antecedent, undefined);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(antecedent);
        });

        it('should return initial array if no Antecedent is added', () => {
          const antecedentCollection: IAntecedent[] = [{ id: 123 }];
          expectedResult = service.addAntecedentToCollectionIfMissing(antecedentCollection, undefined, null);
          expect(expectedResult).toEqual(antecedentCollection);
        });
      });
    });

    afterEach(() => {
      httpMock.verify();
    });
  });
});

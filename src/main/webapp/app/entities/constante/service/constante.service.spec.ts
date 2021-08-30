import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import * as dayjs from 'dayjs';

import { DATE_TIME_FORMAT } from 'app/config/input.constants';
import { IConstante, Constante } from '../constante.model';

import { ConstanteService } from './constante.service';

describe('Service Tests', () => {
  describe('Constante Service', () => {
    let service: ConstanteService;
    let httpMock: HttpTestingController;
    let elemDefault: IConstante;
    let expectedResult: IConstante | IConstante[] | boolean | null;
    let currentDate: dayjs.Dayjs;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
      });
      expectedResult = null;
      service = TestBed.inject(ConstanteService);
      httpMock = TestBed.inject(HttpTestingController);
      currentDate = dayjs();

      elemDefault = {
        id: 0,
        date: currentDate,
        poid: 0,
        taille: 0,
        pas: 0,
        pad: 0,
        pouls: 0,
        temp: 0,
        glycemie: 0,
        cholesterol: 0,
      };
    });

    describe('Service methods', () => {
      it('should find an element', () => {
        const returnedFromService = Object.assign(
          {
            date: currentDate.format(DATE_TIME_FORMAT),
          },
          elemDefault
        );

        service.find(123).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'GET' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(elemDefault);
      });

      it('should create a Constante', () => {
        const returnedFromService = Object.assign(
          {
            id: 0,
            date: currentDate.format(DATE_TIME_FORMAT),
          },
          elemDefault
        );

        const expected = Object.assign(
          {
            date: currentDate,
          },
          returnedFromService
        );

        service.create(new Constante()).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'POST' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should update a Constante', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            date: currentDate.format(DATE_TIME_FORMAT),
            poid: 1,
            taille: 1,
            pas: 1,
            pad: 1,
            pouls: 1,
            temp: 1,
            glycemie: 1,
            cholesterol: 1,
          },
          elemDefault
        );

        const expected = Object.assign(
          {
            date: currentDate,
          },
          returnedFromService
        );

        service.update(expected).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PUT' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should partial update a Constante', () => {
        const patchObject = Object.assign(
          {
            taille: 1,
            pas: 1,
            temp: 1,
          },
          new Constante()
        );

        const returnedFromService = Object.assign(patchObject, elemDefault);

        const expected = Object.assign(
          {
            date: currentDate,
          },
          returnedFromService
        );

        service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PATCH' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should return a list of Constante', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            date: currentDate.format(DATE_TIME_FORMAT),
            poid: 1,
            taille: 1,
            pas: 1,
            pad: 1,
            pouls: 1,
            temp: 1,
            glycemie: 1,
            cholesterol: 1,
          },
          elemDefault
        );

        const expected = Object.assign(
          {
            date: currentDate,
          },
          returnedFromService
        );

        service.query().subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'GET' });
        req.flush([returnedFromService]);
        httpMock.verify();
        expect(expectedResult).toContainEqual(expected);
      });

      it('should delete a Constante', () => {
        service.delete(123).subscribe(resp => (expectedResult = resp.ok));

        const req = httpMock.expectOne({ method: 'DELETE' });
        req.flush({ status: 200 });
        expect(expectedResult);
      });

      describe('addConstanteToCollectionIfMissing', () => {
        it('should add a Constante to an empty array', () => {
          const constante: IConstante = { id: 123 };
          expectedResult = service.addConstanteToCollectionIfMissing([], constante);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(constante);
        });

        it('should not add a Constante to an array that contains it', () => {
          const constante: IConstante = { id: 123 };
          const constanteCollection: IConstante[] = [
            {
              ...constante,
            },
            { id: 456 },
          ];
          expectedResult = service.addConstanteToCollectionIfMissing(constanteCollection, constante);
          expect(expectedResult).toHaveLength(2);
        });

        it("should add a Constante to an array that doesn't contain it", () => {
          const constante: IConstante = { id: 123 };
          const constanteCollection: IConstante[] = [{ id: 456 }];
          expectedResult = service.addConstanteToCollectionIfMissing(constanteCollection, constante);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(constante);
        });

        it('should add only unique Constante to an array', () => {
          const constanteArray: IConstante[] = [{ id: 123 }, { id: 456 }, { id: 59111 }];
          const constanteCollection: IConstante[] = [{ id: 123 }];
          expectedResult = service.addConstanteToCollectionIfMissing(constanteCollection, ...constanteArray);
          expect(expectedResult).toHaveLength(3);
        });

        it('should accept varargs', () => {
          const constante: IConstante = { id: 123 };
          const constante2: IConstante = { id: 456 };
          expectedResult = service.addConstanteToCollectionIfMissing([], constante, constante2);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(constante);
          expect(expectedResult).toContain(constante2);
        });

        it('should accept null and undefined values', () => {
          const constante: IConstante = { id: 123 };
          expectedResult = service.addConstanteToCollectionIfMissing([], null, constante, undefined);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(constante);
        });

        it('should return initial array if no Constante is added', () => {
          const constanteCollection: IConstante[] = [{ id: 123 }];
          expectedResult = service.addConstanteToCollectionIfMissing(constanteCollection, undefined, null);
          expect(expectedResult).toEqual(constanteCollection);
        });
      });
    });

    afterEach(() => {
      httpMock.verify();
    });
  });
});

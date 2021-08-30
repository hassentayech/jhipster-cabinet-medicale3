import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import * as dayjs from 'dayjs';

import { DATE_FORMAT } from 'app/config/input.constants';
import { EtatActuel } from 'app/entities/enumerations/etat-actuel.model';
import { ModeFacturation } from 'app/entities/enumerations/mode-facturation.model';
import { ICasTraiter, CasTraiter } from '../cas-traiter.model';

import { CasTraiterService } from './cas-traiter.service';

describe('Service Tests', () => {
  describe('CasTraiter Service', () => {
    let service: CasTraiterService;
    let httpMock: HttpTestingController;
    let elemDefault: ICasTraiter;
    let expectedResult: ICasTraiter | ICasTraiter[] | boolean | null;
    let currentDate: dayjs.Dayjs;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
      });
      expectedResult = null;
      service = TestBed.inject(CasTraiterService);
      httpMock = TestBed.inject(HttpTestingController);
      currentDate = dayjs();

      elemDefault = {
        id: 0,
        cas: 'AAAAAAA',
        depuis: currentDate,
        histoire: 'AAAAAAA',
        remarques: 'AAAAAAA',
        etatActuel: EtatActuel.EN_TRAITEMENT,
        modeFacturation: ModeFacturation.VISITES,
        prixForfaitaire: 0,
      };
    });

    describe('Service methods', () => {
      it('should find an element', () => {
        const returnedFromService = Object.assign(
          {
            depuis: currentDate.format(DATE_FORMAT),
          },
          elemDefault
        );

        service.find(123).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'GET' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(elemDefault);
      });

      it('should create a CasTraiter', () => {
        const returnedFromService = Object.assign(
          {
            id: 0,
            depuis: currentDate.format(DATE_FORMAT),
          },
          elemDefault
        );

        const expected = Object.assign(
          {
            depuis: currentDate,
          },
          returnedFromService
        );

        service.create(new CasTraiter()).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'POST' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should update a CasTraiter', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            cas: 'BBBBBB',
            depuis: currentDate.format(DATE_FORMAT),
            histoire: 'BBBBBB',
            remarques: 'BBBBBB',
            etatActuel: 'BBBBBB',
            modeFacturation: 'BBBBBB',
            prixForfaitaire: 1,
          },
          elemDefault
        );

        const expected = Object.assign(
          {
            depuis: currentDate,
          },
          returnedFromService
        );

        service.update(expected).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PUT' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should partial update a CasTraiter', () => {
        const patchObject = Object.assign(
          {
            cas: 'BBBBBB',
            etatActuel: 'BBBBBB',
            modeFacturation: 'BBBBBB',
          },
          new CasTraiter()
        );

        const returnedFromService = Object.assign(patchObject, elemDefault);

        const expected = Object.assign(
          {
            depuis: currentDate,
          },
          returnedFromService
        );

        service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PATCH' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should return a list of CasTraiter', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            cas: 'BBBBBB',
            depuis: currentDate.format(DATE_FORMAT),
            histoire: 'BBBBBB',
            remarques: 'BBBBBB',
            etatActuel: 'BBBBBB',
            modeFacturation: 'BBBBBB',
            prixForfaitaire: 1,
          },
          elemDefault
        );

        const expected = Object.assign(
          {
            depuis: currentDate,
          },
          returnedFromService
        );

        service.query().subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'GET' });
        req.flush([returnedFromService]);
        httpMock.verify();
        expect(expectedResult).toContainEqual(expected);
      });

      it('should delete a CasTraiter', () => {
        service.delete(123).subscribe(resp => (expectedResult = resp.ok));

        const req = httpMock.expectOne({ method: 'DELETE' });
        req.flush({ status: 200 });
        expect(expectedResult);
      });

      describe('addCasTraiterToCollectionIfMissing', () => {
        it('should add a CasTraiter to an empty array', () => {
          const casTraiter: ICasTraiter = { id: 123 };
          expectedResult = service.addCasTraiterToCollectionIfMissing([], casTraiter);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(casTraiter);
        });

        it('should not add a CasTraiter to an array that contains it', () => {
          const casTraiter: ICasTraiter = { id: 123 };
          const casTraiterCollection: ICasTraiter[] = [
            {
              ...casTraiter,
            },
            { id: 456 },
          ];
          expectedResult = service.addCasTraiterToCollectionIfMissing(casTraiterCollection, casTraiter);
          expect(expectedResult).toHaveLength(2);
        });

        it("should add a CasTraiter to an array that doesn't contain it", () => {
          const casTraiter: ICasTraiter = { id: 123 };
          const casTraiterCollection: ICasTraiter[] = [{ id: 456 }];
          expectedResult = service.addCasTraiterToCollectionIfMissing(casTraiterCollection, casTraiter);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(casTraiter);
        });

        it('should add only unique CasTraiter to an array', () => {
          const casTraiterArray: ICasTraiter[] = [{ id: 123 }, { id: 456 }, { id: 25486 }];
          const casTraiterCollection: ICasTraiter[] = [{ id: 123 }];
          expectedResult = service.addCasTraiterToCollectionIfMissing(casTraiterCollection, ...casTraiterArray);
          expect(expectedResult).toHaveLength(3);
        });

        it('should accept varargs', () => {
          const casTraiter: ICasTraiter = { id: 123 };
          const casTraiter2: ICasTraiter = { id: 456 };
          expectedResult = service.addCasTraiterToCollectionIfMissing([], casTraiter, casTraiter2);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(casTraiter);
          expect(expectedResult).toContain(casTraiter2);
        });

        it('should accept null and undefined values', () => {
          const casTraiter: ICasTraiter = { id: 123 };
          expectedResult = service.addCasTraiterToCollectionIfMissing([], null, casTraiter, undefined);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(casTraiter);
        });

        it('should return initial array if no CasTraiter is added', () => {
          const casTraiterCollection: ICasTraiter[] = [{ id: 123 }];
          expectedResult = service.addCasTraiterToCollectionIfMissing(casTraiterCollection, undefined, null);
          expect(expectedResult).toEqual(casTraiterCollection);
        });
      });
    });

    afterEach(() => {
      httpMock.verify();
    });
  });
});

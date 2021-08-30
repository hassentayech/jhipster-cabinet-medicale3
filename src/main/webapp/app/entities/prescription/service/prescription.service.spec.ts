import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { IPrescription, Prescription } from '../prescription.model';

import { PrescriptionService } from './prescription.service';

describe('Service Tests', () => {
  describe('Prescription Service', () => {
    let service: PrescriptionService;
    let httpMock: HttpTestingController;
    let elemDefault: IPrescription;
    let expectedResult: IPrescription | IPrescription[] | boolean | null;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
      });
      expectedResult = null;
      service = TestBed.inject(PrescriptionService);
      httpMock = TestBed.inject(HttpTestingController);

      elemDefault = {
        id: 0,
        prescription: 'AAAAAAA',
        prise: 'AAAAAAA',
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

      it('should create a Prescription', () => {
        const returnedFromService = Object.assign(
          {
            id: 0,
          },
          elemDefault
        );

        const expected = Object.assign({}, returnedFromService);

        service.create(new Prescription()).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'POST' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should update a Prescription', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            prescription: 'BBBBBB',
            prise: 'BBBBBB',
          },
          elemDefault
        );

        const expected = Object.assign({}, returnedFromService);

        service.update(expected).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PUT' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should partial update a Prescription', () => {
        const patchObject = Object.assign(
          {
            prescription: 'BBBBBB',
            prise: 'BBBBBB',
          },
          new Prescription()
        );

        const returnedFromService = Object.assign(patchObject, elemDefault);

        const expected = Object.assign({}, returnedFromService);

        service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PATCH' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should return a list of Prescription', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            prescription: 'BBBBBB',
            prise: 'BBBBBB',
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

      it('should delete a Prescription', () => {
        service.delete(123).subscribe(resp => (expectedResult = resp.ok));

        const req = httpMock.expectOne({ method: 'DELETE' });
        req.flush({ status: 200 });
        expect(expectedResult);
      });

      describe('addPrescriptionToCollectionIfMissing', () => {
        it('should add a Prescription to an empty array', () => {
          const prescription: IPrescription = { id: 123 };
          expectedResult = service.addPrescriptionToCollectionIfMissing([], prescription);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(prescription);
        });

        it('should not add a Prescription to an array that contains it', () => {
          const prescription: IPrescription = { id: 123 };
          const prescriptionCollection: IPrescription[] = [
            {
              ...prescription,
            },
            { id: 456 },
          ];
          expectedResult = service.addPrescriptionToCollectionIfMissing(prescriptionCollection, prescription);
          expect(expectedResult).toHaveLength(2);
        });

        it("should add a Prescription to an array that doesn't contain it", () => {
          const prescription: IPrescription = { id: 123 };
          const prescriptionCollection: IPrescription[] = [{ id: 456 }];
          expectedResult = service.addPrescriptionToCollectionIfMissing(prescriptionCollection, prescription);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(prescription);
        });

        it('should add only unique Prescription to an array', () => {
          const prescriptionArray: IPrescription[] = [{ id: 123 }, { id: 456 }, { id: 58455 }];
          const prescriptionCollection: IPrescription[] = [{ id: 123 }];
          expectedResult = service.addPrescriptionToCollectionIfMissing(prescriptionCollection, ...prescriptionArray);
          expect(expectedResult).toHaveLength(3);
        });

        it('should accept varargs', () => {
          const prescription: IPrescription = { id: 123 };
          const prescription2: IPrescription = { id: 456 };
          expectedResult = service.addPrescriptionToCollectionIfMissing([], prescription, prescription2);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(prescription);
          expect(expectedResult).toContain(prescription2);
        });

        it('should accept null and undefined values', () => {
          const prescription: IPrescription = { id: 123 };
          expectedResult = service.addPrescriptionToCollectionIfMissing([], null, prescription, undefined);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(prescription);
        });

        it('should return initial array if no Prescription is added', () => {
          const prescriptionCollection: IPrescription[] = [{ id: 123 }];
          expectedResult = service.addPrescriptionToCollectionIfMissing(prescriptionCollection, undefined, null);
          expect(expectedResult).toEqual(prescriptionCollection);
        });
      });
    });

    afterEach(() => {
      httpMock.verify();
    });
  });
});

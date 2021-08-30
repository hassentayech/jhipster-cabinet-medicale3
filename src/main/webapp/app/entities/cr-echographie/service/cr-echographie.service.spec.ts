import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { EchoAspect } from 'app/entities/enumerations/echo-aspect.model';
import { ICrEchographie, CrEchographie } from '../cr-echographie.model';

import { CrEchographieService } from './cr-echographie.service';

describe('Service Tests', () => {
  describe('CrEchographie Service', () => {
    let service: CrEchographieService;
    let httpMock: HttpTestingController;
    let elemDefault: ICrEchographie;
    let expectedResult: ICrEchographie | ICrEchographie[] | boolean | null;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
      });
      expectedResult = null;
      service = TestBed.inject(CrEchographieService);
      httpMock = TestBed.inject(HttpTestingController);

      elemDefault = {
        id: 0,
        aspectFoie: EchoAspect.TAILLE_ECHOSTRUCURE_NORMALE,
        observationFoie: 'AAAAAAA',
        aspectVesicule: EchoAspect.TAILLE_ECHOSTRUCURE_NORMALE,
        observationVesicule: 'AAAAAAA',
        aspectTrocVoieVeine: EchoAspect.TAILLE_ECHOSTRUCURE_NORMALE,
        observationTrocVoieVeine: 'AAAAAAA',
        aspectReins: EchoAspect.TAILLE_ECHOSTRUCURE_NORMALE,
        observationReins: 'AAAAAAA',
        aspectRate: EchoAspect.TAILLE_ECHOSTRUCURE_NORMALE,
        observationRate: 'AAAAAAA',
        aspectPancreas: EchoAspect.TAILLE_ECHOSTRUCURE_NORMALE,
        observationPancreas: 'AAAAAAA',
        autreObservation: 'AAAAAAA',
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

      it('should create a CrEchographie', () => {
        const returnedFromService = Object.assign(
          {
            id: 0,
          },
          elemDefault
        );

        const expected = Object.assign({}, returnedFromService);

        service.create(new CrEchographie()).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'POST' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should update a CrEchographie', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            aspectFoie: 'BBBBBB',
            observationFoie: 'BBBBBB',
            aspectVesicule: 'BBBBBB',
            observationVesicule: 'BBBBBB',
            aspectTrocVoieVeine: 'BBBBBB',
            observationTrocVoieVeine: 'BBBBBB',
            aspectReins: 'BBBBBB',
            observationReins: 'BBBBBB',
            aspectRate: 'BBBBBB',
            observationRate: 'BBBBBB',
            aspectPancreas: 'BBBBBB',
            observationPancreas: 'BBBBBB',
            autreObservation: 'BBBBBB',
          },
          elemDefault
        );

        const expected = Object.assign({}, returnedFromService);

        service.update(expected).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PUT' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should partial update a CrEchographie', () => {
        const patchObject = Object.assign(
          {
            aspectFoie: 'BBBBBB',
            observationFoie: 'BBBBBB',
            aspectTrocVoieVeine: 'BBBBBB',
            observationTrocVoieVeine: 'BBBBBB',
            observationReins: 'BBBBBB',
            observationRate: 'BBBBBB',
            aspectPancreas: 'BBBBBB',
            observationPancreas: 'BBBBBB',
            autreObservation: 'BBBBBB',
          },
          new CrEchographie()
        );

        const returnedFromService = Object.assign(patchObject, elemDefault);

        const expected = Object.assign({}, returnedFromService);

        service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PATCH' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should return a list of CrEchographie', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            aspectFoie: 'BBBBBB',
            observationFoie: 'BBBBBB',
            aspectVesicule: 'BBBBBB',
            observationVesicule: 'BBBBBB',
            aspectTrocVoieVeine: 'BBBBBB',
            observationTrocVoieVeine: 'BBBBBB',
            aspectReins: 'BBBBBB',
            observationReins: 'BBBBBB',
            aspectRate: 'BBBBBB',
            observationRate: 'BBBBBB',
            aspectPancreas: 'BBBBBB',
            observationPancreas: 'BBBBBB',
            autreObservation: 'BBBBBB',
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

      it('should delete a CrEchographie', () => {
        service.delete(123).subscribe(resp => (expectedResult = resp.ok));

        const req = httpMock.expectOne({ method: 'DELETE' });
        req.flush({ status: 200 });
        expect(expectedResult);
      });

      describe('addCrEchographieToCollectionIfMissing', () => {
        it('should add a CrEchographie to an empty array', () => {
          const crEchographie: ICrEchographie = { id: 123 };
          expectedResult = service.addCrEchographieToCollectionIfMissing([], crEchographie);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(crEchographie);
        });

        it('should not add a CrEchographie to an array that contains it', () => {
          const crEchographie: ICrEchographie = { id: 123 };
          const crEchographieCollection: ICrEchographie[] = [
            {
              ...crEchographie,
            },
            { id: 456 },
          ];
          expectedResult = service.addCrEchographieToCollectionIfMissing(crEchographieCollection, crEchographie);
          expect(expectedResult).toHaveLength(2);
        });

        it("should add a CrEchographie to an array that doesn't contain it", () => {
          const crEchographie: ICrEchographie = { id: 123 };
          const crEchographieCollection: ICrEchographie[] = [{ id: 456 }];
          expectedResult = service.addCrEchographieToCollectionIfMissing(crEchographieCollection, crEchographie);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(crEchographie);
        });

        it('should add only unique CrEchographie to an array', () => {
          const crEchographieArray: ICrEchographie[] = [{ id: 123 }, { id: 456 }, { id: 21824 }];
          const crEchographieCollection: ICrEchographie[] = [{ id: 123 }];
          expectedResult = service.addCrEchographieToCollectionIfMissing(crEchographieCollection, ...crEchographieArray);
          expect(expectedResult).toHaveLength(3);
        });

        it('should accept varargs', () => {
          const crEchographie: ICrEchographie = { id: 123 };
          const crEchographie2: ICrEchographie = { id: 456 };
          expectedResult = service.addCrEchographieToCollectionIfMissing([], crEchographie, crEchographie2);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(crEchographie);
          expect(expectedResult).toContain(crEchographie2);
        });

        it('should accept null and undefined values', () => {
          const crEchographie: ICrEchographie = { id: 123 };
          expectedResult = service.addCrEchographieToCollectionIfMissing([], null, crEchographie, undefined);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(crEchographie);
        });

        it('should return initial array if no CrEchographie is added', () => {
          const crEchographieCollection: ICrEchographie[] = [{ id: 123 }];
          expectedResult = service.addCrEchographieToCollectionIfMissing(crEchographieCollection, undefined, null);
          expect(expectedResult).toEqual(crEchographieCollection);
        });
      });
    });

    afterEach(() => {
      httpMock.verify();
    });
  });
});

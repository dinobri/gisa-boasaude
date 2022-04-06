import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import dayjs from 'dayjs/esm';

import { DATE_TIME_FORMAT } from 'app/config/input.constants';
import { IDisponibilidadeExame, DisponibilidadeExame } from '../disponibilidade-exame.model';

import { DisponibilidadeExameService } from './disponibilidade-exame.service';

describe('DisponibilidadeExame Service', () => {
  let service: DisponibilidadeExameService;
  let httpMock: HttpTestingController;
  let elemDefault: IDisponibilidadeExame;
  let expectedResult: IDisponibilidadeExame | IDisponibilidadeExame[] | boolean | null;
  let currentDate: dayjs.Dayjs;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(DisponibilidadeExameService);
    httpMock = TestBed.inject(HttpTestingController);
    currentDate = dayjs();

    elemDefault = {
      id: 0,
      horaInicio: currentDate,
      horaFim: currentDate,
    };
  });

  describe('Service methods', () => {
    it('should find an element', () => {
      const returnedFromService = Object.assign(
        {
          horaInicio: currentDate.format(DATE_TIME_FORMAT),
          horaFim: currentDate.format(DATE_TIME_FORMAT),
        },
        elemDefault
      );

      service.find(123).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(elemDefault);
    });

    it('should create a DisponibilidadeExame', () => {
      const returnedFromService = Object.assign(
        {
          id: 0,
          horaInicio: currentDate.format(DATE_TIME_FORMAT),
          horaFim: currentDate.format(DATE_TIME_FORMAT),
        },
        elemDefault
      );

      const expected = Object.assign(
        {
          horaInicio: currentDate,
          horaFim: currentDate,
        },
        returnedFromService
      );

      service.create(new DisponibilidadeExame()).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a DisponibilidadeExame', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          horaInicio: currentDate.format(DATE_TIME_FORMAT),
          horaFim: currentDate.format(DATE_TIME_FORMAT),
        },
        elemDefault
      );

      const expected = Object.assign(
        {
          horaInicio: currentDate,
          horaFim: currentDate,
        },
        returnedFromService
      );

      service.update(expected).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a DisponibilidadeExame', () => {
      const patchObject = Object.assign(
        {
          horaInicio: currentDate.format(DATE_TIME_FORMAT),
        },
        new DisponibilidadeExame()
      );

      const returnedFromService = Object.assign(patchObject, elemDefault);

      const expected = Object.assign(
        {
          horaInicio: currentDate,
          horaFim: currentDate,
        },
        returnedFromService
      );

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of DisponibilidadeExame', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          horaInicio: currentDate.format(DATE_TIME_FORMAT),
          horaFim: currentDate.format(DATE_TIME_FORMAT),
        },
        elemDefault
      );

      const expected = Object.assign(
        {
          horaInicio: currentDate,
          horaFim: currentDate,
        },
        returnedFromService
      );

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toContainEqual(expected);
    });

    it('should delete a DisponibilidadeExame', () => {
      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult);
    });

    describe('addDisponibilidadeExameToCollectionIfMissing', () => {
      it('should add a DisponibilidadeExame to an empty array', () => {
        const disponibilidadeExame: IDisponibilidadeExame = { id: 123 };
        expectedResult = service.addDisponibilidadeExameToCollectionIfMissing([], disponibilidadeExame);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(disponibilidadeExame);
      });

      it('should not add a DisponibilidadeExame to an array that contains it', () => {
        const disponibilidadeExame: IDisponibilidadeExame = { id: 123 };
        const disponibilidadeExameCollection: IDisponibilidadeExame[] = [
          {
            ...disponibilidadeExame,
          },
          { id: 456 },
        ];
        expectedResult = service.addDisponibilidadeExameToCollectionIfMissing(disponibilidadeExameCollection, disponibilidadeExame);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a DisponibilidadeExame to an array that doesn't contain it", () => {
        const disponibilidadeExame: IDisponibilidadeExame = { id: 123 };
        const disponibilidadeExameCollection: IDisponibilidadeExame[] = [{ id: 456 }];
        expectedResult = service.addDisponibilidadeExameToCollectionIfMissing(disponibilidadeExameCollection, disponibilidadeExame);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(disponibilidadeExame);
      });

      it('should add only unique DisponibilidadeExame to an array', () => {
        const disponibilidadeExameArray: IDisponibilidadeExame[] = [{ id: 123 }, { id: 456 }, { id: 68687 }];
        const disponibilidadeExameCollection: IDisponibilidadeExame[] = [{ id: 123 }];
        expectedResult = service.addDisponibilidadeExameToCollectionIfMissing(disponibilidadeExameCollection, ...disponibilidadeExameArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const disponibilidadeExame: IDisponibilidadeExame = { id: 123 };
        const disponibilidadeExame2: IDisponibilidadeExame = { id: 456 };
        expectedResult = service.addDisponibilidadeExameToCollectionIfMissing([], disponibilidadeExame, disponibilidadeExame2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(disponibilidadeExame);
        expect(expectedResult).toContain(disponibilidadeExame2);
      });

      it('should accept null and undefined values', () => {
        const disponibilidadeExame: IDisponibilidadeExame = { id: 123 };
        expectedResult = service.addDisponibilidadeExameToCollectionIfMissing([], null, disponibilidadeExame, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(disponibilidadeExame);
      });

      it('should return initial array if no DisponibilidadeExame is added', () => {
        const disponibilidadeExameCollection: IDisponibilidadeExame[] = [{ id: 123 }];
        expectedResult = service.addDisponibilidadeExameToCollectionIfMissing(disponibilidadeExameCollection, undefined, null);
        expect(expectedResult).toEqual(disponibilidadeExameCollection);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});

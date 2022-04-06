import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import dayjs from 'dayjs/esm';

import { DATE_TIME_FORMAT } from 'app/config/input.constants';
import { IDisponibilidadeConsulta, DisponibilidadeConsulta } from '../disponibilidade-consulta.model';

import { DisponibilidadeConsultaService } from './disponibilidade-consulta.service';

describe('DisponibilidadeConsulta Service', () => {
  let service: DisponibilidadeConsultaService;
  let httpMock: HttpTestingController;
  let elemDefault: IDisponibilidadeConsulta;
  let expectedResult: IDisponibilidadeConsulta | IDisponibilidadeConsulta[] | boolean | null;
  let currentDate: dayjs.Dayjs;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(DisponibilidadeConsultaService);
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

    it('should create a DisponibilidadeConsulta', () => {
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

      service.create(new DisponibilidadeConsulta()).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a DisponibilidadeConsulta', () => {
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

    it('should partial update a DisponibilidadeConsulta', () => {
      const patchObject = Object.assign(
        {
          horaFim: currentDate.format(DATE_TIME_FORMAT),
        },
        new DisponibilidadeConsulta()
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

    it('should return a list of DisponibilidadeConsulta', () => {
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

    it('should delete a DisponibilidadeConsulta', () => {
      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult);
    });

    describe('addDisponibilidadeConsultaToCollectionIfMissing', () => {
      it('should add a DisponibilidadeConsulta to an empty array', () => {
        const disponibilidadeConsulta: IDisponibilidadeConsulta = { id: 123 };
        expectedResult = service.addDisponibilidadeConsultaToCollectionIfMissing([], disponibilidadeConsulta);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(disponibilidadeConsulta);
      });

      it('should not add a DisponibilidadeConsulta to an array that contains it', () => {
        const disponibilidadeConsulta: IDisponibilidadeConsulta = { id: 123 };
        const disponibilidadeConsultaCollection: IDisponibilidadeConsulta[] = [
          {
            ...disponibilidadeConsulta,
          },
          { id: 456 },
        ];
        expectedResult = service.addDisponibilidadeConsultaToCollectionIfMissing(
          disponibilidadeConsultaCollection,
          disponibilidadeConsulta
        );
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a DisponibilidadeConsulta to an array that doesn't contain it", () => {
        const disponibilidadeConsulta: IDisponibilidadeConsulta = { id: 123 };
        const disponibilidadeConsultaCollection: IDisponibilidadeConsulta[] = [{ id: 456 }];
        expectedResult = service.addDisponibilidadeConsultaToCollectionIfMissing(
          disponibilidadeConsultaCollection,
          disponibilidadeConsulta
        );
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(disponibilidadeConsulta);
      });

      it('should add only unique DisponibilidadeConsulta to an array', () => {
        const disponibilidadeConsultaArray: IDisponibilidadeConsulta[] = [{ id: 123 }, { id: 456 }, { id: 55934 }];
        const disponibilidadeConsultaCollection: IDisponibilidadeConsulta[] = [{ id: 123 }];
        expectedResult = service.addDisponibilidadeConsultaToCollectionIfMissing(
          disponibilidadeConsultaCollection,
          ...disponibilidadeConsultaArray
        );
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const disponibilidadeConsulta: IDisponibilidadeConsulta = { id: 123 };
        const disponibilidadeConsulta2: IDisponibilidadeConsulta = { id: 456 };
        expectedResult = service.addDisponibilidadeConsultaToCollectionIfMissing([], disponibilidadeConsulta, disponibilidadeConsulta2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(disponibilidadeConsulta);
        expect(expectedResult).toContain(disponibilidadeConsulta2);
      });

      it('should accept null and undefined values', () => {
        const disponibilidadeConsulta: IDisponibilidadeConsulta = { id: 123 };
        expectedResult = service.addDisponibilidadeConsultaToCollectionIfMissing([], null, disponibilidadeConsulta, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(disponibilidadeConsulta);
      });

      it('should return initial array if no DisponibilidadeConsulta is added', () => {
        const disponibilidadeConsultaCollection: IDisponibilidadeConsulta[] = [{ id: 123 }];
        expectedResult = service.addDisponibilidadeConsultaToCollectionIfMissing(disponibilidadeConsultaCollection, undefined, null);
        expect(expectedResult).toEqual(disponibilidadeConsultaCollection);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});

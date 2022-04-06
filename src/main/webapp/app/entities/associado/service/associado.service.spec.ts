import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import dayjs from 'dayjs/esm';

import { DATE_TIME_FORMAT } from 'app/config/input.constants';
import { EstadoCivil } from 'app/entities/enumerations/estado-civil.model';
import { Sexo } from 'app/entities/enumerations/sexo.model';
import { UF } from 'app/entities/enumerations/uf.model';
import { SituacaoAssociado } from 'app/entities/enumerations/situacao-associado.model';
import { IAssociado, Associado } from '../associado.model';

import { AssociadoService } from './associado.service';

describe('Associado Service', () => {
  let service: AssociadoService;
  let httpMock: HttpTestingController;
  let elemDefault: IAssociado;
  let expectedResult: IAssociado | IAssociado[] | boolean | null;
  let currentDate: dayjs.Dayjs;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(AssociadoService);
    httpMock = TestBed.inject(HttpTestingController);
    currentDate = dayjs();

    elemDefault = {
      id: 0,
      nome: 'AAAAAAA',
      sobrenome: 'AAAAAAA',
      dataNascimento: currentDate,
      estadoCivil: EstadoCivil.SOLTEIRO,
      sexo: Sexo.MASCULINO,
      naturalidadeUf: UF.AC,
      naturalidadeCidade: 'AAAAAAA',
      numeroDocumento: 'AAAAAAA',
      ufDocumento: UF.AC,
      orgaoDocumento: 'AAAAAAA',
      dataDocumento: currentDate,
      nomeMae: 'AAAAAAA',
      nomePai: 'AAAAAAA',
      email: 'AAAAAAA',
      telefone: 'AAAAAAA',
      sitaucao: SituacaoAssociado.ATIVO,
    };
  });

  describe('Service methods', () => {
    it('should find an element', () => {
      const returnedFromService = Object.assign(
        {
          dataNascimento: currentDate.format(DATE_TIME_FORMAT),
          dataDocumento: currentDate.format(DATE_TIME_FORMAT),
        },
        elemDefault
      );

      service.find(123).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(elemDefault);
    });

    it('should create a Associado', () => {
      const returnedFromService = Object.assign(
        {
          id: 0,
          dataNascimento: currentDate.format(DATE_TIME_FORMAT),
          dataDocumento: currentDate.format(DATE_TIME_FORMAT),
        },
        elemDefault
      );

      const expected = Object.assign(
        {
          dataNascimento: currentDate,
          dataDocumento: currentDate,
        },
        returnedFromService
      );

      service.create(new Associado()).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a Associado', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          nome: 'BBBBBB',
          sobrenome: 'BBBBBB',
          dataNascimento: currentDate.format(DATE_TIME_FORMAT),
          estadoCivil: 'BBBBBB',
          sexo: 'BBBBBB',
          naturalidadeUf: 'BBBBBB',
          naturalidadeCidade: 'BBBBBB',
          numeroDocumento: 'BBBBBB',
          ufDocumento: 'BBBBBB',
          orgaoDocumento: 'BBBBBB',
          dataDocumento: currentDate.format(DATE_TIME_FORMAT),
          nomeMae: 'BBBBBB',
          nomePai: 'BBBBBB',
          email: 'BBBBBB',
          telefone: 'BBBBBB',
          sitaucao: 'BBBBBB',
        },
        elemDefault
      );

      const expected = Object.assign(
        {
          dataNascimento: currentDate,
          dataDocumento: currentDate,
        },
        returnedFromService
      );

      service.update(expected).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a Associado', () => {
      const patchObject = Object.assign(
        {
          sobrenome: 'BBBBBB',
          dataNascimento: currentDate.format(DATE_TIME_FORMAT),
          estadoCivil: 'BBBBBB',
          sexo: 'BBBBBB',
          naturalidadeUf: 'BBBBBB',
          numeroDocumento: 'BBBBBB',
          orgaoDocumento: 'BBBBBB',
          dataDocumento: currentDate.format(DATE_TIME_FORMAT),
          email: 'BBBBBB',
        },
        new Associado()
      );

      const returnedFromService = Object.assign(patchObject, elemDefault);

      const expected = Object.assign(
        {
          dataNascimento: currentDate,
          dataDocumento: currentDate,
        },
        returnedFromService
      );

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of Associado', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          nome: 'BBBBBB',
          sobrenome: 'BBBBBB',
          dataNascimento: currentDate.format(DATE_TIME_FORMAT),
          estadoCivil: 'BBBBBB',
          sexo: 'BBBBBB',
          naturalidadeUf: 'BBBBBB',
          naturalidadeCidade: 'BBBBBB',
          numeroDocumento: 'BBBBBB',
          ufDocumento: 'BBBBBB',
          orgaoDocumento: 'BBBBBB',
          dataDocumento: currentDate.format(DATE_TIME_FORMAT),
          nomeMae: 'BBBBBB',
          nomePai: 'BBBBBB',
          email: 'BBBBBB',
          telefone: 'BBBBBB',
          sitaucao: 'BBBBBB',
        },
        elemDefault
      );

      const expected = Object.assign(
        {
          dataNascimento: currentDate,
          dataDocumento: currentDate,
        },
        returnedFromService
      );

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toContainEqual(expected);
    });

    it('should delete a Associado', () => {
      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult);
    });

    describe('addAssociadoToCollectionIfMissing', () => {
      it('should add a Associado to an empty array', () => {
        const associado: IAssociado = { id: 123 };
        expectedResult = service.addAssociadoToCollectionIfMissing([], associado);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(associado);
      });

      it('should not add a Associado to an array that contains it', () => {
        const associado: IAssociado = { id: 123 };
        const associadoCollection: IAssociado[] = [
          {
            ...associado,
          },
          { id: 456 },
        ];
        expectedResult = service.addAssociadoToCollectionIfMissing(associadoCollection, associado);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a Associado to an array that doesn't contain it", () => {
        const associado: IAssociado = { id: 123 };
        const associadoCollection: IAssociado[] = [{ id: 456 }];
        expectedResult = service.addAssociadoToCollectionIfMissing(associadoCollection, associado);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(associado);
      });

      it('should add only unique Associado to an array', () => {
        const associadoArray: IAssociado[] = [{ id: 123 }, { id: 456 }, { id: 30722 }];
        const associadoCollection: IAssociado[] = [{ id: 123 }];
        expectedResult = service.addAssociadoToCollectionIfMissing(associadoCollection, ...associadoArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const associado: IAssociado = { id: 123 };
        const associado2: IAssociado = { id: 456 };
        expectedResult = service.addAssociadoToCollectionIfMissing([], associado, associado2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(associado);
        expect(expectedResult).toContain(associado2);
      });

      it('should accept null and undefined values', () => {
        const associado: IAssociado = { id: 123 };
        expectedResult = service.addAssociadoToCollectionIfMissing([], null, associado, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(associado);
      });

      it('should return initial array if no Associado is added', () => {
        const associadoCollection: IAssociado[] = [{ id: 123 }];
        expectedResult = service.addAssociadoToCollectionIfMissing(associadoCollection, undefined, null);
        expect(expectedResult).toEqual(associadoCollection);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});

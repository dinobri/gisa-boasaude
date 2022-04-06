import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { IEspecialidade, Especialidade } from '../especialidade.model';

import { EspecialidadeService } from './especialidade.service';

describe('Especialidade Service', () => {
  let service: EspecialidadeService;
  let httpMock: HttpTestingController;
  let elemDefault: IEspecialidade;
  let expectedResult: IEspecialidade | IEspecialidade[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(EspecialidadeService);
    httpMock = TestBed.inject(HttpTestingController);

    elemDefault = {
      id: 0,
      nome: 'AAAAAAA',
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

    it('should create a Especialidade', () => {
      const returnedFromService = Object.assign(
        {
          id: 0,
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.create(new Especialidade()).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a Especialidade', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          nome: 'BBBBBB',
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.update(expected).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a Especialidade', () => {
      const patchObject = Object.assign(
        {
          nome: 'BBBBBB',
        },
        new Especialidade()
      );

      const returnedFromService = Object.assign(patchObject, elemDefault);

      const expected = Object.assign({}, returnedFromService);

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of Especialidade', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          nome: 'BBBBBB',
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

    it('should delete a Especialidade', () => {
      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult);
    });

    describe('addEspecialidadeToCollectionIfMissing', () => {
      it('should add a Especialidade to an empty array', () => {
        const especialidade: IEspecialidade = { id: 123 };
        expectedResult = service.addEspecialidadeToCollectionIfMissing([], especialidade);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(especialidade);
      });

      it('should not add a Especialidade to an array that contains it', () => {
        const especialidade: IEspecialidade = { id: 123 };
        const especialidadeCollection: IEspecialidade[] = [
          {
            ...especialidade,
          },
          { id: 456 },
        ];
        expectedResult = service.addEspecialidadeToCollectionIfMissing(especialidadeCollection, especialidade);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a Especialidade to an array that doesn't contain it", () => {
        const especialidade: IEspecialidade = { id: 123 };
        const especialidadeCollection: IEspecialidade[] = [{ id: 456 }];
        expectedResult = service.addEspecialidadeToCollectionIfMissing(especialidadeCollection, especialidade);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(especialidade);
      });

      it('should add only unique Especialidade to an array', () => {
        const especialidadeArray: IEspecialidade[] = [{ id: 123 }, { id: 456 }, { id: 88030 }];
        const especialidadeCollection: IEspecialidade[] = [{ id: 123 }];
        expectedResult = service.addEspecialidadeToCollectionIfMissing(especialidadeCollection, ...especialidadeArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const especialidade: IEspecialidade = { id: 123 };
        const especialidade2: IEspecialidade = { id: 456 };
        expectedResult = service.addEspecialidadeToCollectionIfMissing([], especialidade, especialidade2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(especialidade);
        expect(expectedResult).toContain(especialidade2);
      });

      it('should accept null and undefined values', () => {
        const especialidade: IEspecialidade = { id: 123 };
        expectedResult = service.addEspecialidadeToCollectionIfMissing([], null, especialidade, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(especialidade);
      });

      it('should return initial array if no Especialidade is added', () => {
        const especialidadeCollection: IEspecialidade[] = [{ id: 123 }];
        expectedResult = service.addEspecialidadeToCollectionIfMissing(especialidadeCollection, undefined, null);
        expect(expectedResult).toEqual(especialidadeCollection);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});

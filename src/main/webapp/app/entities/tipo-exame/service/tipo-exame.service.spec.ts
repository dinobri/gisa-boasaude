import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { ITipoExame, TipoExame } from '../tipo-exame.model';

import { TipoExameService } from './tipo-exame.service';

describe('TipoExame Service', () => {
  let service: TipoExameService;
  let httpMock: HttpTestingController;
  let elemDefault: ITipoExame;
  let expectedResult: ITipoExame | ITipoExame[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(TipoExameService);
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

    it('should create a TipoExame', () => {
      const returnedFromService = Object.assign(
        {
          id: 0,
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.create(new TipoExame()).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a TipoExame', () => {
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

    it('should partial update a TipoExame', () => {
      const patchObject = Object.assign(
        {
          nome: 'BBBBBB',
        },
        new TipoExame()
      );

      const returnedFromService = Object.assign(patchObject, elemDefault);

      const expected = Object.assign({}, returnedFromService);

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of TipoExame', () => {
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

    it('should delete a TipoExame', () => {
      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult);
    });

    describe('addTipoExameToCollectionIfMissing', () => {
      it('should add a TipoExame to an empty array', () => {
        const tipoExame: ITipoExame = { id: 123 };
        expectedResult = service.addTipoExameToCollectionIfMissing([], tipoExame);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(tipoExame);
      });

      it('should not add a TipoExame to an array that contains it', () => {
        const tipoExame: ITipoExame = { id: 123 };
        const tipoExameCollection: ITipoExame[] = [
          {
            ...tipoExame,
          },
          { id: 456 },
        ];
        expectedResult = service.addTipoExameToCollectionIfMissing(tipoExameCollection, tipoExame);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a TipoExame to an array that doesn't contain it", () => {
        const tipoExame: ITipoExame = { id: 123 };
        const tipoExameCollection: ITipoExame[] = [{ id: 456 }];
        expectedResult = service.addTipoExameToCollectionIfMissing(tipoExameCollection, tipoExame);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(tipoExame);
      });

      it('should add only unique TipoExame to an array', () => {
        const tipoExameArray: ITipoExame[] = [{ id: 123 }, { id: 456 }, { id: 37870 }];
        const tipoExameCollection: ITipoExame[] = [{ id: 123 }];
        expectedResult = service.addTipoExameToCollectionIfMissing(tipoExameCollection, ...tipoExameArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const tipoExame: ITipoExame = { id: 123 };
        const tipoExame2: ITipoExame = { id: 456 };
        expectedResult = service.addTipoExameToCollectionIfMissing([], tipoExame, tipoExame2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(tipoExame);
        expect(expectedResult).toContain(tipoExame2);
      });

      it('should accept null and undefined values', () => {
        const tipoExame: ITipoExame = { id: 123 };
        expectedResult = service.addTipoExameToCollectionIfMissing([], null, tipoExame, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(tipoExame);
      });

      it('should return initial array if no TipoExame is added', () => {
        const tipoExameCollection: ITipoExame[] = [{ id: 123 }];
        expectedResult = service.addTipoExameToCollectionIfMissing(tipoExameCollection, undefined, null);
        expect(expectedResult).toEqual(tipoExameCollection);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});

import { TestBed } from '@angular/core/testing';
import { HttpTestingController, provideHttpClientTesting } from '@angular/common/http/testing';
import { provideHttpClient } from '@angular/common/http';

import { DATE_FORMAT } from 'app/config/input.constants';
import { ICadastroAluno } from '../cadastro-aluno.model';
import { sampleWithFullData, sampleWithNewData, sampleWithPartialData, sampleWithRequiredData } from '../cadastro-aluno.test-samples';

import { CadastroAlunoService, RestCadastroAluno } from './cadastro-aluno.service';

const requireRestSample: RestCadastroAluno = {
  ...sampleWithRequiredData,
  dataCadastro: sampleWithRequiredData.dataCadastro?.format(DATE_FORMAT),
  dn: sampleWithRequiredData.dn?.format(DATE_FORMAT),
  paiDataNascimento: sampleWithRequiredData.paiDataNascimento?.format(DATE_FORMAT),
  maeDataNascimento: sampleWithRequiredData.maeDataNascimento?.format(DATE_FORMAT),
};

describe('CadastroAluno Service', () => {
  let service: CadastroAlunoService;
  let httpMock: HttpTestingController;
  let expectedResult: ICadastroAluno | ICadastroAluno[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [provideHttpClient(), provideHttpClientTesting()],
    });
    expectedResult = null;
    service = TestBed.inject(CadastroAlunoService);
    httpMock = TestBed.inject(HttpTestingController);
  });

  describe('Service methods', () => {
    it('should find an element', () => {
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.find(123).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should create a CadastroAluno', () => {
      const cadastroAluno = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(cadastroAluno).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a CadastroAluno', () => {
      const cadastroAluno = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(cadastroAluno).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a CadastroAluno', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of CadastroAluno', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a CadastroAluno', () => {
      const expected = true;

      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    it('should handle exceptions for searching a CadastroAluno', () => {
      const queryObject: any = {
        page: 0,
        size: 20,
        query: '',
        sort: [],
      };
      service.search(queryObject).subscribe(() => expectedResult);

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush(null, { status: 500, statusText: 'Internal Server Error' });
      expect(expectedResult).toBe(null);
    });

    describe('addCadastroAlunoToCollectionIfMissing', () => {
      it('should add a CadastroAluno to an empty array', () => {
        const cadastroAluno: ICadastroAluno = sampleWithRequiredData;
        expectedResult = service.addCadastroAlunoToCollectionIfMissing([], cadastroAluno);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(cadastroAluno);
      });

      it('should not add a CadastroAluno to an array that contains it', () => {
        const cadastroAluno: ICadastroAluno = sampleWithRequiredData;
        const cadastroAlunoCollection: ICadastroAluno[] = [
          {
            ...cadastroAluno,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addCadastroAlunoToCollectionIfMissing(cadastroAlunoCollection, cadastroAluno);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a CadastroAluno to an array that doesn't contain it", () => {
        const cadastroAluno: ICadastroAluno = sampleWithRequiredData;
        const cadastroAlunoCollection: ICadastroAluno[] = [sampleWithPartialData];
        expectedResult = service.addCadastroAlunoToCollectionIfMissing(cadastroAlunoCollection, cadastroAluno);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(cadastroAluno);
      });

      it('should add only unique CadastroAluno to an array', () => {
        const cadastroAlunoArray: ICadastroAluno[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const cadastroAlunoCollection: ICadastroAluno[] = [sampleWithRequiredData];
        expectedResult = service.addCadastroAlunoToCollectionIfMissing(cadastroAlunoCollection, ...cadastroAlunoArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const cadastroAluno: ICadastroAluno = sampleWithRequiredData;
        const cadastroAluno2: ICadastroAluno = sampleWithPartialData;
        expectedResult = service.addCadastroAlunoToCollectionIfMissing([], cadastroAluno, cadastroAluno2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(cadastroAluno);
        expect(expectedResult).toContain(cadastroAluno2);
      });

      it('should accept null and undefined values', () => {
        const cadastroAluno: ICadastroAluno = sampleWithRequiredData;
        expectedResult = service.addCadastroAlunoToCollectionIfMissing([], null, cadastroAluno, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(cadastroAluno);
      });

      it('should return initial array if no CadastroAluno is added', () => {
        const cadastroAlunoCollection: ICadastroAluno[] = [sampleWithRequiredData];
        expectedResult = service.addCadastroAlunoToCollectionIfMissing(cadastroAlunoCollection, undefined, null);
        expect(expectedResult).toEqual(cadastroAlunoCollection);
      });
    });

    describe('compareCadastroAluno', () => {
      it('Should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareCadastroAluno(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('Should return false if one entity is null', () => {
        const entity1 = { id: 20387 };
        const entity2 = null;

        const compareResult1 = service.compareCadastroAluno(entity1, entity2);
        const compareResult2 = service.compareCadastroAluno(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey differs', () => {
        const entity1 = { id: 20387 };
        const entity2 = { id: 2158 };

        const compareResult1 = service.compareCadastroAluno(entity1, entity2);
        const compareResult2 = service.compareCadastroAluno(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey matches', () => {
        const entity1 = { id: 20387 };
        const entity2 = { id: 20387 };

        const compareResult1 = service.compareCadastroAluno(entity1, entity2);
        const compareResult2 = service.compareCadastroAluno(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});

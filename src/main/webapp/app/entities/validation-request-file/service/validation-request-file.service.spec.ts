import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { ValidationRequestFileType } from 'app/entities/enumerations/validation-request-file-type.model';
import { IValidationRequestFile, ValidationRequestFile } from '../validation-request-file.model';

import { ValidationRequestFileService } from './validation-request-file.service';

describe('Service Tests', () => {
  describe('ValidationRequestFile Service', () => {
    let service: ValidationRequestFileService;
    let httpMock: HttpTestingController;
    let elemDefault: IValidationRequestFile;
    let expectedResult: IValidationRequestFile | IValidationRequestFile[] | boolean | null;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
      });
      expectedResult = null;
      service = TestBed.inject(ValidationRequestFileService);
      httpMock = TestBed.inject(HttpTestingController);

      elemDefault = {
        id: 0,
        name: 'AAAAAAA',
        fileContentType: 'image/png',
        file: 'AAAAAAA',
        type: ValidationRequestFileType.PASSPORT,
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

      it('should create a ValidationRequestFile', () => {
        const returnedFromService = Object.assign(
          {
            id: 0,
          },
          elemDefault
        );

        const expected = Object.assign({}, returnedFromService);

        service.create(new ValidationRequestFile()).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'POST' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should update a ValidationRequestFile', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            name: 'BBBBBB',
            file: 'BBBBBB',
            type: 'BBBBBB',
          },
          elemDefault
        );

        const expected = Object.assign({}, returnedFromService);

        service.update(expected).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PUT' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should partial update a ValidationRequestFile', () => {
        const patchObject = Object.assign(
          {
            file: 'BBBBBB',
          },
          new ValidationRequestFile()
        );

        const returnedFromService = Object.assign(patchObject, elemDefault);

        const expected = Object.assign({}, returnedFromService);

        service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PATCH' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should return a list of ValidationRequestFile', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            name: 'BBBBBB',
            file: 'BBBBBB',
            type: 'BBBBBB',
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

      it('should delete a ValidationRequestFile', () => {
        service.delete(123).subscribe(resp => (expectedResult = resp.ok));

        const req = httpMock.expectOne({ method: 'DELETE' });
        req.flush({ status: 200 });
        expect(expectedResult);
      });

      describe('addValidationRequestFileToCollectionIfMissing', () => {
        it('should add a ValidationRequestFile to an empty array', () => {
          const validationRequestFile: IValidationRequestFile = { id: 123 };
          expectedResult = service.addValidationRequestFileToCollectionIfMissing([], validationRequestFile);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(validationRequestFile);
        });

        it('should not add a ValidationRequestFile to an array that contains it', () => {
          const validationRequestFile: IValidationRequestFile = { id: 123 };
          const validationRequestFileCollection: IValidationRequestFile[] = [
            {
              ...validationRequestFile,
            },
            { id: 456 },
          ];
          expectedResult = service.addValidationRequestFileToCollectionIfMissing(validationRequestFileCollection, validationRequestFile);
          expect(expectedResult).toHaveLength(2);
        });

        it("should add a ValidationRequestFile to an array that doesn't contain it", () => {
          const validationRequestFile: IValidationRequestFile = { id: 123 };
          const validationRequestFileCollection: IValidationRequestFile[] = [{ id: 456 }];
          expectedResult = service.addValidationRequestFileToCollectionIfMissing(validationRequestFileCollection, validationRequestFile);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(validationRequestFile);
        });

        it('should add only unique ValidationRequestFile to an array', () => {
          const validationRequestFileArray: IValidationRequestFile[] = [{ id: 123 }, { id: 456 }, { id: 61059 }];
          const validationRequestFileCollection: IValidationRequestFile[] = [{ id: 123 }];
          expectedResult = service.addValidationRequestFileToCollectionIfMissing(
            validationRequestFileCollection,
            ...validationRequestFileArray
          );
          expect(expectedResult).toHaveLength(3);
        });

        it('should accept varargs', () => {
          const validationRequestFile: IValidationRequestFile = { id: 123 };
          const validationRequestFile2: IValidationRequestFile = { id: 456 };
          expectedResult = service.addValidationRequestFileToCollectionIfMissing([], validationRequestFile, validationRequestFile2);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(validationRequestFile);
          expect(expectedResult).toContain(validationRequestFile2);
        });

        it('should accept null and undefined values', () => {
          const validationRequestFile: IValidationRequestFile = { id: 123 };
          expectedResult = service.addValidationRequestFileToCollectionIfMissing([], null, validationRequestFile, undefined);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(validationRequestFile);
        });
      });
    });

    afterEach(() => {
      httpMock.verify();
    });
  });
});

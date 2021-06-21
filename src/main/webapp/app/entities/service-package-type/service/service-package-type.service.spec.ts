import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { IServicePackageType, ServicePackageType } from '../service-package-type.model';

import { ServicePackageTypeService } from './service-package-type.service';

describe('Service Tests', () => {
  describe('ServicePackageType Service', () => {
    let service: ServicePackageTypeService;
    let httpMock: HttpTestingController;
    let elemDefault: IServicePackageType;
    let expectedResult: IServicePackageType | IServicePackageType[] | boolean | null;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
      });
      expectedResult = null;
      service = TestBed.inject(ServicePackageTypeService);
      httpMock = TestBed.inject(HttpTestingController);

      elemDefault = {
        id: 0,
        name: 'AAAAAAA',
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

      it('should create a ServicePackageType', () => {
        const returnedFromService = Object.assign(
          {
            id: 0,
          },
          elemDefault
        );

        const expected = Object.assign({}, returnedFromService);

        service.create(new ServicePackageType()).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'POST' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should update a ServicePackageType', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            name: 'BBBBBB',
          },
          elemDefault
        );

        const expected = Object.assign({}, returnedFromService);

        service.update(expected).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PUT' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should partial update a ServicePackageType', () => {
        const patchObject = Object.assign({}, new ServicePackageType());

        const returnedFromService = Object.assign(patchObject, elemDefault);

        const expected = Object.assign({}, returnedFromService);

        service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PATCH' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should return a list of ServicePackageType', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            name: 'BBBBBB',
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

      it('should delete a ServicePackageType', () => {
        service.delete(123).subscribe(resp => (expectedResult = resp.ok));

        const req = httpMock.expectOne({ method: 'DELETE' });
        req.flush({ status: 200 });
        expect(expectedResult);
      });

      describe('addServicePackageTypeToCollectionIfMissing', () => {
        it('should add a ServicePackageType to an empty array', () => {
          const servicePackageType: IServicePackageType = { id: 123 };
          expectedResult = service.addServicePackageTypeToCollectionIfMissing([], servicePackageType);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(servicePackageType);
        });

        it('should not add a ServicePackageType to an array that contains it', () => {
          const servicePackageType: IServicePackageType = { id: 123 };
          const servicePackageTypeCollection: IServicePackageType[] = [
            {
              ...servicePackageType,
            },
            { id: 456 },
          ];
          expectedResult = service.addServicePackageTypeToCollectionIfMissing(servicePackageTypeCollection, servicePackageType);
          expect(expectedResult).toHaveLength(2);
        });

        it("should add a ServicePackageType to an array that doesn't contain it", () => {
          const servicePackageType: IServicePackageType = { id: 123 };
          const servicePackageTypeCollection: IServicePackageType[] = [{ id: 456 }];
          expectedResult = service.addServicePackageTypeToCollectionIfMissing(servicePackageTypeCollection, servicePackageType);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(servicePackageType);
        });

        it('should add only unique ServicePackageType to an array', () => {
          const servicePackageTypeArray: IServicePackageType[] = [{ id: 123 }, { id: 456 }, { id: 38609 }];
          const servicePackageTypeCollection: IServicePackageType[] = [{ id: 123 }];
          expectedResult = service.addServicePackageTypeToCollectionIfMissing(servicePackageTypeCollection, ...servicePackageTypeArray);
          expect(expectedResult).toHaveLength(3);
        });

        it('should accept varargs', () => {
          const servicePackageType: IServicePackageType = { id: 123 };
          const servicePackageType2: IServicePackageType = { id: 456 };
          expectedResult = service.addServicePackageTypeToCollectionIfMissing([], servicePackageType, servicePackageType2);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(servicePackageType);
          expect(expectedResult).toContain(servicePackageType2);
        });

        it('should accept null and undefined values', () => {
          const servicePackageType: IServicePackageType = { id: 123 };
          expectedResult = service.addServicePackageTypeToCollectionIfMissing([], null, servicePackageType, undefined);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(servicePackageType);
        });
      });
    });

    afterEach(() => {
      httpMock.verify();
    });
  });
});

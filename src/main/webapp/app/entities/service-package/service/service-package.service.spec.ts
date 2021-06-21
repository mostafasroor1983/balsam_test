import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { IServicePackage, ServicePackage } from '../service-package.model';

import { ServicePackageService } from './service-package.service';

describe('Service Tests', () => {
  describe('ServicePackage Service', () => {
    let service: ServicePackageService;
    let httpMock: HttpTestingController;
    let elemDefault: IServicePackage;
    let expectedResult: IServicePackage | IServicePackage[] | boolean | null;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
      });
      expectedResult = null;
      service = TestBed.inject(ServicePackageService);
      httpMock = TestBed.inject(HttpTestingController);

      elemDefault = {
        id: 0,
        name: 'AAAAAAA',
        recommended: false,
        tagName: 'AAAAAAA',
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

      it('should create a ServicePackage', () => {
        const returnedFromService = Object.assign(
          {
            id: 0,
          },
          elemDefault
        );

        const expected = Object.assign({}, returnedFromService);

        service.create(new ServicePackage()).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'POST' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should update a ServicePackage', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            name: 'BBBBBB',
            recommended: true,
            tagName: 'BBBBBB',
          },
          elemDefault
        );

        const expected = Object.assign({}, returnedFromService);

        service.update(expected).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PUT' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should partial update a ServicePackage', () => {
        const patchObject = Object.assign(
          {
            name: 'BBBBBB',
            recommended: true,
            tagName: 'BBBBBB',
          },
          new ServicePackage()
        );

        const returnedFromService = Object.assign(patchObject, elemDefault);

        const expected = Object.assign({}, returnedFromService);

        service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PATCH' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should return a list of ServicePackage', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            name: 'BBBBBB',
            recommended: true,
            tagName: 'BBBBBB',
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

      it('should delete a ServicePackage', () => {
        service.delete(123).subscribe(resp => (expectedResult = resp.ok));

        const req = httpMock.expectOne({ method: 'DELETE' });
        req.flush({ status: 200 });
        expect(expectedResult);
      });

      describe('addServicePackageToCollectionIfMissing', () => {
        it('should add a ServicePackage to an empty array', () => {
          const servicePackage: IServicePackage = { id: 123 };
          expectedResult = service.addServicePackageToCollectionIfMissing([], servicePackage);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(servicePackage);
        });

        it('should not add a ServicePackage to an array that contains it', () => {
          const servicePackage: IServicePackage = { id: 123 };
          const servicePackageCollection: IServicePackage[] = [
            {
              ...servicePackage,
            },
            { id: 456 },
          ];
          expectedResult = service.addServicePackageToCollectionIfMissing(servicePackageCollection, servicePackage);
          expect(expectedResult).toHaveLength(2);
        });

        it("should add a ServicePackage to an array that doesn't contain it", () => {
          const servicePackage: IServicePackage = { id: 123 };
          const servicePackageCollection: IServicePackage[] = [{ id: 456 }];
          expectedResult = service.addServicePackageToCollectionIfMissing(servicePackageCollection, servicePackage);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(servicePackage);
        });

        it('should add only unique ServicePackage to an array', () => {
          const servicePackageArray: IServicePackage[] = [{ id: 123 }, { id: 456 }, { id: 40984 }];
          const servicePackageCollection: IServicePackage[] = [{ id: 123 }];
          expectedResult = service.addServicePackageToCollectionIfMissing(servicePackageCollection, ...servicePackageArray);
          expect(expectedResult).toHaveLength(3);
        });

        it('should accept varargs', () => {
          const servicePackage: IServicePackage = { id: 123 };
          const servicePackage2: IServicePackage = { id: 456 };
          expectedResult = service.addServicePackageToCollectionIfMissing([], servicePackage, servicePackage2);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(servicePackage);
          expect(expectedResult).toContain(servicePackage2);
        });

        it('should accept null and undefined values', () => {
          const servicePackage: IServicePackage = { id: 123 };
          expectedResult = service.addServicePackageToCollectionIfMissing([], null, servicePackage, undefined);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(servicePackage);
        });
      });
    });

    afterEach(() => {
      httpMock.verify();
    });
  });
});

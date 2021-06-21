import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { EmployeeSize } from 'app/entities/enumerations/employee-size.model';
import { ClientSize } from 'app/entities/enumerations/client-size.model';
import { ICorporate, Corporate } from '../corporate.model';

import { CorporateService } from './corporate.service';

describe('Service Tests', () => {
  describe('Corporate Service', () => {
    let service: CorporateService;
    let httpMock: HttpTestingController;
    let elemDefault: ICorporate;
    let expectedResult: ICorporate | ICorporate[] | boolean | null;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
      });
      expectedResult = null;
      service = TestBed.inject(CorporateService);
      httpMock = TestBed.inject(HttpTestingController);

      elemDefault = {
        id: 0,
        code: 'AAAAAAA',
        name: 'AAAAAAA',
        description: 'AAAAAAA',
        logoContentType: 'image/png',
        logo: 'AAAAAAA',
        contactPerson: 'AAAAAAA',
        employeeSize: EmployeeSize.LESSTHAN10,
        clientSize: ClientSize.LESSTHAN1000,
        email: 'AAAAAAA',
        website: 'AAAAAAA',
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

      it('should create a Corporate', () => {
        const returnedFromService = Object.assign(
          {
            id: 0,
          },
          elemDefault
        );

        const expected = Object.assign({}, returnedFromService);

        service.create(new Corporate()).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'POST' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should update a Corporate', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            code: 'BBBBBB',
            name: 'BBBBBB',
            description: 'BBBBBB',
            logo: 'BBBBBB',
            contactPerson: 'BBBBBB',
            employeeSize: 'BBBBBB',
            clientSize: 'BBBBBB',
            email: 'BBBBBB',
            website: 'BBBBBB',
          },
          elemDefault
        );

        const expected = Object.assign({}, returnedFromService);

        service.update(expected).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PUT' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should partial update a Corporate', () => {
        const patchObject = Object.assign(
          {
            code: 'BBBBBB',
            logo: 'BBBBBB',
            employeeSize: 'BBBBBB',
          },
          new Corporate()
        );

        const returnedFromService = Object.assign(patchObject, elemDefault);

        const expected = Object.assign({}, returnedFromService);

        service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PATCH' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should return a list of Corporate', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            code: 'BBBBBB',
            name: 'BBBBBB',
            description: 'BBBBBB',
            logo: 'BBBBBB',
            contactPerson: 'BBBBBB',
            employeeSize: 'BBBBBB',
            clientSize: 'BBBBBB',
            email: 'BBBBBB',
            website: 'BBBBBB',
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

      it('should delete a Corporate', () => {
        service.delete(123).subscribe(resp => (expectedResult = resp.ok));

        const req = httpMock.expectOne({ method: 'DELETE' });
        req.flush({ status: 200 });
        expect(expectedResult);
      });

      describe('addCorporateToCollectionIfMissing', () => {
        it('should add a Corporate to an empty array', () => {
          const corporate: ICorporate = { id: 123 };
          expectedResult = service.addCorporateToCollectionIfMissing([], corporate);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(corporate);
        });

        it('should not add a Corporate to an array that contains it', () => {
          const corporate: ICorporate = { id: 123 };
          const corporateCollection: ICorporate[] = [
            {
              ...corporate,
            },
            { id: 456 },
          ];
          expectedResult = service.addCorporateToCollectionIfMissing(corporateCollection, corporate);
          expect(expectedResult).toHaveLength(2);
        });

        it("should add a Corporate to an array that doesn't contain it", () => {
          const corporate: ICorporate = { id: 123 };
          const corporateCollection: ICorporate[] = [{ id: 456 }];
          expectedResult = service.addCorporateToCollectionIfMissing(corporateCollection, corporate);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(corporate);
        });

        it('should add only unique Corporate to an array', () => {
          const corporateArray: ICorporate[] = [{ id: 123 }, { id: 456 }, { id: 48927 }];
          const corporateCollection: ICorporate[] = [{ id: 123 }];
          expectedResult = service.addCorporateToCollectionIfMissing(corporateCollection, ...corporateArray);
          expect(expectedResult).toHaveLength(3);
        });

        it('should accept varargs', () => {
          const corporate: ICorporate = { id: 123 };
          const corporate2: ICorporate = { id: 456 };
          expectedResult = service.addCorporateToCollectionIfMissing([], corporate, corporate2);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(corporate);
          expect(expectedResult).toContain(corporate2);
        });

        it('should accept null and undefined values', () => {
          const corporate: ICorporate = { id: 123 };
          expectedResult = service.addCorporateToCollectionIfMissing([], null, corporate, undefined);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(corporate);
        });
      });
    });

    afterEach(() => {
      httpMock.verify();
    });
  });
});

import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import * as dayjs from 'dayjs';

import { DATE_TIME_FORMAT } from 'app/config/input.constants';
import { ValidationRequestStatus } from 'app/entities/enumerations/validation-request-status.model';
import { IValidationRequest, ValidationRequest } from '../validation-request.model';

import { ValidationRequestService } from './validation-request.service';

describe('Service Tests', () => {
  describe('ValidationRequest Service', () => {
    let service: ValidationRequestService;
    let httpMock: HttpTestingController;
    let elemDefault: IValidationRequest;
    let expectedResult: IValidationRequest | IValidationRequest[] | boolean | null;
    let currentDate: dayjs.Dayjs;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
      });
      expectedResult = null;
      service = TestBed.inject(ValidationRequestService);
      httpMock = TestBed.inject(HttpTestingController);
      currentDate = dayjs();

      elemDefault = {
        id: 0,
        status: ValidationRequestStatus.APPROVED,
        actionDateTime: currentDate,
        reason: 'AAAAAAA',
        notes: 'AAAAAAA',
      };
    });

    describe('Service methods', () => {
      it('should find an element', () => {
        const returnedFromService = Object.assign(
          {
            actionDateTime: currentDate.format(DATE_TIME_FORMAT),
          },
          elemDefault
        );

        service.find(123).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'GET' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(elemDefault);
      });

      it('should create a ValidationRequest', () => {
        const returnedFromService = Object.assign(
          {
            id: 0,
            actionDateTime: currentDate.format(DATE_TIME_FORMAT),
          },
          elemDefault
        );

        const expected = Object.assign(
          {
            actionDateTime: currentDate,
          },
          returnedFromService
        );

        service.create(new ValidationRequest()).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'POST' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should update a ValidationRequest', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            status: 'BBBBBB',
            actionDateTime: currentDate.format(DATE_TIME_FORMAT),
            reason: 'BBBBBB',
            notes: 'BBBBBB',
          },
          elemDefault
        );

        const expected = Object.assign(
          {
            actionDateTime: currentDate,
          },
          returnedFromService
        );

        service.update(expected).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PUT' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should partial update a ValidationRequest', () => {
        const patchObject = Object.assign(
          {
            status: 'BBBBBB',
            reason: 'BBBBBB',
          },
          new ValidationRequest()
        );

        const returnedFromService = Object.assign(patchObject, elemDefault);

        const expected = Object.assign(
          {
            actionDateTime: currentDate,
          },
          returnedFromService
        );

        service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PATCH' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should return a list of ValidationRequest', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            status: 'BBBBBB',
            actionDateTime: currentDate.format(DATE_TIME_FORMAT),
            reason: 'BBBBBB',
            notes: 'BBBBBB',
          },
          elemDefault
        );

        const expected = Object.assign(
          {
            actionDateTime: currentDate,
          },
          returnedFromService
        );

        service.query().subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'GET' });
        req.flush([returnedFromService]);
        httpMock.verify();
        expect(expectedResult).toContainEqual(expected);
      });

      it('should delete a ValidationRequest', () => {
        service.delete(123).subscribe(resp => (expectedResult = resp.ok));

        const req = httpMock.expectOne({ method: 'DELETE' });
        req.flush({ status: 200 });
        expect(expectedResult);
      });

      describe('addValidationRequestToCollectionIfMissing', () => {
        it('should add a ValidationRequest to an empty array', () => {
          const validationRequest: IValidationRequest = { id: 123 };
          expectedResult = service.addValidationRequestToCollectionIfMissing([], validationRequest);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(validationRequest);
        });

        it('should not add a ValidationRequest to an array that contains it', () => {
          const validationRequest: IValidationRequest = { id: 123 };
          const validationRequestCollection: IValidationRequest[] = [
            {
              ...validationRequest,
            },
            { id: 456 },
          ];
          expectedResult = service.addValidationRequestToCollectionIfMissing(validationRequestCollection, validationRequest);
          expect(expectedResult).toHaveLength(2);
        });

        it("should add a ValidationRequest to an array that doesn't contain it", () => {
          const validationRequest: IValidationRequest = { id: 123 };
          const validationRequestCollection: IValidationRequest[] = [{ id: 456 }];
          expectedResult = service.addValidationRequestToCollectionIfMissing(validationRequestCollection, validationRequest);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(validationRequest);
        });

        it('should add only unique ValidationRequest to an array', () => {
          const validationRequestArray: IValidationRequest[] = [{ id: 123 }, { id: 456 }, { id: 83021 }];
          const validationRequestCollection: IValidationRequest[] = [{ id: 123 }];
          expectedResult = service.addValidationRequestToCollectionIfMissing(validationRequestCollection, ...validationRequestArray);
          expect(expectedResult).toHaveLength(3);
        });

        it('should accept varargs', () => {
          const validationRequest: IValidationRequest = { id: 123 };
          const validationRequest2: IValidationRequest = { id: 456 };
          expectedResult = service.addValidationRequestToCollectionIfMissing([], validationRequest, validationRequest2);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(validationRequest);
          expect(expectedResult).toContain(validationRequest2);
        });

        it('should accept null and undefined values', () => {
          const validationRequest: IValidationRequest = { id: 123 };
          expectedResult = service.addValidationRequestToCollectionIfMissing([], null, validationRequest, undefined);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(validationRequest);
        });
      });
    });

    afterEach(() => {
      httpMock.verify();
    });
  });
});

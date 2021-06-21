jest.mock('@angular/router');

import { TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of } from 'rxjs';

import { IValidationRequest, ValidationRequest } from '../validation-request.model';
import { ValidationRequestService } from '../service/validation-request.service';

import { ValidationRequestRoutingResolveService } from './validation-request-routing-resolve.service';

describe('Service Tests', () => {
  describe('ValidationRequest routing resolve service', () => {
    let mockRouter: Router;
    let mockActivatedRouteSnapshot: ActivatedRouteSnapshot;
    let routingResolveService: ValidationRequestRoutingResolveService;
    let service: ValidationRequestService;
    let resultValidationRequest: IValidationRequest | undefined;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        providers: [Router, ActivatedRouteSnapshot],
      });
      mockRouter = TestBed.inject(Router);
      mockActivatedRouteSnapshot = TestBed.inject(ActivatedRouteSnapshot);
      routingResolveService = TestBed.inject(ValidationRequestRoutingResolveService);
      service = TestBed.inject(ValidationRequestService);
      resultValidationRequest = undefined;
    });

    describe('resolve', () => {
      it('should return IValidationRequest returned by find', () => {
        // GIVEN
        service.find = jest.fn(id => of(new HttpResponse({ body: { id } })));
        mockActivatedRouteSnapshot.params = { id: 123 };

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultValidationRequest = result;
        });

        // THEN
        expect(service.find).toBeCalledWith(123);
        expect(resultValidationRequest).toEqual({ id: 123 });
      });

      it('should return new IValidationRequest if id is not provided', () => {
        // GIVEN
        service.find = jest.fn();
        mockActivatedRouteSnapshot.params = {};

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultValidationRequest = result;
        });

        // THEN
        expect(service.find).not.toBeCalled();
        expect(resultValidationRequest).toEqual(new ValidationRequest());
      });

      it('should route to 404 page if data not found in server', () => {
        // GIVEN
        spyOn(service, 'find').and.returnValue(of(new HttpResponse({ body: null })));
        mockActivatedRouteSnapshot.params = { id: 123 };

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultValidationRequest = result;
        });

        // THEN
        expect(service.find).toBeCalledWith(123);
        expect(resultValidationRequest).toEqual(undefined);
        expect(mockRouter.navigate).toHaveBeenCalledWith(['404']);
      });
    });
  });
});

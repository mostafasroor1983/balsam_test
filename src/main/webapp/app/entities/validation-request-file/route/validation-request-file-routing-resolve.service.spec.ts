jest.mock('@angular/router');

import { TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of } from 'rxjs';

import { IValidationRequestFile, ValidationRequestFile } from '../validation-request-file.model';
import { ValidationRequestFileService } from '../service/validation-request-file.service';

import { ValidationRequestFileRoutingResolveService } from './validation-request-file-routing-resolve.service';

describe('Service Tests', () => {
  describe('ValidationRequestFile routing resolve service', () => {
    let mockRouter: Router;
    let mockActivatedRouteSnapshot: ActivatedRouteSnapshot;
    let routingResolveService: ValidationRequestFileRoutingResolveService;
    let service: ValidationRequestFileService;
    let resultValidationRequestFile: IValidationRequestFile | undefined;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        providers: [Router, ActivatedRouteSnapshot],
      });
      mockRouter = TestBed.inject(Router);
      mockActivatedRouteSnapshot = TestBed.inject(ActivatedRouteSnapshot);
      routingResolveService = TestBed.inject(ValidationRequestFileRoutingResolveService);
      service = TestBed.inject(ValidationRequestFileService);
      resultValidationRequestFile = undefined;
    });

    describe('resolve', () => {
      it('should return IValidationRequestFile returned by find', () => {
        // GIVEN
        service.find = jest.fn(id => of(new HttpResponse({ body: { id } })));
        mockActivatedRouteSnapshot.params = { id: 123 };

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultValidationRequestFile = result;
        });

        // THEN
        expect(service.find).toBeCalledWith(123);
        expect(resultValidationRequestFile).toEqual({ id: 123 });
      });

      it('should return new IValidationRequestFile if id is not provided', () => {
        // GIVEN
        service.find = jest.fn();
        mockActivatedRouteSnapshot.params = {};

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultValidationRequestFile = result;
        });

        // THEN
        expect(service.find).not.toBeCalled();
        expect(resultValidationRequestFile).toEqual(new ValidationRequestFile());
      });

      it('should route to 404 page if data not found in server', () => {
        // GIVEN
        spyOn(service, 'find').and.returnValue(of(new HttpResponse({ body: null })));
        mockActivatedRouteSnapshot.params = { id: 123 };

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultValidationRequestFile = result;
        });

        // THEN
        expect(service.find).toBeCalledWith(123);
        expect(resultValidationRequestFile).toEqual(undefined);
        expect(mockRouter.navigate).toHaveBeenCalledWith(['404']);
      });
    });
  });
});

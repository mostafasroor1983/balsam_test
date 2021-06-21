jest.mock('@angular/router');

import { TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of } from 'rxjs';

import { IServicePackageType, ServicePackageType } from '../service-package-type.model';
import { ServicePackageTypeService } from '../service/service-package-type.service';

import { ServicePackageTypeRoutingResolveService } from './service-package-type-routing-resolve.service';

describe('Service Tests', () => {
  describe('ServicePackageType routing resolve service', () => {
    let mockRouter: Router;
    let mockActivatedRouteSnapshot: ActivatedRouteSnapshot;
    let routingResolveService: ServicePackageTypeRoutingResolveService;
    let service: ServicePackageTypeService;
    let resultServicePackageType: IServicePackageType | undefined;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        providers: [Router, ActivatedRouteSnapshot],
      });
      mockRouter = TestBed.inject(Router);
      mockActivatedRouteSnapshot = TestBed.inject(ActivatedRouteSnapshot);
      routingResolveService = TestBed.inject(ServicePackageTypeRoutingResolveService);
      service = TestBed.inject(ServicePackageTypeService);
      resultServicePackageType = undefined;
    });

    describe('resolve', () => {
      it('should return IServicePackageType returned by find', () => {
        // GIVEN
        service.find = jest.fn(id => of(new HttpResponse({ body: { id } })));
        mockActivatedRouteSnapshot.params = { id: 123 };

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultServicePackageType = result;
        });

        // THEN
        expect(service.find).toBeCalledWith(123);
        expect(resultServicePackageType).toEqual({ id: 123 });
      });

      it('should return new IServicePackageType if id is not provided', () => {
        // GIVEN
        service.find = jest.fn();
        mockActivatedRouteSnapshot.params = {};

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultServicePackageType = result;
        });

        // THEN
        expect(service.find).not.toBeCalled();
        expect(resultServicePackageType).toEqual(new ServicePackageType());
      });

      it('should route to 404 page if data not found in server', () => {
        // GIVEN
        spyOn(service, 'find').and.returnValue(of(new HttpResponse({ body: null })));
        mockActivatedRouteSnapshot.params = { id: 123 };

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultServicePackageType = result;
        });

        // THEN
        expect(service.find).toBeCalledWith(123);
        expect(resultServicePackageType).toEqual(undefined);
        expect(mockRouter.navigate).toHaveBeenCalledWith(['404']);
      });
    });
  });
});

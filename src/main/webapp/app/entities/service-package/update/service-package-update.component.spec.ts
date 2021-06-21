jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { ServicePackageService } from '../service/service-package.service';
import { IServicePackage, ServicePackage } from '../service-package.model';
import { ICountry } from 'app/entities/country/country.model';
import { CountryService } from 'app/entities/country/service/country.service';
import { IServicePackageType } from 'app/entities/service-package-type/service-package-type.model';
import { ServicePackageTypeService } from 'app/entities/service-package-type/service/service-package-type.service';

import { ServicePackageUpdateComponent } from './service-package-update.component';

describe('Component Tests', () => {
  describe('ServicePackage Management Update Component', () => {
    let comp: ServicePackageUpdateComponent;
    let fixture: ComponentFixture<ServicePackageUpdateComponent>;
    let activatedRoute: ActivatedRoute;
    let servicePackageService: ServicePackageService;
    let countryService: CountryService;
    let servicePackageTypeService: ServicePackageTypeService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [ServicePackageUpdateComponent],
        providers: [FormBuilder, ActivatedRoute],
      })
        .overrideTemplate(ServicePackageUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(ServicePackageUpdateComponent);
      activatedRoute = TestBed.inject(ActivatedRoute);
      servicePackageService = TestBed.inject(ServicePackageService);
      countryService = TestBed.inject(CountryService);
      servicePackageTypeService = TestBed.inject(ServicePackageTypeService);

      comp = fixture.componentInstance;
    });

    describe('ngOnInit', () => {
      it('Should call country query and add missing value', () => {
        const servicePackage: IServicePackage = { id: 456 };
        const country: ICountry = { id: 92718 };
        servicePackage.country = country;

        const countryCollection: ICountry[] = [{ id: 45499 }];
        spyOn(countryService, 'query').and.returnValue(of(new HttpResponse({ body: countryCollection })));
        const expectedCollection: ICountry[] = [country, ...countryCollection];
        spyOn(countryService, 'addCountryToCollectionIfMissing').and.returnValue(expectedCollection);

        activatedRoute.data = of({ servicePackage });
        comp.ngOnInit();

        expect(countryService.query).toHaveBeenCalled();
        expect(countryService.addCountryToCollectionIfMissing).toHaveBeenCalledWith(countryCollection, country);
        expect(comp.countriesCollection).toEqual(expectedCollection);
      });

      it('Should call ServicePackageType query and add missing value', () => {
        const servicePackage: IServicePackage = { id: 456 };
        const packageType: IServicePackageType = { id: 18960 };
        servicePackage.packageType = packageType;

        const servicePackageTypeCollection: IServicePackageType[] = [{ id: 72482 }];
        spyOn(servicePackageTypeService, 'query').and.returnValue(of(new HttpResponse({ body: servicePackageTypeCollection })));
        const additionalServicePackageTypes = [packageType];
        const expectedCollection: IServicePackageType[] = [...additionalServicePackageTypes, ...servicePackageTypeCollection];
        spyOn(servicePackageTypeService, 'addServicePackageTypeToCollectionIfMissing').and.returnValue(expectedCollection);

        activatedRoute.data = of({ servicePackage });
        comp.ngOnInit();

        expect(servicePackageTypeService.query).toHaveBeenCalled();
        expect(servicePackageTypeService.addServicePackageTypeToCollectionIfMissing).toHaveBeenCalledWith(
          servicePackageTypeCollection,
          ...additionalServicePackageTypes
        );
        expect(comp.servicePackageTypesSharedCollection).toEqual(expectedCollection);
      });

      it('Should update editForm', () => {
        const servicePackage: IServicePackage = { id: 456 };
        const country: ICountry = { id: 33561 };
        servicePackage.country = country;
        const packageType: IServicePackageType = { id: 14796 };
        servicePackage.packageType = packageType;

        activatedRoute.data = of({ servicePackage });
        comp.ngOnInit();

        expect(comp.editForm.value).toEqual(expect.objectContaining(servicePackage));
        expect(comp.countriesCollection).toContain(country);
        expect(comp.servicePackageTypesSharedCollection).toContain(packageType);
      });
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', () => {
        // GIVEN
        const saveSubject = new Subject();
        const servicePackage = { id: 123 };
        spyOn(servicePackageService, 'update').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ servicePackage });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: servicePackage }));
        saveSubject.complete();

        // THEN
        expect(comp.previousState).toHaveBeenCalled();
        expect(servicePackageService.update).toHaveBeenCalledWith(servicePackage);
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', () => {
        // GIVEN
        const saveSubject = new Subject();
        const servicePackage = new ServicePackage();
        spyOn(servicePackageService, 'create').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ servicePackage });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: servicePackage }));
        saveSubject.complete();

        // THEN
        expect(servicePackageService.create).toHaveBeenCalledWith(servicePackage);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).toHaveBeenCalled();
      });

      it('Should set isSaving to false on error', () => {
        // GIVEN
        const saveSubject = new Subject();
        const servicePackage = { id: 123 };
        spyOn(servicePackageService, 'update').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ servicePackage });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.error('This is an error!');

        // THEN
        expect(servicePackageService.update).toHaveBeenCalledWith(servicePackage);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).not.toHaveBeenCalled();
      });
    });

    describe('Tracking relationships identifiers', () => {
      describe('trackCountryById', () => {
        it('Should return tracked Country primary key', () => {
          const entity = { id: 123 };
          const trackResult = comp.trackCountryById(0, entity);
          expect(trackResult).toEqual(entity.id);
        });
      });

      describe('trackServicePackageTypeById', () => {
        it('Should return tracked ServicePackageType primary key', () => {
          const entity = { id: 123 };
          const trackResult = comp.trackServicePackageTypeById(0, entity);
          expect(trackResult).toEqual(entity.id);
        });
      });
    });
  });
});

jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { ServicePackageTypeService } from '../service/service-package-type.service';
import { IServicePackageType, ServicePackageType } from '../service-package-type.model';

import { ServicePackageTypeUpdateComponent } from './service-package-type-update.component';

describe('Component Tests', () => {
  describe('ServicePackageType Management Update Component', () => {
    let comp: ServicePackageTypeUpdateComponent;
    let fixture: ComponentFixture<ServicePackageTypeUpdateComponent>;
    let activatedRoute: ActivatedRoute;
    let servicePackageTypeService: ServicePackageTypeService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [ServicePackageTypeUpdateComponent],
        providers: [FormBuilder, ActivatedRoute],
      })
        .overrideTemplate(ServicePackageTypeUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(ServicePackageTypeUpdateComponent);
      activatedRoute = TestBed.inject(ActivatedRoute);
      servicePackageTypeService = TestBed.inject(ServicePackageTypeService);

      comp = fixture.componentInstance;
    });

    describe('ngOnInit', () => {
      it('Should update editForm', () => {
        const servicePackageType: IServicePackageType = { id: 456 };

        activatedRoute.data = of({ servicePackageType });
        comp.ngOnInit();

        expect(comp.editForm.value).toEqual(expect.objectContaining(servicePackageType));
      });
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', () => {
        // GIVEN
        const saveSubject = new Subject();
        const servicePackageType = { id: 123 };
        spyOn(servicePackageTypeService, 'update').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ servicePackageType });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: servicePackageType }));
        saveSubject.complete();

        // THEN
        expect(comp.previousState).toHaveBeenCalled();
        expect(servicePackageTypeService.update).toHaveBeenCalledWith(servicePackageType);
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', () => {
        // GIVEN
        const saveSubject = new Subject();
        const servicePackageType = new ServicePackageType();
        spyOn(servicePackageTypeService, 'create').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ servicePackageType });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: servicePackageType }));
        saveSubject.complete();

        // THEN
        expect(servicePackageTypeService.create).toHaveBeenCalledWith(servicePackageType);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).toHaveBeenCalled();
      });

      it('Should set isSaving to false on error', () => {
        // GIVEN
        const saveSubject = new Subject();
        const servicePackageType = { id: 123 };
        spyOn(servicePackageTypeService, 'update').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ servicePackageType });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.error('This is an error!');

        // THEN
        expect(servicePackageTypeService.update).toHaveBeenCalledWith(servicePackageType);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).not.toHaveBeenCalled();
      });
    });
  });
});

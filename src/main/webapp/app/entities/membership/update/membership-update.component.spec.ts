jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { MembershipService } from '../service/membership.service';
import { IMembership, Membership } from '../membership.model';
import { IServicePackage } from 'app/entities/service-package/service-package.model';
import { ServicePackageService } from 'app/entities/service-package/service/service-package.service';
import { ICorporate } from 'app/entities/corporate/corporate.model';
import { CorporateService } from 'app/entities/corporate/service/corporate.service';

import { IUser } from 'app/entities/user/user.model';
import { UserService } from 'app/entities/user/user.service';

import { MembershipUpdateComponent } from './membership-update.component';

describe('Component Tests', () => {
  describe('Membership Management Update Component', () => {
    let comp: MembershipUpdateComponent;
    let fixture: ComponentFixture<MembershipUpdateComponent>;
    let activatedRoute: ActivatedRoute;
    let membershipService: MembershipService;
    let servicePackageService: ServicePackageService;
    let corporateService: CorporateService;
    let userService: UserService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [MembershipUpdateComponent],
        providers: [FormBuilder, ActivatedRoute],
      })
        .overrideTemplate(MembershipUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(MembershipUpdateComponent);
      activatedRoute = TestBed.inject(ActivatedRoute);
      membershipService = TestBed.inject(MembershipService);
      servicePackageService = TestBed.inject(ServicePackageService);
      corporateService = TestBed.inject(CorporateService);
      userService = TestBed.inject(UserService);

      comp = fixture.componentInstance;
    });

    describe('ngOnInit', () => {
      it('Should call servicePackage query and add missing value', () => {
        const membership: IMembership = { id: 456 };
        const servicePackage: IServicePackage = { id: 7281 };
        membership.servicePackage = servicePackage;

        const servicePackageCollection: IServicePackage[] = [{ id: 40636 }];
        spyOn(servicePackageService, 'query').and.returnValue(of(new HttpResponse({ body: servicePackageCollection })));
        const expectedCollection: IServicePackage[] = [servicePackage, ...servicePackageCollection];
        spyOn(servicePackageService, 'addServicePackageToCollectionIfMissing').and.returnValue(expectedCollection);

        activatedRoute.data = of({ membership });
        comp.ngOnInit();

        expect(servicePackageService.query).toHaveBeenCalled();
        expect(servicePackageService.addServicePackageToCollectionIfMissing).toHaveBeenCalledWith(servicePackageCollection, servicePackage);
        expect(comp.servicePackagesCollection).toEqual(expectedCollection);
      });

      it('Should call corporate query and add missing value', () => {
        const membership: IMembership = { id: 456 };
        const corporate: ICorporate = { id: 58842 };
        membership.corporate = corporate;

        const corporateCollection: ICorporate[] = [{ id: 4579 }];
        spyOn(corporateService, 'query').and.returnValue(of(new HttpResponse({ body: corporateCollection })));
        const expectedCollection: ICorporate[] = [corporate, ...corporateCollection];
        spyOn(corporateService, 'addCorporateToCollectionIfMissing').and.returnValue(expectedCollection);

        activatedRoute.data = of({ membership });
        comp.ngOnInit();

        expect(corporateService.query).toHaveBeenCalled();
        expect(corporateService.addCorporateToCollectionIfMissing).toHaveBeenCalledWith(corporateCollection, corporate);
        expect(comp.corporatesCollection).toEqual(expectedCollection);
      });

      it('Should call User query and add missing value', () => {
        const membership: IMembership = { id: 456 };
        const user: IUser = { id: 27699 };
        membership.user = user;

        const userCollection: IUser[] = [{ id: 87926 }];
        spyOn(userService, 'query').and.returnValue(of(new HttpResponse({ body: userCollection })));
        const additionalUsers = [user];
        const expectedCollection: IUser[] = [...additionalUsers, ...userCollection];
        spyOn(userService, 'addUserToCollectionIfMissing').and.returnValue(expectedCollection);

        activatedRoute.data = of({ membership });
        comp.ngOnInit();

        expect(userService.query).toHaveBeenCalled();
        expect(userService.addUserToCollectionIfMissing).toHaveBeenCalledWith(userCollection, ...additionalUsers);
        expect(comp.usersSharedCollection).toEqual(expectedCollection);
      });

      it('Should update editForm', () => {
        const membership: IMembership = { id: 456 };
        const servicePackage: IServicePackage = { id: 76101 };
        membership.servicePackage = servicePackage;
        const corporate: ICorporate = { id: 57010 };
        membership.corporate = corporate;
        const user: IUser = { id: 47918 };
        membership.user = user;

        activatedRoute.data = of({ membership });
        comp.ngOnInit();

        expect(comp.editForm.value).toEqual(expect.objectContaining(membership));
        expect(comp.servicePackagesCollection).toContain(servicePackage);
        expect(comp.corporatesCollection).toContain(corporate);
        expect(comp.usersSharedCollection).toContain(user);
      });
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', () => {
        // GIVEN
        const saveSubject = new Subject();
        const membership = { id: 123 };
        spyOn(membershipService, 'update').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ membership });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: membership }));
        saveSubject.complete();

        // THEN
        expect(comp.previousState).toHaveBeenCalled();
        expect(membershipService.update).toHaveBeenCalledWith(membership);
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', () => {
        // GIVEN
        const saveSubject = new Subject();
        const membership = new Membership();
        spyOn(membershipService, 'create').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ membership });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: membership }));
        saveSubject.complete();

        // THEN
        expect(membershipService.create).toHaveBeenCalledWith(membership);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).toHaveBeenCalled();
      });

      it('Should set isSaving to false on error', () => {
        // GIVEN
        const saveSubject = new Subject();
        const membership = { id: 123 };
        spyOn(membershipService, 'update').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ membership });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.error('This is an error!');

        // THEN
        expect(membershipService.update).toHaveBeenCalledWith(membership);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).not.toHaveBeenCalled();
      });
    });

    describe('Tracking relationships identifiers', () => {
      describe('trackServicePackageById', () => {
        it('Should return tracked ServicePackage primary key', () => {
          const entity = { id: 123 };
          const trackResult = comp.trackServicePackageById(0, entity);
          expect(trackResult).toEqual(entity.id);
        });
      });

      describe('trackCorporateById', () => {
        it('Should return tracked Corporate primary key', () => {
          const entity = { id: 123 };
          const trackResult = comp.trackCorporateById(0, entity);
          expect(trackResult).toEqual(entity.id);
        });
      });

      describe('trackUserById', () => {
        it('Should return tracked User primary key', () => {
          const entity = { id: 123 };
          const trackResult = comp.trackUserById(0, entity);
          expect(trackResult).toEqual(entity.id);
        });
      });
    });
  });
});

jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { ValidationRequestService } from '../service/validation-request.service';
import { IValidationRequest, ValidationRequest } from '../validation-request.model';

import { IUser } from 'app/entities/user/user.model';
import { UserService } from 'app/entities/user/user.service';

import { ValidationRequestUpdateComponent } from './validation-request-update.component';

describe('Component Tests', () => {
  describe('ValidationRequest Management Update Component', () => {
    let comp: ValidationRequestUpdateComponent;
    let fixture: ComponentFixture<ValidationRequestUpdateComponent>;
    let activatedRoute: ActivatedRoute;
    let validationRequestService: ValidationRequestService;
    let userService: UserService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [ValidationRequestUpdateComponent],
        providers: [FormBuilder, ActivatedRoute],
      })
        .overrideTemplate(ValidationRequestUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(ValidationRequestUpdateComponent);
      activatedRoute = TestBed.inject(ActivatedRoute);
      validationRequestService = TestBed.inject(ValidationRequestService);
      userService = TestBed.inject(UserService);

      comp = fixture.componentInstance;
    });

    describe('ngOnInit', () => {
      it('Should call User query and add missing value', () => {
        const validationRequest: IValidationRequest = { id: 456 };
        const user: IUser = { id: 13820 };
        validationRequest.user = user;
        const createdBy: IUser = { id: 8136 };
        validationRequest.createdBy = createdBy;
        const acceptedBy: IUser = { id: 62330 };
        validationRequest.acceptedBy = acceptedBy;

        const userCollection: IUser[] = [{ id: 48385 }];
        spyOn(userService, 'query').and.returnValue(of(new HttpResponse({ body: userCollection })));
        const additionalUsers = [user, createdBy, acceptedBy];
        const expectedCollection: IUser[] = [...additionalUsers, ...userCollection];
        spyOn(userService, 'addUserToCollectionIfMissing').and.returnValue(expectedCollection);

        activatedRoute.data = of({ validationRequest });
        comp.ngOnInit();

        expect(userService.query).toHaveBeenCalled();
        expect(userService.addUserToCollectionIfMissing).toHaveBeenCalledWith(userCollection, ...additionalUsers);
        expect(comp.usersSharedCollection).toEqual(expectedCollection);
      });

      it('Should update editForm', () => {
        const validationRequest: IValidationRequest = { id: 456 };
        const user: IUser = { id: 2487 };
        validationRequest.user = user;
        const createdBy: IUser = { id: 97240 };
        validationRequest.createdBy = createdBy;
        const acceptedBy: IUser = { id: 53039 };
        validationRequest.acceptedBy = acceptedBy;

        activatedRoute.data = of({ validationRequest });
        comp.ngOnInit();

        expect(comp.editForm.value).toEqual(expect.objectContaining(validationRequest));
        expect(comp.usersSharedCollection).toContain(user);
        expect(comp.usersSharedCollection).toContain(createdBy);
        expect(comp.usersSharedCollection).toContain(acceptedBy);
      });
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', () => {
        // GIVEN
        const saveSubject = new Subject();
        const validationRequest = { id: 123 };
        spyOn(validationRequestService, 'update').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ validationRequest });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: validationRequest }));
        saveSubject.complete();

        // THEN
        expect(comp.previousState).toHaveBeenCalled();
        expect(validationRequestService.update).toHaveBeenCalledWith(validationRequest);
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', () => {
        // GIVEN
        const saveSubject = new Subject();
        const validationRequest = new ValidationRequest();
        spyOn(validationRequestService, 'create').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ validationRequest });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: validationRequest }));
        saveSubject.complete();

        // THEN
        expect(validationRequestService.create).toHaveBeenCalledWith(validationRequest);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).toHaveBeenCalled();
      });

      it('Should set isSaving to false on error', () => {
        // GIVEN
        const saveSubject = new Subject();
        const validationRequest = { id: 123 };
        spyOn(validationRequestService, 'update').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ validationRequest });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.error('This is an error!');

        // THEN
        expect(validationRequestService.update).toHaveBeenCalledWith(validationRequest);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).not.toHaveBeenCalled();
      });
    });

    describe('Tracking relationships identifiers', () => {
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

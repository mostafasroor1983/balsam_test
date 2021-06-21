jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { ValidationRequestFileService } from '../service/validation-request-file.service';
import { IValidationRequestFile, ValidationRequestFile } from '../validation-request-file.model';
import { IValidationRequest } from 'app/entities/validation-request/validation-request.model';
import { ValidationRequestService } from 'app/entities/validation-request/service/validation-request.service';

import { ValidationRequestFileUpdateComponent } from './validation-request-file-update.component';

describe('Component Tests', () => {
  describe('ValidationRequestFile Management Update Component', () => {
    let comp: ValidationRequestFileUpdateComponent;
    let fixture: ComponentFixture<ValidationRequestFileUpdateComponent>;
    let activatedRoute: ActivatedRoute;
    let validationRequestFileService: ValidationRequestFileService;
    let validationRequestService: ValidationRequestService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [ValidationRequestFileUpdateComponent],
        providers: [FormBuilder, ActivatedRoute],
      })
        .overrideTemplate(ValidationRequestFileUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(ValidationRequestFileUpdateComponent);
      activatedRoute = TestBed.inject(ActivatedRoute);
      validationRequestFileService = TestBed.inject(ValidationRequestFileService);
      validationRequestService = TestBed.inject(ValidationRequestService);

      comp = fixture.componentInstance;
    });

    describe('ngOnInit', () => {
      it('Should call ValidationRequest query and add missing value', () => {
        const validationRequestFile: IValidationRequestFile = { id: 456 };
        const validationRequest: IValidationRequest = { id: 88676 };
        validationRequestFile.validationRequest = validationRequest;

        const validationRequestCollection: IValidationRequest[] = [{ id: 11656 }];
        spyOn(validationRequestService, 'query').and.returnValue(of(new HttpResponse({ body: validationRequestCollection })));
        const additionalValidationRequests = [validationRequest];
        const expectedCollection: IValidationRequest[] = [...additionalValidationRequests, ...validationRequestCollection];
        spyOn(validationRequestService, 'addValidationRequestToCollectionIfMissing').and.returnValue(expectedCollection);

        activatedRoute.data = of({ validationRequestFile });
        comp.ngOnInit();

        expect(validationRequestService.query).toHaveBeenCalled();
        expect(validationRequestService.addValidationRequestToCollectionIfMissing).toHaveBeenCalledWith(
          validationRequestCollection,
          ...additionalValidationRequests
        );
        expect(comp.validationRequestsSharedCollection).toEqual(expectedCollection);
      });

      it('Should update editForm', () => {
        const validationRequestFile: IValidationRequestFile = { id: 456 };
        const validationRequest: IValidationRequest = { id: 99060 };
        validationRequestFile.validationRequest = validationRequest;

        activatedRoute.data = of({ validationRequestFile });
        comp.ngOnInit();

        expect(comp.editForm.value).toEqual(expect.objectContaining(validationRequestFile));
        expect(comp.validationRequestsSharedCollection).toContain(validationRequest);
      });
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', () => {
        // GIVEN
        const saveSubject = new Subject();
        const validationRequestFile = { id: 123 };
        spyOn(validationRequestFileService, 'update').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ validationRequestFile });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: validationRequestFile }));
        saveSubject.complete();

        // THEN
        expect(comp.previousState).toHaveBeenCalled();
        expect(validationRequestFileService.update).toHaveBeenCalledWith(validationRequestFile);
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', () => {
        // GIVEN
        const saveSubject = new Subject();
        const validationRequestFile = new ValidationRequestFile();
        spyOn(validationRequestFileService, 'create').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ validationRequestFile });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: validationRequestFile }));
        saveSubject.complete();

        // THEN
        expect(validationRequestFileService.create).toHaveBeenCalledWith(validationRequestFile);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).toHaveBeenCalled();
      });

      it('Should set isSaving to false on error', () => {
        // GIVEN
        const saveSubject = new Subject();
        const validationRequestFile = { id: 123 };
        spyOn(validationRequestFileService, 'update').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ validationRequestFile });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.error('This is an error!');

        // THEN
        expect(validationRequestFileService.update).toHaveBeenCalledWith(validationRequestFile);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).not.toHaveBeenCalled();
      });
    });

    describe('Tracking relationships identifiers', () => {
      describe('trackValidationRequestById', () => {
        it('Should return tracked ValidationRequest primary key', () => {
          const entity = { id: 123 };
          const trackResult = comp.trackValidationRequestById(0, entity);
          expect(trackResult).toEqual(entity.id);
        });
      });
    });
  });
});

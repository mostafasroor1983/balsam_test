import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import * as dayjs from 'dayjs';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';

import { IValidationRequest, ValidationRequest } from '../validation-request.model';
import { ValidationRequestService } from '../service/validation-request.service';
import { IUser } from 'app/entities/user/user.model';
import { UserService } from 'app/entities/user/user.service';

@Component({
  selector: 'jhi-validation-request-update',
  templateUrl: './validation-request-update.component.html',
})
export class ValidationRequestUpdateComponent implements OnInit {
  isSaving = false;

  usersSharedCollection: IUser[] = [];

  editForm = this.fb.group({
    id: [],
    status: [],
    actionDateTime: [],
    reason: [null, [Validators.maxLength(2000)]],
    notes: [null, [Validators.maxLength(5000)]],
    user: [],
    createdBy: [],
    acceptedBy: [],
  });

  constructor(
    protected validationRequestService: ValidationRequestService,
    protected userService: UserService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ validationRequest }) => {
      if (validationRequest.id === undefined) {
        const today = dayjs().startOf('day');
        validationRequest.actionDateTime = today;
      }

      this.updateForm(validationRequest);

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const validationRequest = this.createFromForm();
    if (validationRequest.id !== undefined) {
      this.subscribeToSaveResponse(this.validationRequestService.update(validationRequest));
    } else {
      this.subscribeToSaveResponse(this.validationRequestService.create(validationRequest));
    }
  }

  trackUserById(index: number, item: IUser): number {
    return item.id!;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IValidationRequest>>): void {
    result.pipe(finalize(() => this.onSaveFinalize())).subscribe(
      () => this.onSaveSuccess(),
      () => this.onSaveError()
    );
  }

  protected onSaveSuccess(): void {
    this.previousState();
  }

  protected onSaveError(): void {
    // Api for inheritance.
  }

  protected onSaveFinalize(): void {
    this.isSaving = false;
  }

  protected updateForm(validationRequest: IValidationRequest): void {
    this.editForm.patchValue({
      id: validationRequest.id,
      status: validationRequest.status,
      actionDateTime: validationRequest.actionDateTime ? validationRequest.actionDateTime.format(DATE_TIME_FORMAT) : null,
      reason: validationRequest.reason,
      notes: validationRequest.notes,
      user: validationRequest.user,
      createdBy: validationRequest.createdBy,
      acceptedBy: validationRequest.acceptedBy,
    });

    this.usersSharedCollection = this.userService.addUserToCollectionIfMissing(
      this.usersSharedCollection,
      validationRequest.user,
      validationRequest.createdBy,
      validationRequest.acceptedBy
    );
  }

  protected loadRelationshipsOptions(): void {
    this.userService
      .query()
      .pipe(map((res: HttpResponse<IUser[]>) => res.body ?? []))
      .pipe(
        map((users: IUser[]) =>
          this.userService.addUserToCollectionIfMissing(
            users,
            this.editForm.get('user')!.value,
            this.editForm.get('createdBy')!.value,
            this.editForm.get('acceptedBy')!.value
          )
        )
      )
      .subscribe((users: IUser[]) => (this.usersSharedCollection = users));
  }

  protected createFromForm(): IValidationRequest {
    return {
      ...new ValidationRequest(),
      id: this.editForm.get(['id'])!.value,
      status: this.editForm.get(['status'])!.value,
      actionDateTime: this.editForm.get(['actionDateTime'])!.value
        ? dayjs(this.editForm.get(['actionDateTime'])!.value, DATE_TIME_FORMAT)
        : undefined,
      reason: this.editForm.get(['reason'])!.value,
      notes: this.editForm.get(['notes'])!.value,
      user: this.editForm.get(['user'])!.value,
      createdBy: this.editForm.get(['createdBy'])!.value,
      acceptedBy: this.editForm.get(['acceptedBy'])!.value,
    };
  }
}

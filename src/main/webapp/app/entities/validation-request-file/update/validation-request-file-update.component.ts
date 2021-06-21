import { Component, OnInit, ElementRef } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { IValidationRequestFile, ValidationRequestFile } from '../validation-request-file.model';
import { ValidationRequestFileService } from '../service/validation-request-file.service';
import { AlertError } from 'app/shared/alert/alert-error.model';
import { EventManager, EventWithContent } from 'app/core/util/event-manager.service';
import { DataUtils, FileLoadError } from 'app/core/util/data-util.service';
import { IValidationRequest } from 'app/entities/validation-request/validation-request.model';
import { ValidationRequestService } from 'app/entities/validation-request/service/validation-request.service';

@Component({
  selector: 'jhi-validation-request-file-update',
  templateUrl: './validation-request-file-update.component.html',
})
export class ValidationRequestFileUpdateComponent implements OnInit {
  isSaving = false;

  validationRequestsSharedCollection: IValidationRequest[] = [];

  editForm = this.fb.group({
    id: [],
    name: [null, [Validators.required, Validators.maxLength(1000)]],
    file: [],
    fileContentType: [],
    type: [],
    validationRequest: [],
  });

  constructor(
    protected dataUtils: DataUtils,
    protected eventManager: EventManager,
    protected validationRequestFileService: ValidationRequestFileService,
    protected validationRequestService: ValidationRequestService,
    protected elementRef: ElementRef,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ validationRequestFile }) => {
      this.updateForm(validationRequestFile);

      this.loadRelationshipsOptions();
    });
  }

  byteSize(base64String: string): string {
    return this.dataUtils.byteSize(base64String);
  }

  openFile(base64String: string, contentType: string | null | undefined): void {
    this.dataUtils.openFile(base64String, contentType);
  }

  setFileData(event: Event, field: string, isImage: boolean): void {
    this.dataUtils.loadFileToForm(event, this.editForm, field, isImage).subscribe({
      error: (err: FileLoadError) =>
        this.eventManager.broadcast(
          new EventWithContent<AlertError>('balsamTestApp.error', { ...err, key: 'error.file.' + err.key })
        ),
    });
  }

  clearInputImage(field: string, fieldContentType: string, idInput: string): void {
    this.editForm.patchValue({
      [field]: null,
      [fieldContentType]: null,
    });
    if (idInput && this.elementRef.nativeElement.querySelector('#' + idInput)) {
      this.elementRef.nativeElement.querySelector('#' + idInput).value = null;
    }
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const validationRequestFile = this.createFromForm();
    if (validationRequestFile.id !== undefined) {
      this.subscribeToSaveResponse(this.validationRequestFileService.update(validationRequestFile));
    } else {
      this.subscribeToSaveResponse(this.validationRequestFileService.create(validationRequestFile));
    }
  }

  trackValidationRequestById(index: number, item: IValidationRequest): number {
    return item.id!;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IValidationRequestFile>>): void {
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

  protected updateForm(validationRequestFile: IValidationRequestFile): void {
    this.editForm.patchValue({
      id: validationRequestFile.id,
      name: validationRequestFile.name,
      file: validationRequestFile.file,
      fileContentType: validationRequestFile.fileContentType,
      type: validationRequestFile.type,
      validationRequest: validationRequestFile.validationRequest,
    });

    this.validationRequestsSharedCollection = this.validationRequestService.addValidationRequestToCollectionIfMissing(
      this.validationRequestsSharedCollection,
      validationRequestFile.validationRequest
    );
  }

  protected loadRelationshipsOptions(): void {
    this.validationRequestService
      .query()
      .pipe(map((res: HttpResponse<IValidationRequest[]>) => res.body ?? []))
      .pipe(
        map((validationRequests: IValidationRequest[]) =>
          this.validationRequestService.addValidationRequestToCollectionIfMissing(
            validationRequests,
            this.editForm.get('validationRequest')!.value
          )
        )
      )
      .subscribe((validationRequests: IValidationRequest[]) => (this.validationRequestsSharedCollection = validationRequests));
  }

  protected createFromForm(): IValidationRequestFile {
    return {
      ...new ValidationRequestFile(),
      id: this.editForm.get(['id'])!.value,
      name: this.editForm.get(['name'])!.value,
      fileContentType: this.editForm.get(['fileContentType'])!.value,
      file: this.editForm.get(['file'])!.value,
      type: this.editForm.get(['type'])!.value,
      validationRequest: this.editForm.get(['validationRequest'])!.value,
    };
  }
}

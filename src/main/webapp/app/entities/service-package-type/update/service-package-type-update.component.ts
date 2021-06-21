import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import { IServicePackageType, ServicePackageType } from '../service-package-type.model';
import { ServicePackageTypeService } from '../service/service-package-type.service';

@Component({
  selector: 'jhi-service-package-type-update',
  templateUrl: './service-package-type-update.component.html',
})
export class ServicePackageTypeUpdateComponent implements OnInit {
  isSaving = false;

  editForm = this.fb.group({
    id: [],
    name: [null, [Validators.required, Validators.maxLength(500)]],
  });

  constructor(
    protected servicePackageTypeService: ServicePackageTypeService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ servicePackageType }) => {
      this.updateForm(servicePackageType);
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const servicePackageType = this.createFromForm();
    if (servicePackageType.id !== undefined) {
      this.subscribeToSaveResponse(this.servicePackageTypeService.update(servicePackageType));
    } else {
      this.subscribeToSaveResponse(this.servicePackageTypeService.create(servicePackageType));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IServicePackageType>>): void {
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

  protected updateForm(servicePackageType: IServicePackageType): void {
    this.editForm.patchValue({
      id: servicePackageType.id,
      name: servicePackageType.name,
    });
  }

  protected createFromForm(): IServicePackageType {
    return {
      ...new ServicePackageType(),
      id: this.editForm.get(['id'])!.value,
      name: this.editForm.get(['name'])!.value,
    };
  }
}

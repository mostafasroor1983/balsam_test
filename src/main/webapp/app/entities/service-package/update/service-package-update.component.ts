import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { IServicePackage, ServicePackage } from '../service-package.model';
import { ServicePackageService } from '../service/service-package.service';
import { ICountry } from 'app/entities/country/country.model';
import { CountryService } from 'app/entities/country/service/country.service';
import { IServicePackageType } from 'app/entities/service-package-type/service-package-type.model';
import { ServicePackageTypeService } from 'app/entities/service-package-type/service/service-package-type.service';

@Component({
  selector: 'jhi-service-package-update',
  templateUrl: './service-package-update.component.html',
})
export class ServicePackageUpdateComponent implements OnInit {
  isSaving = false;

  countriesCollection: ICountry[] = [];
  servicePackageTypesSharedCollection: IServicePackageType[] = [];

  editForm = this.fb.group({
    id: [],
    name: [null, [Validators.required, Validators.maxLength(1000)]],
    recommended: [],
    tagName: [null, [Validators.maxLength(500)]],
    country: [],
    packageType: [],
  });

  constructor(
    protected servicePackageService: ServicePackageService,
    protected countryService: CountryService,
    protected servicePackageTypeService: ServicePackageTypeService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ servicePackage }) => {
      this.updateForm(servicePackage);

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const servicePackage = this.createFromForm();
    if (servicePackage.id !== undefined) {
      this.subscribeToSaveResponse(this.servicePackageService.update(servicePackage));
    } else {
      this.subscribeToSaveResponse(this.servicePackageService.create(servicePackage));
    }
  }

  trackCountryById(index: number, item: ICountry): number {
    return item.id!;
  }

  trackServicePackageTypeById(index: number, item: IServicePackageType): number {
    return item.id!;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IServicePackage>>): void {
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

  protected updateForm(servicePackage: IServicePackage): void {
    this.editForm.patchValue({
      id: servicePackage.id,
      name: servicePackage.name,
      recommended: servicePackage.recommended,
      tagName: servicePackage.tagName,
      country: servicePackage.country,
      packageType: servicePackage.packageType,
    });

    this.countriesCollection = this.countryService.addCountryToCollectionIfMissing(this.countriesCollection, servicePackage.country);
    this.servicePackageTypesSharedCollection = this.servicePackageTypeService.addServicePackageTypeToCollectionIfMissing(
      this.servicePackageTypesSharedCollection,
      servicePackage.packageType
    );
  }

  protected loadRelationshipsOptions(): void {
    this.countryService
      .query({ 'servicePackageId.specified': 'false' })
      .pipe(map((res: HttpResponse<ICountry[]>) => res.body ?? []))
      .pipe(
        map((countries: ICountry[]) => this.countryService.addCountryToCollectionIfMissing(countries, this.editForm.get('country')!.value))
      )
      .subscribe((countries: ICountry[]) => (this.countriesCollection = countries));

    this.servicePackageTypeService
      .query()
      .pipe(map((res: HttpResponse<IServicePackageType[]>) => res.body ?? []))
      .pipe(
        map((servicePackageTypes: IServicePackageType[]) =>
          this.servicePackageTypeService.addServicePackageTypeToCollectionIfMissing(
            servicePackageTypes,
            this.editForm.get('packageType')!.value
          )
        )
      )
      .subscribe((servicePackageTypes: IServicePackageType[]) => (this.servicePackageTypesSharedCollection = servicePackageTypes));
  }

  protected createFromForm(): IServicePackage {
    return {
      ...new ServicePackage(),
      id: this.editForm.get(['id'])!.value,
      name: this.editForm.get(['name'])!.value,
      recommended: this.editForm.get(['recommended'])!.value,
      tagName: this.editForm.get(['tagName'])!.value,
      country: this.editForm.get(['country'])!.value,
      packageType: this.editForm.get(['packageType'])!.value,
    };
  }
}

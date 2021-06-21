import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import * as dayjs from 'dayjs';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';

import { IMembership, Membership } from '../membership.model';
import { MembershipService } from '../service/membership.service';
import { IServicePackage } from 'app/entities/service-package/service-package.model';
import { ServicePackageService } from 'app/entities/service-package/service/service-package.service';
import { ICorporate } from 'app/entities/corporate/corporate.model';
import { CorporateService } from 'app/entities/corporate/service/corporate.service';
import { IUser } from 'app/entities/user/user.model';
import { UserService } from 'app/entities/user/user.service';

@Component({
  selector: 'jhi-membership-update',
  templateUrl: './membership-update.component.html',
})
export class MembershipUpdateComponent implements OnInit {
  isSaving = false;

  servicePackagesCollection: IServicePackage[] = [];
  corporatesCollection: ICorporate[] = [];
  usersSharedCollection: IUser[] = [];

  editForm = this.fb.group({
    id: [],
    membershipId: [null, [Validators.required, Validators.maxLength(20)]],
    memberType: [],
    active: [],
    hasPhysicalVersion: [],
    memberShare: [],
    corporateShare: [],
    printingDateTime: [],
    servicePackage: [],
    corporate: [],
    user: [],
  });

  constructor(
    protected membershipService: MembershipService,
    protected servicePackageService: ServicePackageService,
    protected corporateService: CorporateService,
    protected userService: UserService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ membership }) => {
      if (membership.id === undefined) {
        const today = dayjs().startOf('day');
        membership.printingDateTime = today;
      }

      this.updateForm(membership);

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const membership = this.createFromForm();
    if (membership.id !== undefined) {
      this.subscribeToSaveResponse(this.membershipService.update(membership));
    } else {
      this.subscribeToSaveResponse(this.membershipService.create(membership));
    }
  }

  trackServicePackageById(index: number, item: IServicePackage): number {
    return item.id!;
  }

  trackCorporateById(index: number, item: ICorporate): number {
    return item.id!;
  }

  trackUserById(index: number, item: IUser): number {
    return item.id!;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IMembership>>): void {
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

  protected updateForm(membership: IMembership): void {
    this.editForm.patchValue({
      id: membership.id,
      membershipId: membership.membershipId,
      memberType: membership.memberType,
      active: membership.active,
      hasPhysicalVersion: membership.hasPhysicalVersion,
      memberShare: membership.memberShare,
      corporateShare: membership.corporateShare,
      printingDateTime: membership.printingDateTime ? membership.printingDateTime.format(DATE_TIME_FORMAT) : null,
      servicePackage: membership.servicePackage,
      corporate: membership.corporate,
      user: membership.user,
    });

    this.servicePackagesCollection = this.servicePackageService.addServicePackageToCollectionIfMissing(
      this.servicePackagesCollection,
      membership.servicePackage
    );
    this.corporatesCollection = this.corporateService.addCorporateToCollectionIfMissing(this.corporatesCollection, membership.corporate);
    this.usersSharedCollection = this.userService.addUserToCollectionIfMissing(this.usersSharedCollection, membership.user);
  }

  protected loadRelationshipsOptions(): void {
    this.servicePackageService
      .query({ 'membershipId.specified': 'false' })
      .pipe(map((res: HttpResponse<IServicePackage[]>) => res.body ?? []))
      .pipe(
        map((servicePackages: IServicePackage[]) =>
          this.servicePackageService.addServicePackageToCollectionIfMissing(servicePackages, this.editForm.get('servicePackage')!.value)
        )
      )
      .subscribe((servicePackages: IServicePackage[]) => (this.servicePackagesCollection = servicePackages));

    this.corporateService
      .query({ 'membershipId.specified': 'false' })
      .pipe(map((res: HttpResponse<ICorporate[]>) => res.body ?? []))
      .pipe(
        map((corporates: ICorporate[]) =>
          this.corporateService.addCorporateToCollectionIfMissing(corporates, this.editForm.get('corporate')!.value)
        )
      )
      .subscribe((corporates: ICorporate[]) => (this.corporatesCollection = corporates));

    this.userService
      .query()
      .pipe(map((res: HttpResponse<IUser[]>) => res.body ?? []))
      .pipe(map((users: IUser[]) => this.userService.addUserToCollectionIfMissing(users, this.editForm.get('user')!.value)))
      .subscribe((users: IUser[]) => (this.usersSharedCollection = users));
  }

  protected createFromForm(): IMembership {
    return {
      ...new Membership(),
      id: this.editForm.get(['id'])!.value,
      membershipId: this.editForm.get(['membershipId'])!.value,
      memberType: this.editForm.get(['memberType'])!.value,
      active: this.editForm.get(['active'])!.value,
      hasPhysicalVersion: this.editForm.get(['hasPhysicalVersion'])!.value,
      memberShare: this.editForm.get(['memberShare'])!.value,
      corporateShare: this.editForm.get(['corporateShare'])!.value,
      printingDateTime: this.editForm.get(['printingDateTime'])!.value
        ? dayjs(this.editForm.get(['printingDateTime'])!.value, DATE_TIME_FORMAT)
        : undefined,
      servicePackage: this.editForm.get(['servicePackage'])!.value,
      corporate: this.editForm.get(['corporate'])!.value,
      user: this.editForm.get(['user'])!.value,
    };
  }
}

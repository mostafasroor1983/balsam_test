import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ValidationRequestComponent } from '../list/validation-request.component';
import { ValidationRequestDetailComponent } from '../detail/validation-request-detail.component';
import { ValidationRequestUpdateComponent } from '../update/validation-request-update.component';
import { ValidationRequestRoutingResolveService } from './validation-request-routing-resolve.service';

const validationRequestRoute: Routes = [
  {
    path: '',
    component: ValidationRequestComponent,
    data: {
      defaultSort: 'id,asc',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: ValidationRequestDetailComponent,
    resolve: {
      validationRequest: ValidationRequestRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: ValidationRequestUpdateComponent,
    resolve: {
      validationRequest: ValidationRequestRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: ValidationRequestUpdateComponent,
    resolve: {
      validationRequest: ValidationRequestRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(validationRequestRoute)],
  exports: [RouterModule],
})
export class ValidationRequestRoutingModule {}

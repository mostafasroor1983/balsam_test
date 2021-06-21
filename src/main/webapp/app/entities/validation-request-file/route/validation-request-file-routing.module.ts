import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ValidationRequestFileComponent } from '../list/validation-request-file.component';
import { ValidationRequestFileDetailComponent } from '../detail/validation-request-file-detail.component';
import { ValidationRequestFileUpdateComponent } from '../update/validation-request-file-update.component';
import { ValidationRequestFileRoutingResolveService } from './validation-request-file-routing-resolve.service';

const validationRequestFileRoute: Routes = [
  {
    path: '',
    component: ValidationRequestFileComponent,
    data: {
      defaultSort: 'id,asc',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: ValidationRequestFileDetailComponent,
    resolve: {
      validationRequestFile: ValidationRequestFileRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: ValidationRequestFileUpdateComponent,
    resolve: {
      validationRequestFile: ValidationRequestFileRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: ValidationRequestFileUpdateComponent,
    resolve: {
      validationRequestFile: ValidationRequestFileRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(validationRequestFileRoute)],
  exports: [RouterModule],
})
export class ValidationRequestFileRoutingModule {}

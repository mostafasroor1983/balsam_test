import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { CorporateComponent } from '../list/corporate.component';
import { CorporateDetailComponent } from '../detail/corporate-detail.component';
import { CorporateUpdateComponent } from '../update/corporate-update.component';
import { CorporateRoutingResolveService } from './corporate-routing-resolve.service';

const corporateRoute: Routes = [
  {
    path: '',
    component: CorporateComponent,
    data: {
      defaultSort: 'id,asc',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: CorporateDetailComponent,
    resolve: {
      corporate: CorporateRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: CorporateUpdateComponent,
    resolve: {
      corporate: CorporateRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: CorporateUpdateComponent,
    resolve: {
      corporate: CorporateRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(corporateRoute)],
  exports: [RouterModule],
})
export class CorporateRoutingModule {}

import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ServicePackageTypeComponent } from '../list/service-package-type.component';
import { ServicePackageTypeDetailComponent } from '../detail/service-package-type-detail.component';
import { ServicePackageTypeUpdateComponent } from '../update/service-package-type-update.component';
import { ServicePackageTypeRoutingResolveService } from './service-package-type-routing-resolve.service';

const servicePackageTypeRoute: Routes = [
  {
    path: '',
    component: ServicePackageTypeComponent,
    data: {
      defaultSort: 'id,asc',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: ServicePackageTypeDetailComponent,
    resolve: {
      servicePackageType: ServicePackageTypeRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: ServicePackageTypeUpdateComponent,
    resolve: {
      servicePackageType: ServicePackageTypeRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: ServicePackageTypeUpdateComponent,
    resolve: {
      servicePackageType: ServicePackageTypeRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(servicePackageTypeRoute)],
  exports: [RouterModule],
})
export class ServicePackageTypeRoutingModule {}

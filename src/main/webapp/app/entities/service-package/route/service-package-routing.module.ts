import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ServicePackageComponent } from '../list/service-package.component';
import { ServicePackageDetailComponent } from '../detail/service-package-detail.component';
import { ServicePackageUpdateComponent } from '../update/service-package-update.component';
import { ServicePackageRoutingResolveService } from './service-package-routing-resolve.service';

const servicePackageRoute: Routes = [
  {
    path: '',
    component: ServicePackageComponent,
    data: {
      defaultSort: 'id,asc',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: ServicePackageDetailComponent,
    resolve: {
      servicePackage: ServicePackageRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: ServicePackageUpdateComponent,
    resolve: {
      servicePackage: ServicePackageRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: ServicePackageUpdateComponent,
    resolve: {
      servicePackage: ServicePackageRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(servicePackageRoute)],
  exports: [RouterModule],
})
export class ServicePackageRoutingModule {}

import { NgModule } from '@angular/core';

import { SharedModule } from 'app/shared/shared.module';
import { ServicePackageComponent } from './list/service-package.component';
import { ServicePackageDetailComponent } from './detail/service-package-detail.component';
import { ServicePackageUpdateComponent } from './update/service-package-update.component';
import { ServicePackageDeleteDialogComponent } from './delete/service-package-delete-dialog.component';
import { ServicePackageRoutingModule } from './route/service-package-routing.module';

@NgModule({
  imports: [SharedModule, ServicePackageRoutingModule],
  declarations: [
    ServicePackageComponent,
    ServicePackageDetailComponent,
    ServicePackageUpdateComponent,
    ServicePackageDeleteDialogComponent,
  ],
  entryComponents: [ServicePackageDeleteDialogComponent],
})
export class ServicePackageModule {}

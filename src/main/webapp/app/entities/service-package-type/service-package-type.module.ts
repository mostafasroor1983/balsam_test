import { NgModule } from '@angular/core';

import { SharedModule } from 'app/shared/shared.module';
import { ServicePackageTypeComponent } from './list/service-package-type.component';
import { ServicePackageTypeDetailComponent } from './detail/service-package-type-detail.component';
import { ServicePackageTypeUpdateComponent } from './update/service-package-type-update.component';
import { ServicePackageTypeDeleteDialogComponent } from './delete/service-package-type-delete-dialog.component';
import { ServicePackageTypeRoutingModule } from './route/service-package-type-routing.module';

@NgModule({
  imports: [SharedModule, ServicePackageTypeRoutingModule],
  declarations: [
    ServicePackageTypeComponent,
    ServicePackageTypeDetailComponent,
    ServicePackageTypeUpdateComponent,
    ServicePackageTypeDeleteDialogComponent,
  ],
  entryComponents: [ServicePackageTypeDeleteDialogComponent],
})
export class ServicePackageTypeModule {}

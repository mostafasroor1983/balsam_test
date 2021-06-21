import { NgModule } from '@angular/core';

import { SharedModule } from 'app/shared/shared.module';
import { CorporateComponent } from './list/corporate.component';
import { CorporateDetailComponent } from './detail/corporate-detail.component';
import { CorporateUpdateComponent } from './update/corporate-update.component';
import { CorporateDeleteDialogComponent } from './delete/corporate-delete-dialog.component';
import { CorporateRoutingModule } from './route/corporate-routing.module';

@NgModule({
  imports: [SharedModule, CorporateRoutingModule],
  declarations: [CorporateComponent, CorporateDetailComponent, CorporateUpdateComponent, CorporateDeleteDialogComponent],
  entryComponents: [CorporateDeleteDialogComponent],
})
export class CorporateModule {}

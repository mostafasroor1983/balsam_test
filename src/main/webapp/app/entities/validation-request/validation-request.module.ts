import { NgModule } from '@angular/core';

import { SharedModule } from 'app/shared/shared.module';
import { ValidationRequestComponent } from './list/validation-request.component';
import { ValidationRequestDetailComponent } from './detail/validation-request-detail.component';
import { ValidationRequestUpdateComponent } from './update/validation-request-update.component';
import { ValidationRequestDeleteDialogComponent } from './delete/validation-request-delete-dialog.component';
import { ValidationRequestRoutingModule } from './route/validation-request-routing.module';

@NgModule({
  imports: [SharedModule, ValidationRequestRoutingModule],
  declarations: [
    ValidationRequestComponent,
    ValidationRequestDetailComponent,
    ValidationRequestUpdateComponent,
    ValidationRequestDeleteDialogComponent,
  ],
  entryComponents: [ValidationRequestDeleteDialogComponent],
})
export class ValidationRequestModule {}

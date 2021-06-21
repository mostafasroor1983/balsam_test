import { NgModule } from '@angular/core';

import { SharedModule } from 'app/shared/shared.module';
import { ValidationRequestFileComponent } from './list/validation-request-file.component';
import { ValidationRequestFileDetailComponent } from './detail/validation-request-file-detail.component';
import { ValidationRequestFileUpdateComponent } from './update/validation-request-file-update.component';
import { ValidationRequestFileDeleteDialogComponent } from './delete/validation-request-file-delete-dialog.component';
import { ValidationRequestFileRoutingModule } from './route/validation-request-file-routing.module';

@NgModule({
  imports: [SharedModule, ValidationRequestFileRoutingModule],
  declarations: [
    ValidationRequestFileComponent,
    ValidationRequestFileDetailComponent,
    ValidationRequestFileUpdateComponent,
    ValidationRequestFileDeleteDialogComponent,
  ],
  entryComponents: [ValidationRequestFileDeleteDialogComponent],
})
export class ValidationRequestFileModule {}

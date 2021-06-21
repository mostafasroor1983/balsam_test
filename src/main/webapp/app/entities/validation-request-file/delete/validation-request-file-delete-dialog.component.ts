import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IValidationRequestFile } from '../validation-request-file.model';
import { ValidationRequestFileService } from '../service/validation-request-file.service';

@Component({
  templateUrl: './validation-request-file-delete-dialog.component.html',
})
export class ValidationRequestFileDeleteDialogComponent {
  validationRequestFile?: IValidationRequestFile;

  constructor(protected validationRequestFileService: ValidationRequestFileService, public activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.validationRequestFileService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}

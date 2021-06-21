import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IValidationRequest } from '../validation-request.model';
import { ValidationRequestService } from '../service/validation-request.service';

@Component({
  templateUrl: './validation-request-delete-dialog.component.html',
})
export class ValidationRequestDeleteDialogComponent {
  validationRequest?: IValidationRequest;

  constructor(protected validationRequestService: ValidationRequestService, public activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.validationRequestService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}

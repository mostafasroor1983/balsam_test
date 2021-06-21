import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { ICorporate } from '../corporate.model';
import { CorporateService } from '../service/corporate.service';

@Component({
  templateUrl: './corporate-delete-dialog.component.html',
})
export class CorporateDeleteDialogComponent {
  corporate?: ICorporate;

  constructor(protected corporateService: CorporateService, public activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.corporateService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}

import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IMembership } from '../membership.model';
import { MembershipService } from '../service/membership.service';

@Component({
  templateUrl: './membership-delete-dialog.component.html',
})
export class MembershipDeleteDialogComponent {
  membership?: IMembership;

  constructor(protected membershipService: MembershipService, public activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.membershipService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}

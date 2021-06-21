import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IServicePackage } from '../service-package.model';
import { ServicePackageService } from '../service/service-package.service';

@Component({
  templateUrl: './service-package-delete-dialog.component.html',
})
export class ServicePackageDeleteDialogComponent {
  servicePackage?: IServicePackage;

  constructor(protected servicePackageService: ServicePackageService, public activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.servicePackageService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}

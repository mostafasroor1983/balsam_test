import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IServicePackageType } from '../service-package-type.model';
import { ServicePackageTypeService } from '../service/service-package-type.service';

@Component({
  templateUrl: './service-package-type-delete-dialog.component.html',
})
export class ServicePackageTypeDeleteDialogComponent {
  servicePackageType?: IServicePackageType;

  constructor(protected servicePackageTypeService: ServicePackageTypeService, public activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.servicePackageTypeService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}

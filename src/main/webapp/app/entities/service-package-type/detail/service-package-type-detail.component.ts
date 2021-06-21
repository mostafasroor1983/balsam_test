import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IServicePackageType } from '../service-package-type.model';

@Component({
  selector: 'jhi-service-package-type-detail',
  templateUrl: './service-package-type-detail.component.html',
})
export class ServicePackageTypeDetailComponent implements OnInit {
  servicePackageType: IServicePackageType | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ servicePackageType }) => {
      this.servicePackageType = servicePackageType;
    });
  }

  previousState(): void {
    window.history.back();
  }
}

import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IServicePackage } from '../service-package.model';

@Component({
  selector: 'jhi-service-package-detail',
  templateUrl: './service-package-detail.component.html',
})
export class ServicePackageDetailComponent implements OnInit {
  servicePackage: IServicePackage | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ servicePackage }) => {
      this.servicePackage = servicePackage;
    });
  }

  previousState(): void {
    window.history.back();
  }
}

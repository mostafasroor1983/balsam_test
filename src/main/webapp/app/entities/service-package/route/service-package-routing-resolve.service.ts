import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IServicePackage, ServicePackage } from '../service-package.model';
import { ServicePackageService } from '../service/service-package.service';

@Injectable({ providedIn: 'root' })
export class ServicePackageRoutingResolveService implements Resolve<IServicePackage> {
  constructor(protected service: ServicePackageService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IServicePackage> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((servicePackage: HttpResponse<ServicePackage>) => {
          if (servicePackage.body) {
            return of(servicePackage.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new ServicePackage());
  }
}

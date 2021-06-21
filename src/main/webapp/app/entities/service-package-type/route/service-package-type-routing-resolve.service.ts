import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IServicePackageType, ServicePackageType } from '../service-package-type.model';
import { ServicePackageTypeService } from '../service/service-package-type.service';

@Injectable({ providedIn: 'root' })
export class ServicePackageTypeRoutingResolveService implements Resolve<IServicePackageType> {
  constructor(protected service: ServicePackageTypeService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IServicePackageType> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((servicePackageType: HttpResponse<ServicePackageType>) => {
          if (servicePackageType.body) {
            return of(servicePackageType.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new ServicePackageType());
  }
}

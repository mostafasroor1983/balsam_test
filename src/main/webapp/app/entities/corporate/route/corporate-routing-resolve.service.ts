import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { ICorporate, Corporate } from '../corporate.model';
import { CorporateService } from '../service/corporate.service';

@Injectable({ providedIn: 'root' })
export class CorporateRoutingResolveService implements Resolve<ICorporate> {
  constructor(protected service: CorporateService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<ICorporate> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((corporate: HttpResponse<Corporate>) => {
          if (corporate.body) {
            return of(corporate.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new Corporate());
  }
}

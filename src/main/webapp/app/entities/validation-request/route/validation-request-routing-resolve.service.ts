import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IValidationRequest, ValidationRequest } from '../validation-request.model';
import { ValidationRequestService } from '../service/validation-request.service';

@Injectable({ providedIn: 'root' })
export class ValidationRequestRoutingResolveService implements Resolve<IValidationRequest> {
  constructor(protected service: ValidationRequestService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IValidationRequest> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((validationRequest: HttpResponse<ValidationRequest>) => {
          if (validationRequest.body) {
            return of(validationRequest.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new ValidationRequest());
  }
}

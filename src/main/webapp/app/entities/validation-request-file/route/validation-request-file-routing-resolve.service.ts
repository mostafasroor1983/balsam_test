import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IValidationRequestFile, ValidationRequestFile } from '../validation-request-file.model';
import { ValidationRequestFileService } from '../service/validation-request-file.service';

@Injectable({ providedIn: 'root' })
export class ValidationRequestFileRoutingResolveService implements Resolve<IValidationRequestFile> {
  constructor(protected service: ValidationRequestFileService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IValidationRequestFile> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((validationRequestFile: HttpResponse<ValidationRequestFile>) => {
          if (validationRequestFile.body) {
            return of(validationRequestFile.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new ValidationRequestFile());
  }
}

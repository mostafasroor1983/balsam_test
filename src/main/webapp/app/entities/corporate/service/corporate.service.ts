import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { ICorporate, getCorporateIdentifier } from '../corporate.model';

export type EntityResponseType = HttpResponse<ICorporate>;
export type EntityArrayResponseType = HttpResponse<ICorporate[]>;

@Injectable({ providedIn: 'root' })
export class CorporateService {
  public resourceUrl = this.applicationConfigService.getEndpointFor('api/corporates');

  constructor(protected http: HttpClient, private applicationConfigService: ApplicationConfigService) {}

  create(corporate: ICorporate): Observable<EntityResponseType> {
    return this.http.post<ICorporate>(this.resourceUrl, corporate, { observe: 'response' });
  }

  update(corporate: ICorporate): Observable<EntityResponseType> {
    return this.http.put<ICorporate>(`${this.resourceUrl}/${getCorporateIdentifier(corporate) as number}`, corporate, {
      observe: 'response',
    });
  }

  partialUpdate(corporate: ICorporate): Observable<EntityResponseType> {
    return this.http.patch<ICorporate>(`${this.resourceUrl}/${getCorporateIdentifier(corporate) as number}`, corporate, {
      observe: 'response',
    });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<ICorporate>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<ICorporate[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addCorporateToCollectionIfMissing(
    corporateCollection: ICorporate[],
    ...corporatesToCheck: (ICorporate | null | undefined)[]
  ): ICorporate[] {
    const corporates: ICorporate[] = corporatesToCheck.filter(isPresent);
    if (corporates.length > 0) {
      const corporateCollectionIdentifiers = corporateCollection.map(corporateItem => getCorporateIdentifier(corporateItem)!);
      const corporatesToAdd = corporates.filter(corporateItem => {
        const corporateIdentifier = getCorporateIdentifier(corporateItem);
        if (corporateIdentifier == null || corporateCollectionIdentifiers.includes(corporateIdentifier)) {
          return false;
        }
        corporateCollectionIdentifiers.push(corporateIdentifier);
        return true;
      });
      return [...corporatesToAdd, ...corporateCollection];
    }
    return corporateCollection;
  }
}

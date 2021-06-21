import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IServicePackageType, getServicePackageTypeIdentifier } from '../service-package-type.model';

export type EntityResponseType = HttpResponse<IServicePackageType>;
export type EntityArrayResponseType = HttpResponse<IServicePackageType[]>;

@Injectable({ providedIn: 'root' })
export class ServicePackageTypeService {
  public resourceUrl = this.applicationConfigService.getEndpointFor('api/service-package-types');

  constructor(protected http: HttpClient, private applicationConfigService: ApplicationConfigService) {}

  create(servicePackageType: IServicePackageType): Observable<EntityResponseType> {
    return this.http.post<IServicePackageType>(this.resourceUrl, servicePackageType, { observe: 'response' });
  }

  update(servicePackageType: IServicePackageType): Observable<EntityResponseType> {
    return this.http.put<IServicePackageType>(
      `${this.resourceUrl}/${getServicePackageTypeIdentifier(servicePackageType) as number}`,
      servicePackageType,
      { observe: 'response' }
    );
  }

  partialUpdate(servicePackageType: IServicePackageType): Observable<EntityResponseType> {
    return this.http.patch<IServicePackageType>(
      `${this.resourceUrl}/${getServicePackageTypeIdentifier(servicePackageType) as number}`,
      servicePackageType,
      { observe: 'response' }
    );
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IServicePackageType>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IServicePackageType[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addServicePackageTypeToCollectionIfMissing(
    servicePackageTypeCollection: IServicePackageType[],
    ...servicePackageTypesToCheck: (IServicePackageType | null | undefined)[]
  ): IServicePackageType[] {
    const servicePackageTypes: IServicePackageType[] = servicePackageTypesToCheck.filter(isPresent);
    if (servicePackageTypes.length > 0) {
      const servicePackageTypeCollectionIdentifiers = servicePackageTypeCollection.map(
        servicePackageTypeItem => getServicePackageTypeIdentifier(servicePackageTypeItem)!
      );
      const servicePackageTypesToAdd = servicePackageTypes.filter(servicePackageTypeItem => {
        const servicePackageTypeIdentifier = getServicePackageTypeIdentifier(servicePackageTypeItem);
        if (servicePackageTypeIdentifier == null || servicePackageTypeCollectionIdentifiers.includes(servicePackageTypeIdentifier)) {
          return false;
        }
        servicePackageTypeCollectionIdentifiers.push(servicePackageTypeIdentifier);
        return true;
      });
      return [...servicePackageTypesToAdd, ...servicePackageTypeCollection];
    }
    return servicePackageTypeCollection;
  }
}

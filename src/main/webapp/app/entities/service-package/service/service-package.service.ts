import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IServicePackage, getServicePackageIdentifier } from '../service-package.model';

export type EntityResponseType = HttpResponse<IServicePackage>;
export type EntityArrayResponseType = HttpResponse<IServicePackage[]>;

@Injectable({ providedIn: 'root' })
export class ServicePackageService {
  public resourceUrl = this.applicationConfigService.getEndpointFor('api/service-packages');

  constructor(protected http: HttpClient, private applicationConfigService: ApplicationConfigService) {}

  create(servicePackage: IServicePackage): Observable<EntityResponseType> {
    return this.http.post<IServicePackage>(this.resourceUrl, servicePackage, { observe: 'response' });
  }

  update(servicePackage: IServicePackage): Observable<EntityResponseType> {
    return this.http.put<IServicePackage>(`${this.resourceUrl}/${getServicePackageIdentifier(servicePackage) as number}`, servicePackage, {
      observe: 'response',
    });
  }

  partialUpdate(servicePackage: IServicePackage): Observable<EntityResponseType> {
    return this.http.patch<IServicePackage>(
      `${this.resourceUrl}/${getServicePackageIdentifier(servicePackage) as number}`,
      servicePackage,
      { observe: 'response' }
    );
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IServicePackage>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IServicePackage[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addServicePackageToCollectionIfMissing(
    servicePackageCollection: IServicePackage[],
    ...servicePackagesToCheck: (IServicePackage | null | undefined)[]
  ): IServicePackage[] {
    const servicePackages: IServicePackage[] = servicePackagesToCheck.filter(isPresent);
    if (servicePackages.length > 0) {
      const servicePackageCollectionIdentifiers = servicePackageCollection.map(
        servicePackageItem => getServicePackageIdentifier(servicePackageItem)!
      );
      const servicePackagesToAdd = servicePackages.filter(servicePackageItem => {
        const servicePackageIdentifier = getServicePackageIdentifier(servicePackageItem);
        if (servicePackageIdentifier == null || servicePackageCollectionIdentifiers.includes(servicePackageIdentifier)) {
          return false;
        }
        servicePackageCollectionIdentifiers.push(servicePackageIdentifier);
        return true;
      });
      return [...servicePackagesToAdd, ...servicePackageCollection];
    }
    return servicePackageCollection;
  }
}

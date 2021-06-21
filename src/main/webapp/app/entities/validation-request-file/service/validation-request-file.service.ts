import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IValidationRequestFile, getValidationRequestFileIdentifier } from '../validation-request-file.model';

export type EntityResponseType = HttpResponse<IValidationRequestFile>;
export type EntityArrayResponseType = HttpResponse<IValidationRequestFile[]>;

@Injectable({ providedIn: 'root' })
export class ValidationRequestFileService {
  public resourceUrl = this.applicationConfigService.getEndpointFor('api/validation-request-files');

  constructor(protected http: HttpClient, private applicationConfigService: ApplicationConfigService) {}

  create(validationRequestFile: IValidationRequestFile): Observable<EntityResponseType> {
    return this.http.post<IValidationRequestFile>(this.resourceUrl, validationRequestFile, { observe: 'response' });
  }

  update(validationRequestFile: IValidationRequestFile): Observable<EntityResponseType> {
    return this.http.put<IValidationRequestFile>(
      `${this.resourceUrl}/${getValidationRequestFileIdentifier(validationRequestFile) as number}`,
      validationRequestFile,
      { observe: 'response' }
    );
  }

  partialUpdate(validationRequestFile: IValidationRequestFile): Observable<EntityResponseType> {
    return this.http.patch<IValidationRequestFile>(
      `${this.resourceUrl}/${getValidationRequestFileIdentifier(validationRequestFile) as number}`,
      validationRequestFile,
      { observe: 'response' }
    );
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IValidationRequestFile>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IValidationRequestFile[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addValidationRequestFileToCollectionIfMissing(
    validationRequestFileCollection: IValidationRequestFile[],
    ...validationRequestFilesToCheck: (IValidationRequestFile | null | undefined)[]
  ): IValidationRequestFile[] {
    const validationRequestFiles: IValidationRequestFile[] = validationRequestFilesToCheck.filter(isPresent);
    if (validationRequestFiles.length > 0) {
      const validationRequestFileCollectionIdentifiers = validationRequestFileCollection.map(
        validationRequestFileItem => getValidationRequestFileIdentifier(validationRequestFileItem)!
      );
      const validationRequestFilesToAdd = validationRequestFiles.filter(validationRequestFileItem => {
        const validationRequestFileIdentifier = getValidationRequestFileIdentifier(validationRequestFileItem);
        if (
          validationRequestFileIdentifier == null ||
          validationRequestFileCollectionIdentifiers.includes(validationRequestFileIdentifier)
        ) {
          return false;
        }
        validationRequestFileCollectionIdentifiers.push(validationRequestFileIdentifier);
        return true;
      });
      return [...validationRequestFilesToAdd, ...validationRequestFileCollection];
    }
    return validationRequestFileCollection;
  }
}

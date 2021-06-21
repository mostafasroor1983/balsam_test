import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import * as dayjs from 'dayjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IValidationRequest, getValidationRequestIdentifier } from '../validation-request.model';

export type EntityResponseType = HttpResponse<IValidationRequest>;
export type EntityArrayResponseType = HttpResponse<IValidationRequest[]>;

@Injectable({ providedIn: 'root' })
export class ValidationRequestService {
  public resourceUrl = this.applicationConfigService.getEndpointFor('api/validation-requests');

  constructor(protected http: HttpClient, private applicationConfigService: ApplicationConfigService) {}

  create(validationRequest: IValidationRequest): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(validationRequest);
    return this.http
      .post<IValidationRequest>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  update(validationRequest: IValidationRequest): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(validationRequest);
    return this.http
      .put<IValidationRequest>(`${this.resourceUrl}/${getValidationRequestIdentifier(validationRequest) as number}`, copy, {
        observe: 'response',
      })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  partialUpdate(validationRequest: IValidationRequest): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(validationRequest);
    return this.http
      .patch<IValidationRequest>(`${this.resourceUrl}/${getValidationRequestIdentifier(validationRequest) as number}`, copy, {
        observe: 'response',
      })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<IValidationRequest>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<IValidationRequest[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addValidationRequestToCollectionIfMissing(
    validationRequestCollection: IValidationRequest[],
    ...validationRequestsToCheck: (IValidationRequest | null | undefined)[]
  ): IValidationRequest[] {
    const validationRequests: IValidationRequest[] = validationRequestsToCheck.filter(isPresent);
    if (validationRequests.length > 0) {
      const validationRequestCollectionIdentifiers = validationRequestCollection.map(
        validationRequestItem => getValidationRequestIdentifier(validationRequestItem)!
      );
      const validationRequestsToAdd = validationRequests.filter(validationRequestItem => {
        const validationRequestIdentifier = getValidationRequestIdentifier(validationRequestItem);
        if (validationRequestIdentifier == null || validationRequestCollectionIdentifiers.includes(validationRequestIdentifier)) {
          return false;
        }
        validationRequestCollectionIdentifiers.push(validationRequestIdentifier);
        return true;
      });
      return [...validationRequestsToAdd, ...validationRequestCollection];
    }
    return validationRequestCollection;
  }

  protected convertDateFromClient(validationRequest: IValidationRequest): IValidationRequest {
    return Object.assign({}, validationRequest, {
      actionDateTime: validationRequest.actionDateTime?.isValid() ? validationRequest.actionDateTime.toJSON() : undefined,
    });
  }

  protected convertDateFromServer(res: EntityResponseType): EntityResponseType {
    if (res.body) {
      res.body.actionDateTime = res.body.actionDateTime ? dayjs(res.body.actionDateTime) : undefined;
    }
    return res;
  }

  protected convertDateArrayFromServer(res: EntityArrayResponseType): EntityArrayResponseType {
    if (res.body) {
      res.body.forEach((validationRequest: IValidationRequest) => {
        validationRequest.actionDateTime = validationRequest.actionDateTime ? dayjs(validationRequest.actionDateTime) : undefined;
      });
    }
    return res;
  }
}

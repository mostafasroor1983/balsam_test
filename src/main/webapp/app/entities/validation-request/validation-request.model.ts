import * as dayjs from 'dayjs';
import { IUser } from 'app/entities/user/user.model';
import { IValidationRequestFile } from 'app/entities/validation-request-file/validation-request-file.model';
import { ValidationRequestStatus } from 'app/entities/enumerations/validation-request-status.model';

export interface IValidationRequest {
  id?: number;
  status?: ValidationRequestStatus | null;
  actionDateTime?: dayjs.Dayjs | null;
  reason?: string | null;
  notes?: string | null;
  user?: IUser | null;
  createdBy?: IUser | null;
  acceptedBy?: IUser | null;
  files?: IValidationRequestFile[] | null;
}

export class ValidationRequest implements IValidationRequest {
  constructor(
    public id?: number,
    public status?: ValidationRequestStatus | null,
    public actionDateTime?: dayjs.Dayjs | null,
    public reason?: string | null,
    public notes?: string | null,
    public user?: IUser | null,
    public createdBy?: IUser | null,
    public acceptedBy?: IUser | null,
    public files?: IValidationRequestFile[] | null
  ) {}
}

export function getValidationRequestIdentifier(validationRequest: IValidationRequest): number | undefined {
  return validationRequest.id;
}

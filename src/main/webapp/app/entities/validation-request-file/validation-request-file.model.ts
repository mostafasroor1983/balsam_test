import { IValidationRequest } from 'app/entities/validation-request/validation-request.model';
import { ValidationRequestFileType } from 'app/entities/enumerations/validation-request-file-type.model';

export interface IValidationRequestFile {
  id?: number;
  name?: string;
  fileContentType?: string | null;
  file?: string | null;
  type?: ValidationRequestFileType | null;
  validationRequest?: IValidationRequest | null;
}

export class ValidationRequestFile implements IValidationRequestFile {
  constructor(
    public id?: number,
    public name?: string,
    public fileContentType?: string | null,
    public file?: string | null,
    public type?: ValidationRequestFileType | null,
    public validationRequest?: IValidationRequest | null
  ) {}
}

export function getValidationRequestFileIdentifier(validationRequestFile: IValidationRequestFile): number | undefined {
  return validationRequestFile.id;
}

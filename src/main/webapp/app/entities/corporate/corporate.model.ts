import { ICountry } from 'app/entities/country/country.model';
import { EmployeeSize } from 'app/entities/enumerations/employee-size.model';
import { ClientSize } from 'app/entities/enumerations/client-size.model';

export interface ICorporate {
  id?: number;
  code?: string;
  name?: string;
  description?: string | null;
  logoContentType?: string;
  logo?: string;
  contactPerson?: string | null;
  employeeSize?: EmployeeSize;
  clientSize?: ClientSize;
  email?: string | null;
  website?: string | null;
  country?: ICountry | null;
}

export class Corporate implements ICorporate {
  constructor(
    public id?: number,
    public code?: string,
    public name?: string,
    public description?: string | null,
    public logoContentType?: string,
    public logo?: string,
    public contactPerson?: string | null,
    public employeeSize?: EmployeeSize,
    public clientSize?: ClientSize,
    public email?: string | null,
    public website?: string | null,
    public country?: ICountry | null
  ) {}
}

export function getCorporateIdentifier(corporate: ICorporate): number | undefined {
  return corporate.id;
}

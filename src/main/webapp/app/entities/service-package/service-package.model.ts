import { ICountry } from 'app/entities/country/country.model';
import { IServicePackageType } from 'app/entities/service-package-type/service-package-type.model';

export interface IServicePackage {
  id?: number;
  name?: string;
  recommended?: boolean | null;
  tagName?: string | null;
  country?: ICountry | null;
  packageType?: IServicePackageType | null;
}

export class ServicePackage implements IServicePackage {
  constructor(
    public id?: number,
    public name?: string,
    public recommended?: boolean | null,
    public tagName?: string | null,
    public country?: ICountry | null,
    public packageType?: IServicePackageType | null
  ) {
    this.recommended = this.recommended ?? false;
  }
}

export function getServicePackageIdentifier(servicePackage: IServicePackage): number | undefined {
  return servicePackage.id;
}

import { IServicePackage } from 'app/entities/service-package/service-package.model';

export interface IServicePackageType {
  id?: number;
  name?: string;
  packages?: IServicePackage[] | null;
}

export class ServicePackageType implements IServicePackageType {
  constructor(public id?: number, public name?: string, public packages?: IServicePackage[] | null) {}
}

export function getServicePackageTypeIdentifier(servicePackageType: IServicePackageType): number | undefined {
  return servicePackageType.id;
}

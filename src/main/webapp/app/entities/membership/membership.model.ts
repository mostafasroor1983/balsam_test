import * as dayjs from 'dayjs';
import { IServicePackage } from 'app/entities/service-package/service-package.model';
import { ICorporate } from 'app/entities/corporate/corporate.model';
import { IUser } from 'app/entities/user/user.model';
import { MemberType } from 'app/entities/enumerations/member-type.model';

export interface IMembership {
  id?: number;
  membershipId?: string;
  memberType?: MemberType | null;
  active?: boolean | null;
  hasPhysicalVersion?: boolean | null;
  memberShare?: string | null;
  corporateShare?: string | null;
  printingDateTime?: dayjs.Dayjs | null;
  servicePackage?: IServicePackage | null;
  corporate?: ICorporate | null;
  user?: IUser | null;
}

export class Membership implements IMembership {
  constructor(
    public id?: number,
    public membershipId?: string,
    public memberType?: MemberType | null,
    public active?: boolean | null,
    public hasPhysicalVersion?: boolean | null,
    public memberShare?: string | null,
    public corporateShare?: string | null,
    public printingDateTime?: dayjs.Dayjs | null,
    public servicePackage?: IServicePackage | null,
    public corporate?: ICorporate | null,
    public user?: IUser | null
  ) {
    this.active = this.active ?? false;
    this.hasPhysicalVersion = this.hasPhysicalVersion ?? false;
  }
}

export function getMembershipIdentifier(membership: IMembership): number | undefined {
  return membership.id;
}

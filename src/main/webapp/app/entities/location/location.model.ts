export interface ILocation {
  id?: number;
  streetAddressLine1?: string | null;
  streetAddressLine2?: string | null;
  latitude?: string | null;
  longitude?: string | null;
}

export class Location implements ILocation {
  constructor(
    public id?: number,
    public streetAddressLine1?: string | null,
    public streetAddressLine2?: string | null,
    public latitude?: string | null,
    public longitude?: string | null
  ) {}
}

export function getLocationIdentifier(location: ILocation): number | undefined {
  return location.id;
}

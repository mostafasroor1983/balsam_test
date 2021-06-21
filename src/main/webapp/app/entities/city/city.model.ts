import { ICountry } from 'app/entities/country/country.model';

export interface ICity {
  id?: number;
  name?: string;
  code?: string | null;
  country?: ICountry | null;
}

export class City implements ICity {
  constructor(public id?: number, public name?: string, public code?: string | null, public country?: ICountry | null) {}
}

export function getCityIdentifier(city: ICity): number | undefined {
  return city.id;
}

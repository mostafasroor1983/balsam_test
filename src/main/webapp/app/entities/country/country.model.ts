import { ICity } from 'app/entities/city/city.model';
import { ICorporate } from 'app/entities/corporate/corporate.model';

export interface ICountry {
  id?: number;
  name?: string;
  code?: string | null;
  phoneCode?: string | null;
  membershipCode?: string | null;
  cities?: ICity[] | null;
  corporates?: ICorporate[] | null;
}

export class Country implements ICountry {
  constructor(
    public id?: number,
    public name?: string,
    public code?: string | null,
    public phoneCode?: string | null,
    public membershipCode?: string | null,
    public cities?: ICity[] | null,
    public corporates?: ICorporate[] | null
  ) {}
}

export function getCountryIdentifier(country: ICountry): number | undefined {
  return country.id;
}

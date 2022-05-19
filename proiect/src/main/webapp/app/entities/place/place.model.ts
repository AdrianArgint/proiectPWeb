export interface IPlace {
  id?: number;
  name?: string | null;
  latitude?: number | null;
  longitude?: number | null;
  address?: string | null;
  type?: string | null;
  description?: string | null;
}

export class Place implements IPlace {
  constructor(
    public id?: number,
    public name?: string | null,
    public latitude?: number | null,
    public longitude?: number | null,
    public address?: string | null,
    public type?: string | null,
    public description?: string | null
  ) {}
}

export function getPlaceIdentifier(place: IPlace): number | undefined {
  return place.id;
}

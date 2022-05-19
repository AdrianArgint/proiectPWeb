import { IPlace } from '../place/place.model';

export interface IUser {
  id?: string;
  login?: string;
  firstName?: string;
  lastName?: string;
  imageUrl?: string;
  email?: string;
  place?: IPlace;
}

export class User implements IUser {
  constructor(
    public id: string,
    public login: string,
    public firstName: string,
    public lastName: string,
    public imageUrl: string,
    public email?: string,
    public place?: IPlace
  ) {}
}

export function getUserIdentifier(user: IUser): string | undefined {
  return user.id;
}

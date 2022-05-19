import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IPlace, getPlaceIdentifier } from '../place.model';

export type EntityResponseType = HttpResponse<IPlace>;
export type EntityArrayResponseType = HttpResponse<IPlace[]>;

@Injectable({ providedIn: 'root' })
export class PlaceService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/places');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(place: IPlace): Observable<EntityResponseType> {
    return this.http.post<IPlace>(this.resourceUrl, place, { observe: 'response' });
  }

  update(place: IPlace): Observable<EntityResponseType> {
    return this.http.put<IPlace>(`${this.resourceUrl}/${getPlaceIdentifier(place) as number}`, place, { observe: 'response' });
  }

  partialUpdate(place: IPlace): Observable<EntityResponseType> {
    return this.http.patch<IPlace>(`${this.resourceUrl}/${getPlaceIdentifier(place) as number}`, place, { observe: 'response' });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IPlace>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IPlace[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addPlaceToCollectionIfMissing(placeCollection: IPlace[], ...placesToCheck: (IPlace | null | undefined)[]): IPlace[] {
    const places: IPlace[] = placesToCheck.filter(isPresent);
    if (places.length > 0) {
      const placeCollectionIdentifiers = placeCollection.map(placeItem => getPlaceIdentifier(placeItem)!);
      const placesToAdd = places.filter(placeItem => {
        const placeIdentifier = getPlaceIdentifier(placeItem);
        if (placeIdentifier == null || placeCollectionIdentifiers.includes(placeIdentifier)) {
          return false;
        }
        placeCollectionIdentifiers.push(placeIdentifier);
        return true;
      });
      return [...placesToAdd, ...placeCollection];
    }
    return placeCollection;
  }
}

import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import { IAlert } from '../alert.model';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { ApplicationConfigService } from '../../../core/config/application-config.service';
import dayjs from 'dayjs/esm';
import { createRequestOption } from '../../../core/request/request-util';
import { IPost } from '../../post/post.model';

export type EntityResponseType = HttpResponse<IAlert>;
export type EntityArrayResponseType = HttpResponse<IAlert[]>;

@Injectable({
  providedIn: 'root',
})
export class AlertService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/alert');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(announcement: IAlert): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(announcement);
    console.warn(this.resourceUrl);
    return this.http
      .post<IAlert>(`${this.resourceUrl}/send`, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  convertDateFromClient(announcement: IAlert): IAlert {
    return Object.assign({}, announcement, {
      createdDate: announcement.createdDate?.isValid() ? announcement.createdDate.toJSON() : undefined,
    });
  }

  convertDateFromServer(res: EntityResponseType): EntityResponseType {
    if (res.body) {
      res.body.createdDate = res.body.createdDate ? dayjs(res.body.createdDate) : undefined;
    }
    return res;
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<IAlert>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IPost[]>(this.resourceUrl, { params: options, observe: 'response' });
  }
}

import { Injectable } from '@angular/core';
import { ActivatedRouteSnapshot, Resolve, Router } from '@angular/router';
import { EMPTY, Observable, of } from 'rxjs';
import { Alert, IAlert } from '../alert.model';
import { AlertService } from '../service/alert.service';
import { mergeMap } from 'rxjs/operators';
import { HttpResponse } from '@angular/common/http';
import { Post } from '../../post/post.model';

@Injectable({
  providedIn: 'root',
})
export class OfficialAnnouncementResolver implements Resolve<IAlert> {
  constructor(protected service: AlertService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IAlert> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((post: HttpResponse<Alert>) => {
          if (post.body) {
            return of(post.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new Post());
  }
}

import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ShelterPlaceComponent } from '../shelter-list/shelter-place.component';
import { PlaceDetailComponent } from '../detail/place-detail.component';
import { PlaceUpdateComponent } from '../update/place-update.component';
import { PlaceRoutingResolveService } from './place-routing-resolve.service';
import { DangerPlaceComponent } from '../danger-place/danger-place.component';
import { HelpPlaceComponent } from '../help-place/help-place.component';
import { PlacesMapComponent } from '../places-map/places-map.component';

const placeRoute: Routes = [
  {
    path: 'shelter',
    component: ShelterPlaceComponent,
    data: {
      defaultSort: 'id,asc',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'danger',
    component: DangerPlaceComponent,
    data: {
      defaultSort: 'id,asc',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'help',
    component: HelpPlaceComponent,
    data: {
      defaultSort: 'id,asc',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: PlaceDetailComponent,
    resolve: {
      place: PlaceRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: PlaceUpdateComponent,
    resolve: {
      place: PlaceRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: PlaceUpdateComponent,
    resolve: {
      place: PlaceRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'map',
    component: PlacesMapComponent,
    resolve: {
      place: PlaceRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(placeRoute)],
  exports: [RouterModule],
})
export class PlaceRoutingModule {}

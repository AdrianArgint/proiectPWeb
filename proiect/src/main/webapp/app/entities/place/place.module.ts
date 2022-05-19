import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { ShelterPlaceComponent } from './shelter-list/shelter-place.component';
import { PlaceDetailComponent } from './detail/place-detail.component';
import { PlaceUpdateComponent } from './update/place-update.component';
import { PlaceDeleteDialogComponent } from './delete/place-delete-dialog.component';
import { PlaceRoutingModule } from './route/place-routing.module';
import { GooglePlaceModule } from 'ngx-google-places-autocomplete';
import { MatSelectModule } from '@angular/material/select';
import { MatInputModule } from '@angular/material/input';
import { DangerPlaceComponent } from './danger-place/danger-place.component';
import { HelpPlaceComponent } from './help-place/help-place.component';
import { PlacesMapComponent } from './places-map/places-map.component';
import { AgmCoreModule } from '@agm/core';
import { OverviewDialogComponent } from './help-place/overview-dialog.component';
import { MatDialogModule } from '@angular/material/dialog';
import { MatButtonModule } from '@angular/material/button';

@NgModule({
  imports: [
    SharedModule,
    PlaceRoutingModule,
    GooglePlaceModule,
    MatSelectModule,
    MatInputModule,
    AgmCoreModule,
    MatDialogModule,
    MatButtonModule,
  ],
  declarations: [
    ShelterPlaceComponent,
    PlaceDetailComponent,
    PlaceUpdateComponent,
    PlaceDeleteDialogComponent,
    DangerPlaceComponent,
    HelpPlaceComponent,
    PlacesMapComponent,
    OverviewDialogComponent,
  ],
  entryComponents: [PlaceDeleteDialogComponent],
})
export class PlaceModule {}

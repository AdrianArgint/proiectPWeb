import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

import { SharedModule } from 'app/shared/shared.module';
import { HOME_ROUTE, PROFILE_ROUTE } from './home.route';
import { HomeComponent } from './home.component';
import {PlaceModule} from "../entities/place/place.module";

@NgModule({
  imports: [SharedModule, RouterModule.forChild([HOME_ROUTE, PROFILE_ROUTE]), PlaceModule],
  declarations: [HomeComponent],
})
export class HomeModule {}

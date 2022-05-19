import { Route } from '@angular/router';

import { HomeComponent } from './home.component';
import { ProfileComponent } from '../profile/profile.component';

export const HOME_ROUTE: Route = {
  path: '',
  component: HomeComponent,
  data: {
    pageTitle: 'Welcome, Java Hipster!',
  },
};

export const PROFILE_ROUTE: Route = {
  path: 'my-profile',
  component: ProfileComponent,
  data: {
    pageTitle: 'My account  ',
  },
};

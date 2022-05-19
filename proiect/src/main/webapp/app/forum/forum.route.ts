import { Route } from '@angular/router';
import { ForumPageComponent } from './forum-page/forum-page.component';

export const FORUM_ROUTE: Route = {
  path: 'forum',
  component: ForumPageComponent,
  data: {
    pageTitle: 'Welcome, Java Hipster!',
  },
};

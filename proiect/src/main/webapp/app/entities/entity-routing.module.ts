import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

@NgModule({
  imports: [
    RouterModule.forChild([
      {
        path: 'post',
        data: { pageTitle: 'Posts' },
        loadChildren: () => import('./post/post.module').then(m => m.PostModule),
      },
      {
        path: 'post-comment',
        data: { pageTitle: 'PostComments' },
        loadChildren: () => import('./post-comment/post-comment.module').then(m => m.PostCommentModule),
      },
      {
        path: 'place',
        data: { pageTitle: 'Places' },
        loadChildren: () => import('./place/place.module').then(m => m.PlaceModule),
      },
      {
        path: 'alert',
        data: { pageTitle: 'Alerts' },
        loadChildren: () => import('./official-announcement/official-announcement.module').then(m => m.OfficialAnnouncementModule),
      },
      /* jhipster-needle-add-entity-route - JHipster will add entity modules routes here */
    ]),
  ],
})
export class EntityRoutingModule {}

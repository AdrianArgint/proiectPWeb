import { NgModule } from '@angular/core';
import { PostComponent } from './post/post.component';
import { ForumPageComponent } from './forum-page/forum-page.component';
import { RouterModule } from '@angular/router';
import { SharedModule } from '../shared/shared.module';
import { FORUM_ROUTE } from './forum.route';
import { MatCardModule } from '@angular/material/card';
import { MatButtonModule } from '@angular/material/button';
import { MatDividerModule } from '@angular/material/divider';
import { PostCommentComponent } from './post-comment/post-comment.component';
import { MatInputModule } from '@angular/material/input';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { FlexModule } from '@angular/flex-layout';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { MatIconModule } from '@angular/material/icon';
import { SortPipe } from './sortPipe/sort.pipe';
import { SortPostsPipe } from './sortPostsPipe/sort-posts.pipe';

@NgModule({
  declarations: [PostComponent, ForumPageComponent, PostCommentComponent, SortPipe, SortPostsPipe],
  imports: [
    SharedModule,
    BrowserAnimationsModule,
    RouterModule.forChild([FORUM_ROUTE]),
    MatCardModule,
    MatButtonModule,
    MatDividerModule,
    MatInputModule,
    FlexModule,
    ReactiveFormsModule,
    FormsModule,
    MatIconModule,
  ],
  providers: [SortPostsPipe],
})
export class ForumModule {}

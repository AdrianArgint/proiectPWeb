import { Pipe, PipeTransform } from '@angular/core';
import { IPostComment } from '../../entities/post-comment/post-comment.model';
import { IPost } from '../../entities/post/post.model';

@Pipe({
  name: 'sortPosts',
})
export class SortPostsPipe implements PipeTransform {
  transform(commentsList: IPost[], ...args: unknown[]): IPost[] {
    commentsList.sort((a: IPostComment, b: IPostComment) => {
      if (a.createdDate! > b.createdDate!) {
        return -1;
      } else if (a.createdDate! < b.createdDate!) {
        return 1;
      } else {
        return 0;
      }
    });
    return commentsList;
  }
}

import { Pipe, PipeTransform } from '@angular/core';
import { IPostComment } from '../../entities/post-comment/post-comment.model';

@Pipe({
  name: 'sort',
})
export class SortPipe implements PipeTransform {
  transform(commentsList: IPostComment[]): IPostComment[] {
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

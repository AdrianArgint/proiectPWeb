import { Component, EventEmitter, Input, Output } from '@angular/core';
import { IPostComment } from '../../entities/post-comment/post-comment.model';
import { Account } from '../../core/auth/account.model';

@Component({
  selector: 'jhi-post-comment',
  templateUrl: './post-comment.component.html',
  styleUrls: ['./post-comment.component.scss'],
})
export class PostCommentComponent {
  @Input() comment?: IPostComment;

  @Input() loggedUser?: Account;

  @Output() sendId = new EventEmitter<number>();

  constructor() {
    // empty
  }

  sendIdToParent(): void {
    this.sendId.emit(this.comment?.id);
  }
}

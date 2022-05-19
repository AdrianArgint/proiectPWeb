import { Component, EventEmitter, Input, OnInit, Output } from '@angular/core';
import { IPost } from '../../entities/post/post.model';
import { PostService } from '../../entities/post/service/post.service';
import { FormControl } from '@angular/forms';
import { PostCommentService } from '../../entities/post-comment/service/post-comment.service';
import { PostComment } from '../../entities/post-comment/post-comment.model';
import { AccountService } from '../../core/auth/account.service';
import { UserService } from '../../entities/user/user.service';
import { User } from '../../entities/user/user.model';
import { Account } from '../../core/auth/account.model';
import dayjs from 'dayjs/esm';

@Component({
  selector: 'jhi-post',
  templateUrl: './post.component.html',
  styleUrls: ['./post.component.scss'],
})
export class PostComponent implements OnInit {
  @Input() loggedUser: Account | undefined | null;

  @Input() post: IPost | undefined;

  @Output() postId = new EventEmitter<number>();

  auxPost: IPost | undefined;
  reply = false;
  newComment: FormControl = new FormControl('');

  constructor(
    public postService: PostService,
    public postCommentService: PostCommentService,
    public accountService: AccountService,
    public userService: UserService
  ) {
    // empty
  }

  ngOnInit(): void {
    this.auxPost = this.post;
  }

  sendPostIdToParent(): void {
    console.warn(this.post?.id);
    this.postId.emit(this.post?.id);
  }

  replyToPost(): void {
    this.reply = !this.reply;
  }

  updatePost(): void {
    console.warn(this.newComment.value);
    if (this.auxPost && this.loggedUser) {
      const user = new User(
        this.loggedUser.id!,
        this.loggedUser.login,
        this.loggedUser.firstName!,
        this.loggedUser.lastName!,
        this.loggedUser.imageUrl!
      );
      const newComm = {
        ...new PostComment(),
        content: this.newComment.value,
        post: this.post,
        author: user,
        createdDate: dayjs(),
        lastUpdatedDate: dayjs(),
      };
      this.postCommentService.create(newComm).subscribe(savedComment => {
        const postToAdd: IPost = { ...this.post };
        postToAdd.postComments?.push(savedComment.body!);
        this.postService.update(postToAdd).subscribe(() => {
          this.reply = false;
          this.newComment.reset();
        });
      });
    }
  }

  deleteComment(commentId: number): void {
    this.postCommentService.delete(commentId).subscribe(() => {
      this.post!.postComments = this.post?.postComments?.filter(post => post.id !== commentId);
      console.warn('succes deleted');
    });
  }
}

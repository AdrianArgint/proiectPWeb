import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import dayjs from 'dayjs/esm';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';

import { IPostComment, PostComment } from '../post-comment.model';
import { PostCommentService } from '../service/post-comment.service';
import { IPost } from 'app/entities/post/post.model';
import { PostService } from 'app/entities/post/service/post.service';
import { IUser } from 'app/entities/user/user.model';
import { UserService } from 'app/entities/user/user.service';

@Component({
  selector: 'jhi-post-comment-update',
  templateUrl: './post-comment-update.component.html',
})
export class PostCommentUpdateComponent implements OnInit {
  isSaving = false;

  postsSharedCollection: IPost[] = [];
  usersSharedCollection: IUser[] = [];

  editForm = this.fb.group({
    id: [],
    content: [],
    createdDate: [],
    lastUpdatedDate: [],
    post: [],
    author: [],
  });

  constructor(
    protected postCommentService: PostCommentService,
    protected postService: PostService,
    protected userService: UserService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ postComment }) => {
      if (postComment.id === undefined) {
        const today = dayjs().startOf('day');
        postComment.createdDate = today;
        postComment.lastUpdatedDate = today;
      }

      this.updateForm(postComment);

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const postComment = this.createFromForm();
    if (postComment.id !== undefined) {
      this.subscribeToSaveResponse(this.postCommentService.update(postComment));
    } else {
      this.subscribeToSaveResponse(this.postCommentService.create(postComment));
    }
  }

  trackPostById(index: number, item: IPost): number {
    return item.id!;
  }

  trackUserById(index: number, item: IUser): string {
    return item.id!;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IPostComment>>): void {
    result.pipe(finalize(() => this.onSaveFinalize())).subscribe({
      next: () => this.onSaveSuccess(),
      error: () => this.onSaveError(),
    });
  }

  protected onSaveSuccess(): void {
    this.previousState();
  }

  protected onSaveError(): void {
    // Api for inheritance.
  }

  protected onSaveFinalize(): void {
    this.isSaving = false;
  }

  protected updateForm(postComment: IPostComment): void {
    this.editForm.patchValue({
      id: postComment.id,
      content: postComment.content,
      createdDate: postComment.createdDate ? postComment.createdDate.format(DATE_TIME_FORMAT) : null,
      lastUpdatedDate: postComment.lastUpdatedDate ? postComment.lastUpdatedDate.format(DATE_TIME_FORMAT) : null,
      post: postComment.post,
      author: postComment.author,
    });

    this.postsSharedCollection = this.postService.addPostToCollectionIfMissing(this.postsSharedCollection, postComment.post);
    this.usersSharedCollection = this.userService.addUserToCollectionIfMissing(this.usersSharedCollection, postComment.author);
  }

  protected loadRelationshipsOptions(): void {
    this.postService
      .query()
      .pipe(map((res: HttpResponse<IPost[]>) => res.body ?? []))
      .pipe(map((posts: IPost[]) => this.postService.addPostToCollectionIfMissing(posts, this.editForm.get('post')!.value)))
      .subscribe((posts: IPost[]) => (this.postsSharedCollection = posts));

    this.userService
      .query()
      .pipe(map((res: HttpResponse<IUser[]>) => res.body ?? []))
      .pipe(map((users: IUser[]) => this.userService.addUserToCollectionIfMissing(users, this.editForm.get('author')!.value)))
      .subscribe((users: IUser[]) => (this.usersSharedCollection = users));
  }

  protected createFromForm(): IPostComment {
    return {
      ...new PostComment(),
      id: this.editForm.get(['id'])!.value,
      content: this.editForm.get(['content'])!.value,
      createdDate: this.editForm.get(['createdDate'])!.value
        ? dayjs(this.editForm.get(['createdDate'])!.value, DATE_TIME_FORMAT)
        : undefined,
      lastUpdatedDate: this.editForm.get(['lastUpdatedDate'])!.value
        ? dayjs(this.editForm.get(['lastUpdatedDate'])!.value, DATE_TIME_FORMAT)
        : undefined,
      post: this.editForm.get(['post'])!.value,
      author: this.editForm.get(['author'])!.value,
    };
  }
}

import { Component, OnInit } from '@angular/core';
import { PostService } from '../../entities/post/service/post.service';
import { IPost } from '../../entities/post/post.model';
import { AccountService } from '../../core/auth/account.service';
import { Account } from '../../core/auth/account.model';
import { FormControl } from '@angular/forms';
import dayjs from 'dayjs/esm';
import { User } from '../../entities/user/user.model';
import { SortPostsPipe } from '../sortPostsPipe/sort-posts.pipe';

@Component({
  selector: 'jhi-forum-page',
  templateUrl: './forum-page.component.html',
  styleUrls: ['./forum-page.component.scss'],
})
export class ForumPageComponent implements OnInit {
  loggedUser: Account | null | undefined;

  allPosts: IPost[] | null | undefined;

  isOpened = false;

  newTitle: FormControl = new FormControl('');
  newContent: FormControl = new FormControl('');

  date = dayjs();

  constructor(private postService: PostService, public accountService: AccountService, public sortPostPipe: SortPostsPipe) {}

  ngOnInit(): void {
    this.accountService.identity().subscribe(account => {
      this.loggedUser = account;
    });

    this.postService.query().subscribe(res => {
      this.allPosts = res.body;
    });
  }

  openCreatePost(): void {
    this.isOpened = !this.isOpened;
  }

  deletePost(postId: number): void {
    console.warn(postId);
    this.postService.delete(postId).subscribe(() => {
      this.allPosts = this.allPosts!.filter(post => post.id !== postId);

      console.warn('success');
    });
  }

  createPost(): void {
    if (this.loggedUser) {
      const user = new User(
        this.loggedUser.id!,
        this.loggedUser.login,
        this.loggedUser.firstName!,
        this.loggedUser.lastName!,
        this.loggedUser.imageUrl!
      );

      const newPost: IPost = {
        title: this.newTitle.value,
        content: this.newContent.value,
        createdDate: dayjs(),
        lastUpdatedDate: dayjs(),
        author: user,
        postComments: [],
      };
      this.postService.create(newPost).subscribe(res => {
        console.warn('success');
        this.isOpened = false;
        this.allPosts?.push(res.body!);
        this.allPosts = this.sortPostPipe.transform(this.allPosts!);
        this.newContent.reset();
        this.newTitle.reset();
      });
    }
    // this.postService.create();
  }
}

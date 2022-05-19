import dayjs from 'dayjs/esm';
import { IUser } from 'app/entities/user/user.model';
import { IPostComment } from '../post-comment/post-comment.model';

export interface IPost {
  id?: number;
  title?: string | null;
  content?: string | null;
  createdDate?: dayjs.Dayjs | null;
  lastUpdatedDate?: dayjs.Dayjs | null;
  author?: IUser | null;
  postComments?: IPostComment[] | null;
}

export class Post implements IPost {
  constructor(
    public id?: number,
    public title?: string | null,
    public content?: string | null,
    public createdDate?: dayjs.Dayjs | null,
    public lastUpdatedDate?: dayjs.Dayjs | null,
    public author?: IUser | null,
    public postComments?: IPostComment[] | null
  ) {}
}

export function getPostIdentifier(post: IPost): number | undefined {
  return post.id;
}

import dayjs from 'dayjs/esm';
import { IPost } from 'app/entities/post/post.model';
import { IUser } from 'app/entities/user/user.model';

export interface IPostComment {
  id?: number;
  content?: string | null;
  createdDate?: dayjs.Dayjs | null;
  lastUpdatedDate?: dayjs.Dayjs | null;
  post?: IPost | null;
  author?: IUser | null;
}

export class PostComment implements IPostComment {
  constructor(
    public id?: number,
    public content?: string | null,
    public createdDate?: dayjs.Dayjs | null,
    public lastUpdatedDate?: dayjs.Dayjs | null,
    public post?: IPost | null,
    public author?: IUser | null
  ) {}
}

export function getPostCommentIdentifier(postComment: IPostComment): number | undefined {
  return postComment.id;
}

import {User} from "./user";
import {PostType} from "./postType";

export interface Question {
  id: number;
  //votes: Vote[];
  createdByUser: User;
  //answers: Question[];
  postType: PostType;
  postTitleQ: string;
  postContent: string;
  postedDate: Date;
  img?: string;
  statusQ?: string;
  acceptedAnswer?: number;
  //comments: Comment[];
  //postTags: PostTag[];
}

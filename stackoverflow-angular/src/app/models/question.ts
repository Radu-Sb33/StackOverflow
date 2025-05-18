import {User} from "./user";
import {PostType} from "./postType";
import {Answer} from "./answer";

export interface Question {
  id: number;
  //votes: Vote[];
  createdByUser: User|null;
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
  answers?: Answer[];
  createdByUsername?: string;
}

import {User} from "./user";
import {PostType} from "./postType";
import {Question} from "./question";

export interface Answer {
  id: number;
  //votes: Vote[];
  createdByUser?: User;
  parentQuestion: Question;
  postType: PostType;
  postContent: string;
  postedDate: Date;
  img?: string;
  //comments: Comment[];
  //postTags: PostTag[];
  createdByUsername?: string;
}

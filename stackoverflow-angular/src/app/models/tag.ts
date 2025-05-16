import {User} from "./user";


export interface Tag {
  id: number;
  tagName: string;
  tagDescription: string;
  createdByUsername: string;
  //postTags?: PostTag[];
}

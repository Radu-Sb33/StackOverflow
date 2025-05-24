import {Answer} from "./answer";
import {Question} from "./question";
import {Tag} from "./tag";
export interface PostTag{
  id: number,
  post: Answer | Question,
  tag: Tag
}

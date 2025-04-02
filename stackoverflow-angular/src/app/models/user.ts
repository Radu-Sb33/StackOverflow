import {Timestamp} from "rxjs";

export interface User {
   id: number ;
   username: string;
   email: string;
  password: string;
  about: string;
  is_moderator: boolean;
  reputation: number;
  is_banned: boolean;
  img: string;
  creation_date: Timestamp<any>;
}

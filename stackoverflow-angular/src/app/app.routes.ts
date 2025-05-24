import {RouterModule, Routes} from '@angular/router';
import {LoginComponent} from "./auth-comp/login/login.component";
import {SignupComponent} from "./auth-comp/signup/signup.component";
import {QuestionComponent} from "./posts/question/question.component";
import {AnswersComponent} from "./posts/answers/answers.component";
import {NgModule} from "@angular/core";
import {UsersComponent} from "./users/users.component";
import {AddQuestionComponent} from "./add-comp/add-question/add-question.component";
import {HomeComponent} from "./home/home.component";
import {AddAnswerComponent} from "./add-comp/add-answer/add-answer.component";
import {UserProfileComponent} from "./user-profile/user-profile.component";
import {ModeratorGuard} from "./auth-comp/auth/moderator.guard";
import {TagsComponent} from "./tags/tags.component";
import {EditQuestionComponent} from "./edit-comp/edit-question/edit-question.component";
import {EditAnswerComponent} from "./edit-comp/edit-answer/edit-answer.component";

export const routes: Routes = [
  { path: 'login', component: LoginComponent },
  { path: 'signup', component: SignupComponent },
  { path: 'questions', component: QuestionComponent},
  { path: 'answers', component: AnswersComponent},
  { path: 'users', component: UsersComponent, canActivate: [ModeratorGuard] },
  { path: 'add-question', component: AddQuestionComponent},
  { path: 'add-answer', component: AddAnswerComponent},
  { path: 'edit-question/:id', component: EditQuestionComponent},
  { path: 'edit-answer/:id', component: EditAnswerComponent},
  { path: 'home', component: HomeComponent },
  { path: 'user-profile', component: UserProfileComponent},
  { path: 'tags', component: TagsComponent},
  { path: '', redirectTo: 'home', pathMatch: 'full'}
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }

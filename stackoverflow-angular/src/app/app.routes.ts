import {RouterModule, Routes} from '@angular/router';
import {LoginComponent} from "./auth-comp/login/login.component";
import {SignupComponent} from "./auth-comp/signup/signup.component";
import {QuestionComponent} from "./posts/question/question.component";
import {AnswersComponent} from "./posts/answers/answers.component";
import {NgModule} from "@angular/core";
import {UsersComponent} from "./users/users.component";
import {AddQuestionComponent} from "./add-comp/add-question/add-question.component";
import {HomeComponent} from "./home/home.component";

export const routes: Routes = [
  { path: 'login', component: LoginComponent },
  { path: 'signup', component: SignupComponent },
  { path: 'questions', component: QuestionComponent},
  { path: 'answers', component: AnswersComponent},
  { path: 'users', component: UsersComponent},
  { path: 'add-question', component: AddQuestionComponent},
  {path: 'home', component: HomeComponent },
  { path: '', redirectTo: 'home', pathMatch: 'full'}
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }

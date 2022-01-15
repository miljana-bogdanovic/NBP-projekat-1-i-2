import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { HomePageComponent } from './components/home-page/home-page.component';
import { LoginPageComponent } from './components/login-page/login-page.component';
import { ProfilePageComponent } from './components/profile-page/profile-page.component';
import { AuthGuard } from './app-routing.gard';
import { UserListComponent } from './components/user-list/user-list.component';
import { RegistrationPageComponent } from './components/registration-page/registration-page.component';
import { SideBarComponent } from './components/side-bar/side-bar.component';
import { ExplorePageComponent } from './components/explore-page/explore-page.component';


const routes: Routes = [
  { path: 'login', component: LoginPageComponent },
  { path: 'registration', component: RegistrationPageComponent },
  {
    path: 'home',
    canLoad: [AuthGuard],
    canActivate: [AuthGuard],
    component: HomePageComponent,
  },
  {
    path: 'profile/:username',
    canLoad: [AuthGuard],
   canActivate: [AuthGuard],
    component: ProfilePageComponent,
  },
  {
    path: 'profile/:username/following',
    canLoad: [AuthGuard],
   canActivate: [AuthGuard],
    component: UserListComponent,
  },
  {
    path: 'profile/:username/followers',
    canLoad: [AuthGuard],
    canActivate: [AuthGuard],
    component: UserListComponent,
  },
  {
    path: 'explore',
    canLoad: [AuthGuard],
    canActivate: [AuthGuard],
    component: ExplorePageComponent,
  },
  { path: '**', redirectTo: 'home', pathMatch: 'full' },
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule],
})
export class AppRoutingModule {}

import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';

import { AppRoutingModule } from './app-routing.module';
import { RouterModule } from '@angular/router';
import { AppComponent } from './app.component';
import { HomePageComponent } from './components/home-page/home-page.component';
import { ProfilePageComponent } from './components/profile-page/profile-page.component';
import { LoginPageComponent } from './components/login-page/login-page.component';
import { LoginService } from './services/login.service';
import { AuthService } from './services/auth.service';
import { MatCardModule } from '@angular/material/card';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { HttpClientModule, HTTP_INTERCEPTORS } from '@angular/common/http';
import { MatProgressSpinnerModule } from '@angular/material/progress-spinner';
import { MatExpansionModule } from '@angular/material/expansion';
import { MatProgressBarModule } from '@angular/material/progress-bar';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { MatDatepickerModule } from '@angular/material/datepicker';
import { MatDialogModule } from '@angular/material/dialog';
import { MatSelectModule } from '@angular/material/select';
import { MatNativeDateModule } from '@angular/material/core';
import { MatIconModule } from '@angular/material/icon';
import { TweetComponent } from './components/tweet/tweet.component';
import { TweetListComponent } from './components/tweet-list/tweet-list.component';
import { NewTweetComponent } from './components/new-tweet/new-tweet.component';
import { EditProfileComponent } from './components/edit-profile/edit-profile.component';
import { UserListComponent } from './components/user-list/user-list.component';
import { EditTweetComponent } from './components/edit-tweet/edit-tweet.component';
import { RegistrationPageComponent } from './components/registration-page/registration-page.component';
import { SideBarComponent } from './components/side-bar/side-bar.component';
import { AuthInterceptor } from './services/auth-interceptor';
import { SearchPipe } from './directives/serach.pipe';
import { ExplorePageComponent } from './components/explore-page/explore-page.component';
import { SearchTweetPipe } from './directives/search-tweet.pipe';


@NgModule({
  declarations: [
    AppComponent,
    HomePageComponent,
    ProfilePageComponent,
    LoginPageComponent,
    TweetComponent,
    TweetListComponent,
    NewTweetComponent,
    EditProfileComponent,
    UserListComponent,
    EditTweetComponent,
    RegistrationPageComponent,
    SideBarComponent,
    SearchPipe,
    ExplorePageComponent,
    SearchTweetPipe,

  ],
  imports: [
    BrowserModule,
    AppRoutingModule,
    RouterModule,
    MatCardModule,
    ReactiveFormsModule,
    HttpClientModule,
    FormsModule,
    MatProgressSpinnerModule,
    MatExpansionModule,
    MatProgressBarModule,
    MatFormFieldModule,
    MatInputModule,
    MatDatepickerModule,
    MatDialogModule,
    MatSelectModule,
    MatNativeDateModule,
    MatIconModule,
    BrowserAnimationsModule,
  ],
  providers: [LoginService, AuthService, {
     provide : HTTP_INTERCEPTORS,
     useClass : AuthInterceptor,
     multi : true
   }],
  bootstrap: [AppComponent],
})
export class AppModule {}

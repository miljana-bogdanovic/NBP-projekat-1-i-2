import { UniqueSelectionDispatcher } from '@angular/cdk/collections';
import { HttpClient } from '@angular/common/http';
import { EventEmitter, Injectable, Output } from '@angular/core';
import { BrowserDynamicTestingModule } from '@angular/platform-browser-dynamic/testing';
import { environment } from 'src/environments/environment';
import { LoggedUser } from '../models/logged-user.model';
import { Tweet } from '../models/tweet.model';
import { User } from '../models/user.model';
import { LoginService } from './login.service';
import { UserService } from './user.service';

@Injectable({
  providedIn: 'root'
})
export class TweetService {
  @Output() tweetsChanged : EventEmitter<Tweet[]>= new EventEmitter<Tweet[]>();


  constructor(private http: HttpClient, private userService : UserService, private loggedUserService : LoginService) { }

  getTweetsByUsername(username : String,) {
   return this.http.get<any>(`${environment.api}/userline/${username}`)
  }

  getTweets(username : string) {
    return this.http.get<any>(`${environment.api}/tweets/${username}`)
   }
  
  
  getTimelineTweets() {
    const username=this.loggedUserService.getLoggedUser();
    return this.http.get<any>(`${environment.api}/timeline/${username}`)
  }

  getTweet(username : String, createdAt : String) {
   return this.http.get<any>(`${environment.api}/tweet/username/${username}/createdAt/${createdAt.replace('T', ' ')}`)
  }


  createTweet(body : string, retweet : boolean=false, rretweetedFrom : string =""){
    const username=this.loggedUserService.getLoggedUser();
    const newDate= new Date().toISOString().split('T')[0]+" "+new Date().toISOString().split('T')[1].substr(0,8);
    this.userService.getUser(username).subscribe({
      next : (data) => {
        console.log( "user", data)
        this.http
        .post<string>( environment.api+'/tweet', {
          username :this.loggedUserService.getLoggedUser(),
          body : body,
          createdAt:newDate,
          likes : 0,
          photo : data!.image,
          retweets : 0,
          isRetweet : retweet,
          originalOwnerUsername : this.loggedUserService.getLoggedUser(),
          firstName : data!.firstName,
          lastName : data!.lastName,
          retweetedFrom : rretweetedFrom
        })
        .subscribe(
          {next : (data) => {
            
          }
          ,
          error : (err) => {
            console.log(err);
          
          }
        }
        );
      }
    })
   return newDate;
  }

  deleteTweet(username : String, createdAt : String, retweetedFrom : string='') {
    this.http.delete(`${environment.api}/tweet/username/${username}/createdAt/${createdAt.replace('T', ' ')}/retweetedFrom/${retweetedFrom}`)
    .subscribe(
      {next : (data) => {
        console.log(data);
      }});
      
  }

   updateTweet(body : string, username : string, createdAt : string, likesNum : number, retweetNum : number)
   {
     console.log(likesNum)
    this.http
        .patch<string>(`${environment.api}/tweet`, {
          body : body,
          username : username,
          createdAt : createdAt.replace('T', ' '),
          likes : likesNum,
          retweets : retweetNum
        })
        .subscribe(
          {next : (data) => {
            console.log(data)
          }
          ,
          error : (err) => {
            console.log(err);
          
          }
        }
        );
  }

  retweet(tweet : Tweet){
  //this.updateTweet(tweet.body, tweet.originalOwnerUsername, tweet.createdAt,tweet.likes, tweet.retweets+1);
  console.log(tweet)
  const newDate=this.createTweet(tweet.body, true, tweet.originalOwnerUsername);
  console.log('ovde')
  this.http.post<any>(`${environment.api}/retweet/createdAt/${tweet.createdAt.replace('T', ' ')}/username/${this.loggedUserService.getLoggedUser()}/retweetedFrom/${tweet.originalOwnerUsername}/retweetCreatedAt/${newDate}`, {
  }).
  subscribe(
    {
      next : (data) => {
        console.log(data)
      }
    }
  )
  //this.tweetsChanged.emit(this.tweets);
  } 

  like(tweet : Tweet){
    // username tweetOwnerUsername createdAt
    console.log(this.loggedUserService.getLoggedUser())
    console.log(tweet.createdAt.replace('T', ' '))
    console.log(tweet)
    this.http
    .post<string>(`${environment.api}/like`, {
      username : this.loggedUserService.getLoggedUser(),
      createdAt : tweet.createdAt.replace('T', ' '),
      originalOwnerUsername : tweet.originalOwnerUsername
    })
    .subscribe(
      {next : (data) => {
        console.log(data)
      }
      ,
      error : (err) => {
        console.log(err);
      
      }
    }
    );
  }
  
  unlike(tweet : Tweet){
    this.http
    .post<string>(`${environment.api}/unlike`, {
      username : this.loggedUserService.getLoggedUser(),
      createdAt : tweet.createdAt.replace('T', ' '),
      originalOwnerUsername : tweet.originalOwnerUsername
    })
    .subscribe(
      {next : (data) => {
        console.log(data)
      }
      ,
      error : (err) => {
        console.log(err);
      
      }
    }
    );
  }
  
  userLikedTweet(username : string, createdAt : string){
    return this.http
        .post<boolean>( `${environment.api}/liked/createdAt/${createdAt.replace('T', ' ')}/username/${this.loggedUserService.getLoggedUser()}/originalOwnerUsername/${username}`, {
        })
      
}

userRetweetedTweet(username : string, createdAt : string){
  return this.http
      .post<boolean>( `${environment.api}/retweeted/createdAt/${createdAt.replace('T', ' ')}/username/${this.loggedUserService.getLoggedUser()}/originalOwnerUsername/${username}`, {
      })
    
}


}

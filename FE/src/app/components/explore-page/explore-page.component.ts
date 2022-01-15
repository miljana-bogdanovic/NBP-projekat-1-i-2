import { Component, OnInit } from '@angular/core';
import { Observable, of } from 'rxjs';
import { Tweet } from 'src/app/models/tweet.model';
import { User } from 'src/app/models/user.model';
import { LoginService } from 'src/app/services/login.service';
import { TweetService } from 'src/app/services/tweet.service';
import { UserService } from 'src/app/services/user.service';

@Component({
  selector: 'app-explore-page',
  templateUrl: './explore-page.component.html',
  styleUrls: ['./explore-page.component.css']
})
export class ExplorePageComponent implements OnInit {
  tweets : Tweet[]=[];
  search: string = '';
  loggedUsername : string ='';
  tweetsObservable :  Observable<Tweet[] | null> = of([]);

  constructor(private tweetService : TweetService, private userService : UserService, private loggedUser : LoginService) { }

  ngOnInit(): void {
    this.loggedUsername=this.loggedUser.getLoggedUser();
    this.loadTweets();
    

  }
  
  loadTweets(){
    this.tweetService.getTweets(this.loggedUsername).subscribe({
      next : (data) => {
        this.tweets=data.sort((t1 : Tweet, t2 : Tweet)=>{return t2.createdAt.localeCompare(t1.createdAt)});
        this.tweets.forEach((t)=> {
         this.tweetService.userLikedTweet(t.originalOwnerUsername, t.createdAt).subscribe({
           next : (data)=> {
            t.isLikedByUser=data;
           }
         })
         this.tweetService.userRetweetedTweet(t.originalOwnerUsername, t.createdAt).subscribe({
          next : (data)=> {
           t.isRetweetedByUser=data;
          }
        })
        })
        this.tweetsObservable=of(data.map((t: Tweet) => <Tweet>t));
      }
    })
  }

  profile( username : string){


  }
  onTweetChange(){

  }
}

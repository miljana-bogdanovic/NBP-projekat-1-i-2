import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { Observable, of, take } from 'rxjs';
import { Tweet } from 'src/app/models/tweet.model';
import { User } from 'src/app/models/user.model';
import { LoginService } from 'src/app/services/login.service';
import { TweetService } from 'src/app/services/tweet.service';
import { UserService } from 'src/app/services/user.service';

@Component({
  selector: 'app-home-page',
  templateUrl: './home-page.component.html',
  styleUrls: ['./home-page.component.css']
})
export class HomePageComponent implements OnInit {
  tweets : Tweet[]=[];
  search: string = " ";
  loggedUsername : string ="";
  users :  Observable<User[] | null> = of([]);

  constructor( private router: Router,private tweetService : TweetService, private userService : UserService, private loggedUser : LoginService) {
  }
  ngOnInit(): void {
  this.loadTweets();
  this.loggedUsername=this.loggedUser.getLoggedUser();
  this.userService.getUsers().subscribe((data)=> {
    this.users=of(data.map((user) => <User>user));
  })
  }

  loadTweets()
  {
    this.tweetService.getTimelineTweets().subscribe
    (
      //username ne
      //original creatror se zove retweetedFrom
      // username se zove originalCreator

      {next : (data) => {
        console.log(data)
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
        
       }
       ,
       error : (err) => {
         console.log(err);
       
       }
     }
     
    );
  }
  onTweetChange()
  {
    this.loadTweets()
  }

  profile(name : string){
    this.router.navigateByUrl( `/profile/${name}`);
  }
onClick(user : User){

}
}


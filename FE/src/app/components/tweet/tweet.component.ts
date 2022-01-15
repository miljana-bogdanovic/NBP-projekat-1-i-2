import { Component, Input, Output, OnInit, EventEmitter } from '@angular/core';
import { Router } from '@angular/router';
import { Tweet } from 'src/app/models/tweet.model';
import { User } from 'src/app/models/user.model';
import { LoginService } from 'src/app/services/login.service';
import { TweetService } from 'src/app/services/tweet.service';
import { UserService } from 'src/app/services/user.service';

@Component({
  selector: 'app-tweet',
  templateUrl: './tweet.component.html',
  styleUrls: ['./tweet.component.css'],
})
export class TweetComponent implements OnInit {
  @Input() tweet: Tweet | null = null;
  @Output() tweetCreated: EventEmitter<Tweet>= new EventEmitter<Tweet>();
  @Output() tweetDeleted: EventEmitter<Tweet>= new EventEmitter<Tweet>();
  isMine : boolean=false;
  edit : boolean=false;
  username : string='';
  canRetweet : boolean=false;

  constructor(private tweetService : TweetService, private profileService : UserService, private loggedUser : LoginService, private router : Router) {}

  ngOnInit(): void {
    this.username=this.loggedUser.getLoggedUser()
    if (this.tweet?.originalOwnerUsername==this.username)
      this.isMine=true;
    this.profileService.userFollowsUser(this.tweet!.originalOwnerUsername).subscribe(
      {
        next : (data) => {
          this.canRetweet=data;
          console.log(data)
        }
      }
    )
  }

  toggleLike(){
    this.tweet!.isLikedByUser=!this.tweet!.isLikedByUser;
    if (this.tweet)
    if (this.tweet!.isLikedByUser)
     {
         this.tweetService.like(this.tweet);
         this.tweet.likes=this.tweet!.likes+1;
         this.tweetService.like(this.tweet);
     }
    else
    {
      this.tweetService.unlike(this.tweet);
      this.tweet.likes=this.tweet!.likes-1;
      this.tweetService.unlike(this.tweet);
    }
    // pozovi fju za lajk
  }

  toggleTweet(){
    console.log('kliknuto')
    console.log(this.tweet)
    if (this.tweet?.isRetweetedByUser!=true)
      {
        this.tweet!.isRetweetedByUser=!this.tweet!.isRetweetedByUser;
        this.tweet!.retweets=this.tweet!.retweets+1;
        this.tweetService.retweet(this.tweet!);
        this.tweetCreated.emit();
      }
  }

  onEditClick(){
    this.edit=true;
  }

  onDeleteClick(){
    this.tweetService.deleteTweet(this.tweet!.originalOwnerUsername, this.tweet!.createdAt, this.tweet?.isRetweet ? this.tweet.retweetedFrom : '');
    this.tweetDeleted.emit(this.tweet!);
  }

  toggleEdit(body : string){
    this.edit=false;
    this.tweet!.body=body;
  }

  ngOnChanges(): void {
    //console.log(this.tweet); // logs correct data, yay!
  }

  profile(name : string){
    this.router.navigateByUrl( `/profile/${name}`);
  }
}

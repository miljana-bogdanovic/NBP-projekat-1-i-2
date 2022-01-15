import { HttpClient } from '@angular/common/http';
import { THIS_EXPR } from '@angular/compiler/src/output/output_ast';
import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { combineLatest, map, of, take } from 'rxjs';
import { LoggedUser } from 'src/app/models/logged-user.model';
import { Tweet } from 'src/app/models/tweet.model';
import { User } from 'src/app/models/user.model';
import { LoginService } from 'src/app/services/login.service';
import { TweetService } from 'src/app/services/tweet.service';
import { UserService } from 'src/app/services/user.service';

@Component({
  selector: 'app-profile-page',
  templateUrl: './profile-page.component.html',
  styleUrls: ['./profile-page.component.css']
})
export class ProfilePageComponent implements OnInit {
  user: User|null=null;
  username : string='';
  isLoading: boolean = true;
  showPopup: boolean = false;
  follow : boolean=false;
  isFollowed : boolean=false;
  tweets : Tweet[]=[];
  loggedUser : string='';
  image : string="https://www.shareicon.net/data/512x512/2016/10/25/847809_user_512x512.png";

  constructor(
    private userService: UserService,
    private tweetService : TweetService,
    private http: HttpClient,
    private router: Router,
    private route: ActivatedRoute,
    private loggedUserService : LoginService
  ) {}


  ngOnInit(): void {
    const user=this.loggedUserService.getLoggedUser();
    if (user!=null)
      this.loggedUser=user

    this.route.params.subscribe(params => {
      this.username=params['username'];
      this.loadProfile(this.username);
      this.loadTweets(this.username);
      console.log(this.username, this.loggedUser)
      this.loggedUser=this.loggedUserService.getLoggedUser();
      this.follow=this.loggedUser==this.username ? false : true;
        });

  }
  loadProfile(username : string) {
   
 this.userService.getUser(username)
 .pipe(take(1))
    .subscribe(
    {next : (data) => {
      this.user=new User(data.username, data.image, data.firstName, data.lastName, data.email, data.followers, data.following, data.password);
      if (this.loggedUser!=this.user?.username)
          this.userService.userFollowsUser(this.user!.username).subscribe(
            {
              next : (data) => {
                console.log(data)
                this.follow=true;
                this.isFollowed=data;
              }
            }
          )
     }
     ,
     error : (err) => {
       console.log(err);
     
     }
   }
   
  );

}
  loadTweets(username : string )
{
  this.tweetService.getTweetsByUsername(username)
  .pipe(take(1))
    .subscribe(
    {next : (data) => {
    this.tweets=data.sort((t1 : Tweet, t2 : Tweet)=>{return t2.createdAt.localeCompare(t1.createdAt)});;
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
    this.isLoading=false;
     }
     ,
     error : (err) => {
       console.log(err);
     
     }
   }
   
  );
}
    
  onChangeClick() {
    this.showPopup=true;
  }
  onCloseEvent(user : User){
    this.showPopup=false;
    if (user!=undefined)
    {
      this.user!.firstName=user.firstName;
      this.user!.lastName=user.lastName;
      this.user!.email=user.email;
      this.user!.image=user.image;
      this.tweets.map((t)=> {
        t.firstName=user.firstName;
        t.lastName=user.lastName;
        t.image=user.image;
      });
    }
  }

  onClickUnfollow(){
      this.userService.unfollowUser(this.user!.username).subscribe(
        {
          next : (data) => {
            console.log(data)
          }
        });
      this.user!.followers-=1;
      this.isFollowed=false;
  }
  onClickFollow(){
      this.userService.followUser(this.user!.username).subscribe(
        {
          next : (data) => {
            console.log(data)
          }
        }
      )
      this.user!.followers+=1;
      this.isFollowed=true;
    }
  


  onClickFollowers(){
    this.router.navigateByUrl('/profile/'+this.user?.username+'/followers');
  }
  onClickFollowing(){
    this.router.navigateByUrl('/profile/'+this.user?.username+'/following');
  }

  onRefreshTweets(){
    this.loadTweets(this.username);
  }
}

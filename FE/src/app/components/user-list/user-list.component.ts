import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { LoggedUser } from 'src/app/models/logged-user.model';
import { UserInfo } from 'src/app/models/user-info.models';
import { User } from 'src/app/models/user.model';
import { LoginService } from 'src/app/services/login.service';
import { UserService } from 'src/app/services/user.service';

@Component({
  selector: 'app-user-list',
  templateUrl: './user-list.component.html',
  styleUrls: ['./user-list.component.css']
})
export class UserListComponent implements OnInit {
  loggedUsername : string = "";
  username : string=""
  users : UserInfo[]=[];
  image : string="https://www.shareicon.net/data/512x512/2016/10/25/847809_user_512x512.png";
  constructor( private router: Router,
    private route: ActivatedRoute,
    private userService : UserService,
    private loggedUser : LoginService) { }

  ngOnInit(): void {
    this.loggedUsername=this.loggedUser.getLoggedUser();
    this.route.params.subscribe(params => {
      this.username=params['username'];
      this.getUsers(this.route).subscribe(
        ({
          next : (data) => {
            console.log(data)
            
            this.users=data;
            this.users.forEach(element => {
              this.userService.userFollowsUser(element.friendUsername ? element.friendUsername : element.followerUsername).subscribe({
                next : (data) => {
                  element.isFollowedByMe=data;
                }
              })
            });
          }
        })
      )
  });
}

  getUsers(route : ActivatedRoute)
  {
    if(route.toString().search('following')!=-1)
    {
      console.log('following')
      return this.userService.getFollowing(this.username);
    }
    else {
      console.log('followers')
      return this.userService.getFollowers(this.username);
    }
     
  }

  onClick(user : UserInfo){
    console.log(user.isFollowedByMe)
    if (user.isFollowedByMe)
       { this.userService.unfollowUser(user.friendUsername ? user.friendUsername : user.followerUsername).subscribe({
         next : (data)=> {
          user.isFollowedByMe=!user.isFollowedByMe;
         }
       })
       }
        else
        this.userService.followUser( user.friendUsername ? user.friendUsername : user.followerUsername).subscribe({
          next : (data)=> {
           user.isFollowedByMe=!user.isFollowedByMe;
          }
        })
      

      console.log(user.isFollowedByMe)
      }

  back(){
    this.router.navigateByUrl( `/profile/${this.username}`);
  }

  profile(name : string){
    this.router.navigateByUrl( `/profile/${name}`);
  }
}

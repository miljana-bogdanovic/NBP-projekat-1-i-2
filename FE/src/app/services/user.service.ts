import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { environment } from 'src/environments/environment';
import { LoginService } from './login.service';
import { User } from '../models/user.model';

@Injectable({
  providedIn: 'root'
})
export class UserService {
 //private _username : string = 'miljana1';
  
  //public getUsername(): string {
    //if (this.username=="")
      //this.username=this.loginService.getCurrentLoggedUserUsername()
    //return this._username;
  //}
  public set username(value: string) {

    this.username = value;
  }
  constructor(private http : HttpClient,
    private loggedUserService : LoginService) { }


  getUsers(){
    console.log('getUsers')
    return this.http.get<User[]>(`${environment.api}/users`)
  }
  getUser(username : String) 
  {
    return this.http.get<any>(`${environment.api}/profile/${username}`)
  }

  createUser(username : string, firstName : string, lastName : string, email : string, password : string, image : string)
   {
    //console.log(username, firstName, lastName, email, password, image  )
   return this.http
       .post<string>( environment.api+'/profile', {
         username : username,
         followers : 0,
         following: 0,
         firstName : firstName,
         lastName : lastName,
         image : image,
         email : email,
         password : password
       })
       
  }

  updateUser(username : string, firstName : string, lastName : string, email : string, password : string, image : string, followers : number, following : number)
  {
  return  this.http
       .patch<string>(`${environment.api}/profile`, {
         username : username,
        followers : followers,
        following: following,
         firstName : firstName,
         lastName : lastName,
         image : image,
         email : email,
         password : password,
       })

  }

  deleteUser(username : String) 
  {
    return this.http.delete(`${environment.api}/profile/${username}`)
     
  }

  // ne followouje
  followUser( follows : string){
      return this.http
          .post<string>( `${environment.api}/${this.loggedUserService.getLoggedUser()}/follow/${follows}`, {
          
          })
        
  }

  userFollowsUser(follows : string){
      return this.http
          .get<boolean>( `${environment.api}/${this.loggedUserService.getLoggedUser()}/follows/${follows}`, {
          
          })
        
  }


  unfollowUser( follows : string){
    return this.http
        .post<string>( `${environment.api}/${this.loggedUserService.getLoggedUser()}/unfollow/${follows}`, {
        })
      
}

getFollowers(username : string){
  return this.http.get<any>(`${environment.api}/${username}/followers`)
}

getFollowing(username : string){
  return this.http.get<any>(`${environment.api}/${username}/following`)
}
}

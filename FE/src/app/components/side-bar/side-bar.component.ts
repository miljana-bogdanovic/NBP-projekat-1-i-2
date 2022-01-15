import { Component, OnInit } from '@angular/core';
import { LoginService } from 'src/app/services/login.service';

@Component({
  selector: 'app-side-bar',
  templateUrl: './side-bar.component.html',
  styleUrls: ['./side-bar.component.css']
})
export class SideBarComponent implements OnInit {
username : string='';
  constructor(private loggedUser : LoginService) { }

  ngOnInit(): void {
    this.loggedUser.loggedUser.subscribe((data)=> {
      this.username=data!.username;
    })
    this.username=this.loggedUser.getLoggedUser();
  }
  logout(){
    this.loggedUser.logout();
    this.username='';
  }
}

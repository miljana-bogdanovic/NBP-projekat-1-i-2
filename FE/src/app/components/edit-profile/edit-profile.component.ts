import { Component, Input, OnInit, Output } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';
import { User } from 'src/app/models/user.model';
import { UserService } from 'src/app/services/user.service';
import { EventEmitter } from '@angular/core';
import { LoginService } from 'src/app/services/login.service';
import { take } from 'rxjs';

@Component({
  selector: 'app-edit-profil',
  templateUrl: './edit-profile.component.html',
  styleUrls: ['./edit-profile.component.css']
})
export class EditProfileComponent implements OnInit {
@Input() user : User|null=null;
form: FormGroup|null=null;
username : string='';
name : string='';
lastname : string='';
password : string='';
newPassword : string='';
image : string='';
body : string='';
email : string='';
 imageSrc: string = '';
@Output() close : EventEmitter<User> = new EventEmitter<User>();

  constructor(private userService : UserService, private loggedKorisnikService : LoginService) { }

  ngOnInit(): void {
    this.form = new FormGroup({
      name : new FormControl('', Validators.required),
      lastname: new FormControl('', Validators.required),
      email : new FormControl('', Validators.required),
      //password: new FormControl('',  Validators.minLength(6)),
     // newPassword: new FormControl('', Validators.minLength(6)),
      body :  new FormControl('')
  });

  this.username=this.loggedKorisnikService.getLoggedUser();
  this.userService.getUser(this.username)
  .pipe(take(1))
  .subscribe(
    {
      next : (data) => {
        this.name=data.firstName;
        this.lastname=data.lastName;
        this.image=data.photo;
        this.imageSrc=data.image;
        this.email=data.email;
        this.form?.patchValue({
          name : this.name,
          lastname : this.lastname,
          email : this.email
        })
        this.user=data;
      }
    });
  
  }
  onSubmit(){
    if (this.form)
    {
     
    //if((this.form.get('password')!.value!='' && this.form.get('newPassword')!.value=='')|| (this.form.get('lozinka')!.value=='' && this.form.get('novalozinka')!.value!='')  )
    //this.error="Enter both new and old password";
   // else 
    //{
      console.log(this.imageSrc, this.image)
        this.userService.updateUser(
          this.user!.username,
          this.form.get('name')!.value,
          this.form.get('lastname')!.value,
          this.form.get('email')!.value,
          this.user!.password,
          this.imageSrc,
          this.user!.followers,
          this.user!.following
        ).subscribe(
            {
              next : (data)=> {
                  this.close.emit(new User( this.user!.username,
                    this.imageSrc,
                    this.form!.get('name')!.value,
                    this.form!.get('lastname')!.value,
                    this.form!.get('email')!.value,
                    this.user!.followers,
                    this.user!.following, ''));
                  
              }
              
            }
          )
     //   this.form.get('password')!.value,
     //   this.form.get('newPassword')!.value)
        //.subscribe(
        //   (_) => {
        //     this.success="Profile updated"
        //     window.scroll(0,0);
        //     setTimeout(()=>{
        //       this.success='';
             
        //     }, 2000);                 
        //   },
        //   (err) => {
        //     this.error=err.error;
        //     setTimeout(()=>{
        //     this.error='';
        //     }, 2000);
                  
        //   }
        //   );
     // }
   // }
  
  }
}

  cancel(){
   // fire event
   this.close.emit(undefined);
}

  handleInputChange(e:any) {
    var file = e.dataTransfer ? e.dataTransfer.files[0] : e.target.files[0];
    var pattern = /image-*/;
    var reader = new FileReader();
    if (!file.type.match(pattern)) {
      return;
    }
    reader.onload = this._handleReaderLoaded.bind(this);
    reader.readAsDataURL(file);
  }

  _handleReaderLoaded(e:any) {
    let reader = e.target;
    this.imageSrc = reader.result;
  }
}

import { Component, OnInit } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import { Subscription } from 'rxjs';
import { LoginService } from '../../services/login.service';

@Component({
  selector: 'app-login-page',
  templateUrl: './login-page.component.html',
  styleUrls: ['./login-page.component.css'],
})
export class LoginPageComponent implements OnInit {
  form: FormGroup | null = null;
  loading = false;
  submitted = false;
  errorMessage: string | null = null;
  loginSubscription: Subscription | null = null;
  hide = true;

  constructor(private router: Router, private loginService: LoginService) {}

  ngOnInit(): void {
    console.log("uspesno")
    this.form = new FormGroup({
      username: new FormControl('', [Validators.required]),
      password: new FormControl('', [
        Validators.required,
        Validators.minLength(6),
      ]),
    });
  }
  
  get f() {
    if (this.form) return this.form.controls;
    return null;
  }

  onSubmit() {
    this.submitted = true;
    if (this.form) {
      this.loginSubscription = this.loginService
        .loginUser(
          this.form.get('username')!.value,
          this.form.get('password')!.value
        )
        .subscribe({
          next: this.handleUpdate.bind(this),
          error: this.handleError.bind(this)
          }
        );
    }
  }
  
  handleUpdate(){
    this.errorMessage = null;
    console.log("uspesno")
    this.router.navigate(['/home']); 
    console.log("posleHome")
  }
  handleError(err : string|null){
    this.errorMessage = "Error, try again";
  }

  registrate(){
    this.router.navigate(['/registration']);
  }

  ngOnDestroy() {
    if (this.loginSubscription) {
      this.loginSubscription.unsubscribe();
    }
    this.submitted = false;
  }
}

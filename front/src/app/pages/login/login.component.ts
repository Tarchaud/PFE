import { Component } from '@angular/core';
import { AuthService } from 'src/app/shared/services/auth.service';

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css']
})
export class LoginComponent {
  userNameLogIn : string = "";
  passwordLogIn : string = "";
  userNameSignUp : string = "";
  passwordSignUp : string = "";


  constructor(private auth : AuthService) { }

  ngOnInit(): void {
    this.initForm();
  }

  login() {
    console.log("Login");
    this.auth.auhtID = {
      username : this.userNameLogIn,
      password : this.passwordLogIn
    };
    // this.auth.login();
  }

  register() {
    console.log("Register");
    this.auth.auhtID = {
      username : this.userNameSignUp,
      password : this.passwordSignUp
    };
    // this.auth.register();
  }

  initForm() {
    this.userNameLogIn = "";
    this.passwordLogIn = "";
    this.userNameSignUp = "";
    this.passwordSignUp = "";
  }


}

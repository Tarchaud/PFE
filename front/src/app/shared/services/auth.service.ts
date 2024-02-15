import { Injectable } from '@angular/core';
import { UserI } from '../models/user-i';
import { HttpClient } from '@angular/common/http';
import { Router } from '@angular/router';

@Injectable({
  providedIn: 'root'
})
export class AuthService {
  isLoggedIn : boolean = false;
  user !: UserI;
  auhtID : { username : string, password : string } = { username : "", password : "" };

  constructor(private http : HttpClient, private router : Router) { }

  login() {
    this.http.post<UserI>("http://localhost:3000/login", this.auhtID).subscribe({
      next : (data : UserI) => {
        this.user = data;
        this.isLoggedIn = true;
        this.router.navigate(["/"]);
      },
      error : (err) => {
        console.log(err);
      },
      complete : () => {
        console.log("Login completed");
      }
    })
  }

  logout() {
    this.isLoggedIn = false;
    this.user = { id : "", username : "" };
    this.router.navigate(["/"]);
  }

  register() {
    this.http.post<UserI>("http://localhost:3000/register", this.auhtID).subscribe({
      next : (data : UserI) => {
        this.user = data;
        this.isLoggedIn = true;
        this.router.navigate(["/"]);
      },
      error : (err) => {
        console.log(err);
      },
      complete : () => {
        console.log("Register completed");
      }
    })
  }
}

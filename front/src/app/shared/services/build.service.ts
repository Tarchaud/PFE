import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';

@Injectable({
  providedIn: 'root'
})
export class BuildService {
  //TODO : create model Build
  public arr_builds : any[] = [];

  constructor(private http : HttpClient) { }

  //TODO : link to the backend
  getAllbuilds() {
    return this.http.get<any[]>('assets/data/builds.json').subscribe({
      next : (data : any[]) => {
        this.arr_builds = data;
      },
      error : (err) => {
        console.log(err);
      },
      complete : () => {
        console.log('Get all builds completed');
      }
    });
  }


  //TODO : link to the backend
  createBuild(build : any) {
    console.log("Build created");
    return this.http.post<any>('assets/data/builds.json', build).subscribe({
      next : (data : any) => {
        console.log(data);
      },
      error : (err) => {
        console.log(err);
      },
      complete : () => {
        console.log('Create build completed');
      }
    });
  }
}

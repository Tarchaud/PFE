import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { BuildI } from '../models/build-i';

@Injectable({
  providedIn: 'root'
})
export class BuildService {
  public arr_builds : BuildI[] = [];

  constructor(private http : HttpClient) { }

  //TODO : link to the backend
  getAllbuilds() {
    return this.http.get<BuildI[]>('assets/data/builds.json').subscribe({
      next : (data : BuildI[]) => {
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
      next : (data : BuildI) => {
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

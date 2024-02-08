import { Component } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { BuildI } from 'src/app/shared/models/build-i';
import { BuildService } from 'src/app/shared/services/build.service';

@Component({
  selector: 'app-build',
  templateUrl: './build.component.html',
  styleUrls: ['./build.component.css']
})
export class BuildComponent {
  build !: BuildI;
  param !: string;


  constructor(public activeRoute : ActivatedRoute, public buildService : BuildService) {
    this.param = this.activeRoute.snapshot.paramMap.get('buildId') || '';
    console.log('params : ', this.param);
    this.buildService.getBuildById(this.param).subscribe({
      next : (data : BuildI) => {
        this.build = data;
      },
      error : (err) => {
        console.log(err);
      },
      complete : () => {
        console.log('Get build completed');
      }
    });
   }

  ngOnInit(): void {
  }


}

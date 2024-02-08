import { Component } from '@angular/core';

import { Notify } from 'notiflix/build/notiflix-notify-aio';

//Services
import { ActionService } from 'src/app/shared/services/action.service';
import { BuildService } from 'src/app/shared/services/build.service';


@Component({
  selector: 'app-create-build',
  templateUrl: './create-build.component.html',
  styleUrls: ['./create-build.component.css']
})
export class CreateBuildComponent {
  nameBuild : string = "";
  levelBuild : number = 100;
  effectBuild : number[] = [];
  selectedEffect: { [key: number]: boolean } = {};


  constructor(public action : ActionService, private buildService : BuildService) { }

  ngOnInit(): void {
    this.initForm();
    this.action.getAllActions();
  }

  initForm() {
    this.nameBuild = "";
    this.levelBuild = 100;
    this.effectBuild = [];
    this.selectedEffect = {};
  }

  createBuild() {
    for(let key in this.selectedEffect) {
      if (this.selectedEffect[key]) {
        this.effectBuild.push(parseInt(key));
      }
    }
    if (this.effectBuild.length == 0 || this.nameBuild == "" || (this.levelBuild < 1 || this.levelBuild > 230)) {
      Notify.failure("Invalid Form");
      return;
    }

    let build = {
      name : this.nameBuild,
      level : this.levelBuild,
      effect : this.effectBuild
    }
    console.log("Name : " + this.nameBuild);
    console.log("Level : " + this.levelBuild);
    console.table(this.effectBuild);

    //TODO : comment
    // this.buildService.createBuild(build);

    console.log("Build created");
    this.initForm();
  }

}

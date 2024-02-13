import { Component } from '@angular/core';
import { Router } from '@angular/router';

//Drag and Drop
import {CdkDragDrop, CdkDropList, CdkDrag, moveItemInArray} from '@angular/cdk/drag-drop';

//Notiflix
import { Notify } from 'notiflix/build/notiflix-notify-aio';
import { Block } from 'notiflix/build/notiflix-block-aio';

//Services
import { ActionService } from 'src/app/shared/services/action.service';
import { BuildService } from 'src/app/shared/services/build.service';

//Models
import { BuildI } from 'src/app/shared/models/build-i';

class buildFormI {
  name : string = "";
  level : number = 100;
  cost : string = "low";
  effects : number[] = [];
}

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
  costBuild : string = "low";

  step = 0;


  constructor(public action : ActionService, private buildService : BuildService, private route : Router) { }

  ngOnInit(): void {
    this.initForm();
    this.action.getAllActions();
  }

  initForm() {
    this.nameBuild = "";
    this.levelBuild = 100;
    this.effectBuild = [];
    this.selectedEffect = {};
    this.step = 0;
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

    let buildForm = {
      name : this.nameBuild,
      level : this.levelBuild,
      cost : this.costBuild,
      effects : this.effectBuild
    }

    if( this.effectBuild.length == 1){
      this.sendRequest(buildForm);
    }else {
      this.step = 1;
    }
  }

  drop(event: CdkDragDrop<string[]>) {
    moveItemInArray(this.effectBuild, event.previousIndex, event.currentIndex);
  }

  prioEffectStep() {
    let buildForm = {
      name : this.nameBuild,
      level : this.levelBuild,
      cost : this.costBuild,
      effects : this.effectBuild
    }
    console.log(buildForm);

    this.sendRequest(buildForm);
  }

  sendRequest(buildForm : buildFormI) {
    Block.arrows('.container', 'Génération du Build...');
    this.initForm();
      this.buildService.createBuild(buildForm).subscribe({
        next : (data : BuildI) => {
          console.log(data);
          this.route.navigate(['/build', data.id]);
        },
        error : (err) => {
          console.log(err.message);
          Notify.failure(err.message);
        },
        complete : () => {
            Block.remove('.container');
            console.log('Build created');
          }
        }
      );
  }

}

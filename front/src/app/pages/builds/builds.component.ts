import { Component } from '@angular/core';
import { ActionService } from 'src/app/shared/services/action.service';
import { BuildService } from 'src/app/shared/services/build.service';

@Component({
  selector: 'app-builds',
  templateUrl: './builds.component.html',
  styleUrls: ['./builds.component.css']
})
export class BuildsComponent {
  levelFilter: number[] = [0, 230];
  toShow: number = 20;
  nameBuild : string = '';
  costFilter : string = 'all';
  effectsFilter : number[] = [];

  constructor(public builds : BuildService, public actions : ActionService) { }

  ngOnInit() {
    this.actions.getAllActions();
    this.builds.getAllbuilds();
  }

  //Permet de faire du lazy loading
  onScroll(event: any) {
    const element = event.target;
    if (element.scrollHeight - element.scrollTop === element.clientHeight) {
      // Le scroll a atteint le bas
      console.log('Scroll down detected!');
      this.toShow += 20;
    }
  }

  resetEffectFilter() {
    this.effectsFilter = [];
  }

}

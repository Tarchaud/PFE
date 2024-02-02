import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';

//Model
import { ActionI } from '../models/action-i';

@Injectable({
  providedIn: 'root'
})
export class ActionService {
  public arr_actions : ActionI[] = [];
  public map_actions = new Map<Number, ActionI>();

  constructor(private http : HttpClient) { }

  //TODO : link to the backend
  getAllActions() {
    return this.http.get<ActionI[]>('assets/data/actions.json').subscribe({
      next : (data : ActionI[]) => {
        this.arr_actions = data;
        this.mapActions();
      },
      error : (err) => {
        console.log(err);
      },
      complete : () => {
        console.log('Get all actions completed');
      }
    });
  }

  mapActions() {
    this.arr_actions.forEach((action) => {
      this.map_actions.set(action.definition.id, action);
    });
  }
}

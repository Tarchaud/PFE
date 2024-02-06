import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';

//Model
import { ItemI } from '../models/item-i';

@Injectable({
  providedIn: 'root'
})
export class ItemService {
  public arr_items : ItemI[] = [];

  constructor(private http : HttpClient) { }

  //TODO : link to the backend
  getAllItems() {
    return this.http.get<ItemI[]>('assets/data/items.json').subscribe({
      next : (data : ItemI[]) => {
        this.arr_items = data;
      },
      error : (err) => {
        console.log(err);
      },
      complete : () => {
        console.log('Get all items completed');
      }
    });
  }

  //TODO : link to the backend
  getAllItemsByType(type : number[]) {
    return this.http.get<ItemI[]>('assets/data/items.json').subscribe({
      next : (data : ItemI[]) => {
        this.arr_items = data.filter((item) => {
          return type.includes(item.definition.item.baseParameters.itemTypeId);
        });

        //sort by level
        this.arr_items = this.arr_items.sort((a, b) => {
          return b.definition.item.level - a.definition.item.level;
        });
      },
      error : (err) => {
        console.log(err);
      },
      complete : () => {
        console.log('Get all items completed');
      }
    });
  }

  getItemById(id : number) {
    return this.http.get<ItemI[]>('assets/data/items.json').subscribe({
      next : (data : ItemI[]) => {
        this.arr_items = data.filter((item) => {
          return item.definition.item.id === id;
        });
      },
      error : (err) => {
        console.log(err);
      },
      complete : () => {
        console.log('Get all items completed');
      }
    });
  }


}

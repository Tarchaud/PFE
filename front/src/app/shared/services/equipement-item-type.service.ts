import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';

//Model
import { EquipmentItemTypeI } from '../models/equipment-item-type-i';

@Injectable({
  providedIn: 'root'
})
export class EquipementItemTypeService {
  public arr_equipementItemTypes : EquipmentItemTypeI[] = [];

  constructor(private http : HttpClient) { }

  //TODO : link to the backend
  getAllEquipementItemTypes() {
    return this.http.get<EquipmentItemTypeI[]>('assets/data/equipementItemTypes.json').subscribe({
      next : (data : EquipmentItemTypeI[]) => {
        this.arr_equipementItemTypes = data;
      },
      error : (err) => {
        console.log(err);
      },
      complete : () => {
        console.log('Get all equipementItemTypes completed');
      }
    });
  }
}

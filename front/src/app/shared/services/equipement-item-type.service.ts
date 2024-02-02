import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';

//Model
import { EquipmentItemTypeI } from '../models/equipment-item-type-i';

@Injectable({
  providedIn: 'root'
})
export class EquipementItemTypeService {
  public arr_equipementItemTypes : EquipmentItemTypeI[] = [];
  public map_equipementItemTypes = new Map<Number, EquipmentItemTypeI>();

  constructor(private http : HttpClient) { }

  //TODO : link to the backend
  getAllEquipementItemTypes() {
    return this.http.get<EquipmentItemTypeI[]>('assets/data/equipementItemTypes.json').subscribe({
      next : (data : EquipmentItemTypeI[]) => {
        this.arr_equipementItemTypes = data;
        this.mapEquipementItemTypes();
      },
      error : (err) => {
        console.log(err);
      },
      complete : () => {
        console.log('Get all equipementItemTypes completed');
      }
    });
  }

  mapEquipementItemTypes() {
    this.arr_equipementItemTypes.forEach((equipementItemType) => {
      this.map_equipementItemTypes.set(equipementItemType.definition.id, equipementItemType);
    });
  }
}

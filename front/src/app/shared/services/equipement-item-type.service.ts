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
  public map_equipementParentItemTypes = new Map<number, number[]>();

  constructor(private http : HttpClient) { }

  //TODO : link to the backend
  getAllEquipementItemTypes() {
    return this.http.get<EquipmentItemTypeI[]>('assets/data/equipmentItemTypes.json').subscribe({
      next : (data : EquipmentItemTypeI[]) => {
        this.arr_equipementItemTypes = data;
        this.mapEquipementItemTypes();
        this.mapEquipementParentItemTypes();
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

  mapEquipementParentItemTypes() {
    this.arr_equipementItemTypes.forEach((equipementItemType) => {
      if(equipementItemType.definition.parentId !== undefined ) {
        if(this.map_equipementParentItemTypes.has(equipementItemType.definition.parentId)) {
        this.map_equipementParentItemTypes.get(equipementItemType.definition.parentId)?.push(equipementItemType.definition.id);
        } else if(this.map_equipementItemTypes.has(equipementItemType.definition.parentId)){
          this.map_equipementParentItemTypes.set(equipementItemType.definition.parentId, [equipementItemType.definition.id]);
        } else if(!this.map_equipementParentItemTypes.has(equipementItemType.definition.id)) {
          this.map_equipementParentItemTypes.set(equipementItemType.definition.id, [equipementItemType.definition.id]);
        }
      }
      this.map_equipementParentItemTypes.delete(647);
      this.map_equipementParentItemTypes.delete(537);
      this.map_equipementParentItemTypes.delete(480);

    });
  }
}

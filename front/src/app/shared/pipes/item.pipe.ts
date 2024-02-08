import { Pipe, PipeTransform } from '@angular/core';
import { ItemI } from '../models/item-i';
import { BuildI } from '../models/build-i';

@Pipe({
  name: 'name'
})
export class ItemPipe implements PipeTransform {

  transform(arr: any[], filter : string): any[] {
    if(arr as ItemI[]) {
      return arr.filter(i => i.title.fr.toLowerCase().indexOf(filter.toLowerCase()) > -1 );
    }else if (arr as BuildI[]) {
      return arr.filter(b => b.name.toLowerCase().indexOf(filter.toLowerCase()) > -1);
    }else {
      return arr;
    }
  }

}

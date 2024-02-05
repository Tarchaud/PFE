import { Pipe, PipeTransform } from '@angular/core';
import { ItemI } from '../models/item-i';

@Pipe({
  name: 'item'
})
export class ItemPipe implements PipeTransform {

  transform(items: ItemI[], filter : string): ItemI[] {
    return items.filter(i => i.title.fr.toLowerCase().indexOf(filter.toLowerCase()) > -1 );;
  }

}

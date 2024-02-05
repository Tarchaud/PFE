import { Pipe, PipeTransform } from '@angular/core';
import { ItemI } from '../models/item-i';

@Pipe({
  name: 'level'
})
export class LevelPipe implements PipeTransform {

  transform(items: ItemI[], levelMin: number, levelMax: number): ItemI[] {
    console.log(levelMax);
    console.log(items.length);


    return items.filter(i => i.definition.item.level >= levelMin && i.definition.item.level <= levelMax);
  }

}

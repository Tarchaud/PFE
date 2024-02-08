import { Pipe, PipeTransform } from '@angular/core';
import { ItemI } from '../models/item-i';
import { BuildI } from '../models/build-i';

@Pipe({
  name: 'level'
})
export class LevelPipe implements PipeTransform {

  transform(arr: any[], levelMin: number, levelMax: number): any[]{
    if (arr as ItemI[]) {
      return arr.filter(i => i.definition.item.level >= levelMin && i.definition.item.level <= levelMax);
    }else if (arr as BuildI[]) {
      return arr.filter(b => b.level >= levelMin && b.level <= levelMax);
    }else {
      return arr;
    }
  }

}

import { Pipe, PipeTransform } from '@angular/core';

@Pipe({
  name: 'sentenceCost'
})
export class SentenceCostPipe implements PipeTransform {
  map_cost : {[key : string] : string} = {
    "low" : "bas",
    "medium" : "moyen",
    "high" : "élevé"
  }

  transform(sentence : string): string {
    return this.map_cost[sentence];
  }

}

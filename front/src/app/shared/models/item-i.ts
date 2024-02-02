import { TranslationI } from "./action-i";

export interface ItemI {
  id : string;
  definition : {
    item : defItemI;
    useEffects : effectI[];
    useCriticalEffects : effectI[];
    equipEffects : effectI[];
  };
  title : TranslationI;
  description : TranslationI;
}


export interface effectI {
  effect : {
    definition : {
      id : number;
      actionId : number;
      areaShape : number;
      areaSize : number[];
      params : number[];
    }
  }
}

export interface defItemI {
  id : number;
  level : number;
  baseParameters : {
    itemTypeId : number;
    itemSetId : number;
    rarity : number;
    bindType : number;
    minimumShardSlotNumber : number;
    maximumShardSlotNumber : number
  };
  useParameters : {
    useCostAp : number;
    useCostMp : number;
    useCostWp : number;
    useRangeMin : number;
    useRangeMax : number;
    useTestFreeCell : boolean;
    useTestLos : boolean;
    useTestOnlyLine : boolean;
    useTestNoBorderCell : boolean;
    useWorldTarget : number
  };
  graphicParameters : {
    gfxId : number;
    femaleGfxId : number;
  };
  properties : number[];
}
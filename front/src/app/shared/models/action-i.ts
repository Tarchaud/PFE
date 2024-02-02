export interface ActionI {
  id: string;
  definition : {
    id : number;
    effect : string;
  };
  description : TranslationI;
}


export interface TranslationI {
  fr : string;
  en : string;
  es : string;
  pt : string;
}

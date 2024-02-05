import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';

//Modules
import { FormsModule } from '@angular/forms';
import { HttpClientModule } from '@angular/common/http';
import { MatSliderModule } from '@angular/material/slider';

//Pipes
import { RarityPipe } from './pipes/rarity.pipe';
import { ElementPipe } from './pipes/element.pipe';
import { LevelPipe } from './pipes/level.pipe';
import { ItemPipe } from './pipes/item.pipe';


@NgModule({
  declarations: [
    RarityPipe,
    ElementPipe,
    LevelPipe,
    ItemPipe
  ],
  exports: [
    RarityPipe,
    ElementPipe,
    LevelPipe,
    ItemPipe,
    MatSliderModule,
    FormsModule,
    HttpClientModule,
  ],
  imports: [
    CommonModule,
    MatSliderModule,
    FormsModule,
    HttpClientModule,
  ]
})
export class SharedModule { }

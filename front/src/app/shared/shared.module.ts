import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';

//Modules
import { FormsModule } from '@angular/forms';
import { HttpClientModule } from '@angular/common/http';
import { MatSliderModule } from '@angular/material/slider';
import { DragDropModule } from '@angular/cdk/drag-drop';
import {MatDividerModule} from '@angular/material/divider';

//Pipes
import { RarityPipe } from './pipes/rarity.pipe';
import { ElementPipe } from './pipes/element.pipe';
import { LevelPipe } from './pipes/level.pipe';
import { ItemPipe } from './pipes/item.pipe';
import { SentenceCostPipe } from './pipes/sentence-cost.pipe';
import { CostFiilterPipe } from './pipes/cost-fiilter.pipe';


@NgModule({
  declarations: [
    RarityPipe,
    ElementPipe,
    LevelPipe,
    ItemPipe,
    SentenceCostPipe,
    CostFiilterPipe
  ],
  exports: [
    RarityPipe,
    ElementPipe,
    LevelPipe,
    ItemPipe,
    SentenceCostPipe,
    CostFiilterPipe,
    DragDropModule,
    MatDividerModule,
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

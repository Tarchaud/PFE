import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';

//Modules
import { FormsModule } from '@angular/forms';
import { HttpClientModule } from '@angular/common/http';
import { RarityPipe } from './pipes/rarity.pipe';
import { ElementPipe } from './pipes/element.pipe';


@NgModule({
  declarations: [
    RarityPipe,
    ElementPipe
  ],
  exports: [
    RarityPipe,
    ElementPipe,
    FormsModule,
    HttpClientModule,
  ],
  imports: [
    CommonModule,
    FormsModule,
    HttpClientModule,
  ]
})
export class SharedModule { }

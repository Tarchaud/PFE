import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';

//Modules
import { FormsModule } from '@angular/forms';
import { HttpClientModule } from '@angular/common/http';


@NgModule({
  declarations: [],
  exports: [
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

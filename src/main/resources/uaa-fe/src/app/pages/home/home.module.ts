import {NgModule} from '@angular/core';
import {CommonModule} from '@angular/common';
import {RouterModule} from '@angular/router';
import {ReactiveFormsModule} from '@angular/forms';
import {HomeRoutingModule} from './home-routing.module';


@NgModule({
  declarations: [
  ],
  imports: [
    CommonModule,
    RouterModule,
    ReactiveFormsModule,
    HomeRoutingModule
  ]
})
export class HomeModule {
}

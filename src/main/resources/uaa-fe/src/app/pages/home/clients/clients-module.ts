import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import {ClientsRoutingModule} from './clients-routing.module';
import {SharedModule} from '../shared/shared.module';



@NgModule({
  declarations: [],
  imports: [
    CommonModule,
    SharedModule,
    ClientsRoutingModule
  ]
})
export class ClientsModule { }

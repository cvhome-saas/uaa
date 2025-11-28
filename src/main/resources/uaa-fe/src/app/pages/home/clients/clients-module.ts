import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import {ClientsRoutingModule} from './clients-routing.module';
import {SharedModule} from '../shared/shared.module';
import { AgGridModule } from 'ag-grid-angular';



@NgModule({
  declarations: [],
  imports: [
    CommonModule,
    SharedModule,
    ClientsRoutingModule,
    AgGridModule
  ]
})
export class ClientsModule { }

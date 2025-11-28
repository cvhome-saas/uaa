import { Component } from '@angular/core';
import { SharedModule } from '../shared/shared.module';
import { AgGridAngular } from 'ag-grid-angular';
import { ColDef } from 'ag-grid-community';

@Component({
  selector: 'app-clients',
  imports: [
    SharedModule,
    AgGridAngular
  ],
  template:`
    <div class="page-content">
      <div class="page-body">

        <nb-card status="basic" accent="info">
          <nb-card-header>
            <div class="row">
              <h1 class="page_title">{{ 'CUSTOMERS.CUSTOMER_LIST' | translate }}</h1>
            </div>
          </nb-card-header>

          <nb-card-body [nbSpinner]="false" nbSpinnerSize="large" nbSpinnerStatus="primary">
            <ag-grid-angular
              style="width: 100%; height: 500px;"
              class="ag-theme-quartz"
              [rowData]="rowData"
              [columnDefs]="colDefs">
            </ag-grid-angular>
          </nb-card-body>
        </nb-card>

      </div>

    </div>
  `,
})
export class ClientsComponent  {
  rowData = [
    { make: "Tesla", model: "Model Y", price: 64950, electric: true },
    { make: "Ford", model: "F-Series", price: 33850, electric: false },
    { make: "Toyota", model: "Corolla", price: 29600, electric: false },
  ];

  colDefs: ColDef[] = [
    { field: "make" },
    { field: "model" },
    { field: "price" },
    { field: "electric" }
  ];
}

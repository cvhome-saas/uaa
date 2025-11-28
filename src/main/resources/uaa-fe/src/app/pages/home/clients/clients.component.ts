import { Component } from '@angular/core';
import {SharedModule} from '../shared/shared.module';

@Component({
  selector: 'app-clients',
  imports: [
    SharedModule,
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
          </nb-card-body>
        </nb-card>

      </div>

    </div>
  `,
})
export class ClientsComponent  {

}

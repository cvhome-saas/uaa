import {Routes} from '@angular/router';
import {HomeComponent} from './pages/home/home.component';
import {LoginComponent} from './pages/public/pages/login/login.component';
import {ClientLoginComponent} from './pages/public/pages/client-login/client-login.component';

export const routes: Routes = [
  {
    path: '',
    component: HomeComponent,
    children: [
      {
        path: "login",
        component: LoginComponent
      },
      {
        path: "client-login",
        component: ClientLoginComponent
      }
    ]
  }
];

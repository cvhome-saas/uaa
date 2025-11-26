import {Component} from '@angular/core';
import {RouterOutlet} from '@angular/router';
import {HeaderComponent} from './pages/public/componants/header/header.component';
import {FooterComponent} from './pages/public/componants/footer/footer.component';

@Component({
  selector: 'app-root',
  imports: [RouterOutlet, HeaderComponent, FooterComponent],
  template: `
    <div id="scrollUp" title="Scroll To Top">
      <i class="fas fa-arrow-up"></i>
    </div>
    <div class="main">
      <app-header/>
      <router-outlet/>
      <app-footer/>
    </div>
  `
})
export class AppComponent {
  title = 'uaa-fe';
}

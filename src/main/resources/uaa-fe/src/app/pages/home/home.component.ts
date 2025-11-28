import {Component} from '@angular/core';
import {RouterOutlet} from '@angular/router';
import {ThemeModule} from './theme/theme.module';
import {SharedModule} from './shared/shared.module';
import {MenuItem} from './menu-item';
import {MENU_ITEMS} from './pages-menu';
import {NbIconLibraries} from '@nebular/theme';
import {TranslateService} from '@ngx-translate/core';

@Component({
  selector: 'app-home',
  template: `
    <ngx-one-column-layout>
      <nb-menu [items]="menu"></nb-menu>
      <router-outlet></router-outlet>
    </ngx-one-column-layout>
  `,
  imports: [
    RouterOutlet,
    ThemeModule,
    SharedModule,
  ]
})
export class HomeComponent {
  menu: MenuItem[] = MENU_ITEMS;

  constructor(private iconLibraries: NbIconLibraries,
              private translateService: TranslateService,
  ) {
    this.iconLibraries.registerFontPack('font-awesome', {packClass: 'fa', iconClassPrefix: 'fa'});
    this.iconLibraries.setDefaultPack('font-awesome');
    this.translateService.use("en");
    this.translateService.onLangChange.subscribe((lang) => {
      this.translateMenu(this.menu);
    });

  }

  translateMenu(array) {
    array.forEach((el, index) => {
      el.title = this.translateService.instant(el.key);
      if (el.children) {
        this.translateMenu(el.children);
      }
    });
  }

}

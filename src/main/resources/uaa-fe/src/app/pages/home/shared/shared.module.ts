import {NgModule} from '@angular/core';
import {CommonModule} from '@angular/common';
import {
  NbActionsModule,
  NbAutocompleteModule,
  NbButtonModule,
  NbCardModule,
  NbChatModule,
  NbCheckboxModule,
  NbContextMenuModule,
  NbDatepickerModule,
  NbDialogModule,
  NbIconModule,
  NbInputModule,
  NbLayoutModule,
  NbMenuModule,
  NbOptionModule,
  NbRouteTabsetModule,
  NbSearchModule,
  NbSelectModule,
  NbSidebarModule,
  NbSpinnerModule,
  NbToastrModule,
  NbUserModule,
  NbWindowModule
} from '@nebular/theme';
import {TranslateModule} from '@ngx-translate/core';
import {FormsModule, ReactiveFormsModule} from '@angular/forms';

export const MODULES = [
  NbLayoutModule,
  NbMenuModule,
  NbCardModule,
  NbOptionModule,
  NbSelectModule,
  NbUserModule,
  NbActionsModule,
  NbSearchModule,
  NbSidebarModule,
  NbContextMenuModule,
  NbButtonModule,
  NbIconModule,
  TranslateModule,
  NbSidebarModule,
  NbMenuModule,
  NbDatepickerModule,
  NbDialogModule,
  NbWindowModule,
  NbToastrModule,
  NbChatModule,
  NbSpinnerModule,
  NbCheckboxModule,
  NbAutocompleteModule,
  NbInputModule,
  NbRouteTabsetModule,

  FormsModule,
  ReactiveFormsModule

];

const COMPONENTS: any[] = [];

@NgModule({
  declarations: [
    ...COMPONENTS
  ],
  imports: [
    CommonModule,
    ...MODULES
  ],
  exports: [
    CommonModule,
    ...MODULES,
    ...COMPONENTS
  ]
})
export class SharedModule {
}

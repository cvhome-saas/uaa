import { bootstrapApplication } from '@angular/platform-browser';
import { appConfig } from './app/app.config';
import { AppComponent } from './app/app.component';
import { ModuleRegistry, AllCommunityModule } from 'ag-grid-community';

import { provideGlobalGridOptions } from 'ag-grid-community';

// Mark all grids as using legacy themes
provideGlobalGridOptions({
  theme: "legacy",
});
ModuleRegistry.registerModules([ AllCommunityModule ]);

bootstrapApplication(AppComponent, appConfig)
  .catch((err) => console.error(err));

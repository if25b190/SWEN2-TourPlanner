import {ApplicationConfig, provideBrowserGlobalErrorListeners, provideZoneChangeDetection} from '@angular/core';
import {provideRouter} from '@angular/router';

import {routes} from './app.routes';
import {HTTP_INTERCEPTORS, provideHttpClient} from "@angular/common/http";
import {provideIcons} from "@ng-icons/core";
import {featherSettings} from "@ng-icons/feather-icons";
import {provideAnimations} from "@angular/platform-browser/animations";
import {provideToastr} from "ngx-toastr";
import {EVENT_MANAGER_PLUGINS} from "@angular/platform-browser";
import {DebounceEventPlugin} from "./debounce-event-plugin";

export const appConfig: ApplicationConfig = {
    providers: [
        provideBrowserGlobalErrorListeners(),
        provideZoneChangeDetection({eventCoalescing: true}),
        provideRouter(routes),
        provideHttpClient(),
        provideIcons({featherSettings}),
        provideAnimations(),
        provideToastr({progressBar: true}),
        {
            provide: EVENT_MANAGER_PLUGINS,
            useClass: DebounceEventPlugin,
            multi: true,
        }
    ]
};

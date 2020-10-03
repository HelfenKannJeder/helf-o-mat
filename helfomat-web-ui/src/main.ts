import './polyfills.ts';

import {platformBrowserDynamic} from '@angular/platform-browser-dynamic';
import {enableProdMode} from '@angular/core';
import {environment} from './environments/environment';
import {AppModule} from './app/';
import {KioskModule} from "./app/kiosk.module";

if (environment.production) {
  enableProdMode();
}

if (environment.kiosk) {
    platformBrowserDynamic().bootstrapModule(KioskModule);
} else {
    platformBrowserDynamic().bootstrapModule(AppModule);
}
